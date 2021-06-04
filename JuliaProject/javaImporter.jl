using Base: String, UInt8
using JavaCall: isprimitive, J_NULL, JavaCallError
using JavaCall

JavaCall.init(["-Xmx128M"])

# Stores a class reference (:ref) and Stores
# the original reference (:originalRed) to the
# object, so that its constructor can be called later
struct JCallInfo
    ref::Any
    originalRef::Any
    methods::Dict
end

# Stores class full path as key and JCallInfo as class
# information
importedclasses = Dict{String, JCallInfo}()

Base.show(io::IO, jv::JCallInfo) =
            if(typeof(getfield(jv, :ref)) <: JavaObject)
                if(isnull(getfield(jv, :ref)))
                    show(io, "NULL")
                else
                    show(io, jcall(getfield(jv, :ref), "toString", JString, ()))
                end
            else
                show(io, showJuliaType(getfield(jv, :ref)))
            end

Base.getproperty(jv::JCallInfo, sym::Symbol) =
            (values...) -> findBestMethod(getfield(jv,:ref),getfield(jv, :methods)[String(sym)],values...)


function javaimport(fullPath::String)
    elem = get(importedclasses, fullPath, ())
    if(elem!=())
        return elem
    end
    if(isempty(fullPath))
        error("Please provide a name for the class import")
    end

    originalRef = JavaCall.jimport(fullPath)
    class = classforname(fullPath)
    methods() = jcall(class, "getMethods", Vector{JMethod}, ())
    methodsDict = Dict()

    for method in (methods())
        methodName = jcall(method, "getName", JString,())
        methodParameterTypes() = jcall(method,"getParameterTypes",Vector{JClass},())
        parameterNames = []
        for name in (methodParameterTypes())
            push!(parameterNames, jcall(name, "getName", JString, ()))
        end

        functionsArray = get(methodsDict,methodName,[])
       
        finalParamNames = tuple(parameterNames...)
        push!(functionsArray,(finalParamNames, method))
        delete!(methodsDict,methodName)
        get!(methodsDict,methodName,functionsArray)
    end

    classRef = class
    if(typeof(class)!=JClass)
        classRef = jcall(class,"getClass",JClass,())
    end

    ret = JCallInfo(classRef, originalRef, methodsDict)
    get!(importedclasses, fullPath, ret)
    return ret
end

function newInstance(ji::JCallInfo, values::Any...)
    object = getfield(ji, :originalRef)
    types = []

    for value in values
        push!(types, typeof(convertToJavaType(value)))
    end

    methodTypes = tuple(types...)
    constructor = object(methodTypes, values...)
    return JCallInfo(convertToJavaType(constructor), object, getfield(ji, :methods))
end

function findBestMethod(class::Any, methods::Vector, values::Any...)
    if(isempty(methods)) 
        return "No Such Method"
    end

    # consider the created structure as input
    parsedValues = []
    for value in values
        if(typeof(value) <: JCallInfo)
            push!(parsedValues, convertToJavaType(getfield(value, :ref)))
        else
            push!(parsedValues, convertToJavaType(value))
        end
    end

    chosenmethod = nothing
    valid = true

    for method in methods
        if(length(method[1]) != length(parsedValues))
            continue
        end
        for i in eachindex(method[1])
            if !compareTypes(method[1][i],typeof(parsedValues[i]))
                valid = false
                break
            end
        end
        if(valid)
            chosenmethod = method[2]
        end
        valid = true
    end

    if(chosenmethod === nothing)
        error("No method found with the provided arguments")
    end

    value = jcall(class, chosenmethod, parsedValues...)

    classobject = class
    if(typeof(class)!=JClass)
        classobject = jcall(class,"getClass",JClass,())
    end

    object = importedclasses[jcall(classobject, "getCanonicalName", JString,())]
    return_value = JCallInfo(convertToJavaType(value), getfield(object,:originalRef), getfield(object,:methods))

    return return_value
end

##############################################################
######################### Converters #########################
##############################################################

function compareTypes(javaType::String, juliaType::Any)
    if juliaType <: jboolean
        if javaType == "boolean"
            return true
        end
    end
    if juliaType <: jchar
        if javaType == "char"
            return true
        end
    end
    if juliaType <: jint
        if javaType == "int"
            return true
        end
    end
    if juliaType <: jlong
        if javaType == "long"
            return true
        end
    end
    if juliaType <: jfloat
        if javaType == "float"
            return true
        end
    end
    if juliaType <: jdouble
        if javaType == "double"
            return true
        end
    end
    if juliaType <: JString
        if javaType == "String" || javaType == "java.lang.String" || javaType == "java.lang.Object"
            return true
        end
    end
    if juliaType <: JavaObject
        if javaType == "java.lang.Object"
            return true
        end
    end
    return false
end

# Convert types to Java types
function convertToJavaType(type::Any)
    if typeof(type) == String
        return JString(type)
    end
    if(typeof(type) == UInt8)
        return jboolean(type)
    end
    if(typeof(type) == UInt16 || typeof(type) == Char)
        return jchar(type)
    end
    if(typeof(type) == Int32)
        return jint(type)
    end
    if(typeof(type) == Int64)
        return jlong(type)
    end
    if(typeof(type) == Float32)
        return jfloat(type)
    end
    if(typeof(type) == Float64)
        return jdouble(type)
    end
    return type
end

function primitiveToObject(object::Any) 
    if typeof(object) <: String
        string = javaimport("java.lang.String")
        return newInstance(string, object)
    end
    if(typeof(object) == UInt8)
        boolean = javaimport("java.lang.Boolean")
        return newInstance(boolean, object)
    end
    if(typeof(object) == UInt16 || typeof(object) == Char)
        character = javaimport("java.lang.Character")
        return newInstance(character, object)
    end
    if(typeof(object) == Int32)
        integer = javaimport("java.lang.Integer")
        return newInstance(integer, object)
    end
    if(typeof(object) == Int64)
        long = javaimport("java.lang.Long")
        return newInstance(long, object)
    end
    if(typeof(object) == Float32)
        float = javaimport("java.lang.Float")
        return newInstance(float, object)
    end
    if(typeof(object) == Float64)
        double = javaimport("java.lang.Double")
        return newInstance(double, object)
    end
    return object
end

function showJuliaType(type::Any)
    if typeof(type) == UInt8
        return Bool(type)
    end
    return type
end


##############################################################
########################### Tests ############################
##############################################################

time = javaimport("java.time.LocalDate")
now = time.now()
now.plusDays(4)

math = javaimport("java.lang.Math")
math.abs(-1)

time2 = javaimport("java.time.LocalDate")
now.plusDays(4).plusDays(2).plusDays(4)

date = javaimport("java.util.Date")
newDate = newInstance(date)
newDate.getTime()
newDate.getDay()

hashMap = javaimport("java.util.HashMap")
jmap = newInstance(hashMap)
jmap.put("ola", "adeus")

url = javaimport("java.net.URL")
newUrl = newInstance(url, "http://www.google.com")

list = javaimport("java.util.ArrayList")
arrList = newInstance(list)
arrList.add("ola")
arrList.get(Int32(0)) # Tentar conversao caso nao haja mais de um metodo

string = javaimport("java.lang.String")
newstr = newInstance(string, "ola")
concatenated = newstr.concat("olaaaa")
concatConcat = concatenated.concat("dfudfhd")
testReplace = concatConcat.replace('d', 'c')

boolean = javaimport("java.lang.Boolean")
booleanConstr = newInstance(boolean, "false")

integer = javaimport("java.lang.Integer")
intConstructor = newInstance(integer, Int32(1))
value = intConstructor.intValue()
intConstructor.parseInt("2")

newList = newInstance(list)
newList.add(primitiveToObject(1))
newList.add(booleanConstr) # (nao é problema)

long = javaimport("java.lang.Long")
longImpl = newInstance(long, Int64(1))

void = javaimport("java.lang.Void")
newVoid = newInstance(void) # (valor que representa o Void)

class = javaimport("java.lang.Class")
stringClass = class.forName("java.lang.String") 
typeof(getfield(stringClass, :ref)) # retorna: JClass (alias for JavaObject{Symbol("java.lang.Class")})

random = javaimport("java.util.Random")
newrandom = newInstance(random, 122)
newrandom.nextLong()
newrandom.setSeed(0)
newrandom.nextInt()
newrandom.ints() # retorna: "java.util.stream.IntPipeline\$Head@52cc8049" --> toString

object = javaimport("java.lang.Object")
newobject = newInstance(object)


##############################################################
############################ TODO ############################
##############################################################

# - Quando criamos listas elas sao do tipo generico. Ou seja, 
#   aceitam qualquer tipo de objetos. Devemos permitir isso?
# - Devemos fazer conversao de tipos primitivos para objetos
#   por exemplo no caso das listas?
# - primitiveToObject podia ser uma macro?
# - Na API do Julia diz que usar "==" para comparar tipos pode 
#   nao ser ideal. Acha que devemos usar o "<:" ou o "isa"?
# - Ao tentar fazer class.forName("...") nao funciona
# - Ate que ponto é que macros sao uma boa ideia?

##############################################################
######################## Presentation ########################
##############################################################

# - Mention conversion of types (from julia types to Java types)
# - 