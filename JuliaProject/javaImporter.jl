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

# Stores class full path as key and JCallInfo as class information
importedClasses = Dict{String, JCallInfo}()

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

Base.show(io::IO, nt::Nothing) = show(io, "Nothing")

Base.getproperty(jv::JCallInfo, sym::Symbol) =
            (values...) -> findBestMethod(getfield(jv,:ref),getfield(jv, :methods)[String(sym)],values...)

function javaImport(fullPath::String)
    elem = get(importedClasses, fullPath, ())
    if(elem!=())
        return elem
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
    get!(importedClasses, fullPath, ret)
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

    # consider our own structure as input
    parsedValues = []
    for value in values
        if(typeof(value) <: JCallInfo)
            push!(parsedValues, getfield(value, :ref))
        else
            push!(parsedValues, value)
        end
    end

    finalMethod = methods[1][2]
    valid = true

    for method in methods
        if(length(method[1]) != length(parsedValues))
            continue
        end
        if(!isempty(method[1]))
            for i in eachindex(method[1])
                if !compareTypes(method[1][i],parsedValues[i])
                    valid = false
                    break
                end
            end
        end
        if(valid)
            finalMethod = method[2]
        end
        valid = true
    end

    value = jcall(class, finalMethod, parsedValues...)

    classobject = class
    if(typeof(class)!=JClass)
        classobject = jcall(class,"getClass",JClass,())
    end

    object = importedClasses[jcall(classobject, "getCanonicalName", JString,())]
    return_value = JCallInfo(convertToJavaType(value), getfield(object,:originalRef), getfield(object,:methods))

    return return_value
end

function compareTypes(javaType::Any,juliaType::Any)
    if juliaType isa jboolean
        if javaType == "boolean"
            return true
        end
    end
    if juliaType isa jchar
        if javaType == "char"
            return true
        end
    end
    if juliaType isa jint
        if javaType == "int"
            return true
        end
    end
    if juliaType isa jlong
        if javaType == "long"
            return true
        end
    end
    if juliaType isa jfloat
        if javaType == "float"
            return true
        end
    end
    if juliaType isa jdouble
        if javaType == "double"
            return true
        end
    end
    if juliaType isa String
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
    if(typeof(type) == UInt16)
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

function showJuliaType(type::Any)
    if typeof(type) == UInt8
        return Bool(type)
    end
    return type
end


##############################################################
########################### Tests ############################
##############################################################

time = javaImport("java.time.LocalDate")
now = time.now()
now.plusDays(4)

math = javaImport("java.lang.Math")
math.abs(-1)

time2 = javaImport("java.time.LocalDate")
now.plusDays(4).plusDays(2).plusDays(4)

date = javaImport("java.util.Date")
newDate = newInstance(date)
newDate.getTime()
newDate.getDay()

hashMap = javaImport("java.util.HashMap")
jmap = newInstance(hashMap)
jmap.put("ola", "adeus")

url = javaImport("java.net.URL")
newUrl = newInstance(url, "http://www.google.com")

list = javaImport("java.util.ArrayList")
arrList = newInstance(list)
arrList.add("ola")
arrList.get(0)

string = javaImport("java.lang.String")
newstr = newInstance(string, "ola")
concatenated = newstr.concat("olaaaa")
concatenated.concat("dfudfhd")

boolean = javaImport("java.lang.Boolean")
booleanConstr = newInstance(boolean, "false")
boolean.FALSE() # static fields. Should we deal with this?


##############################################################
############################ TODO ############################
##############################################################

# - Temos de tentar com objetos e com as subclasses e superclasses
