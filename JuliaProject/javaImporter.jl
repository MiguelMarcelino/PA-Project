using JavaCall: isprimitive
using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref::Any
    originalRef::Any
    methods::Dict
end

# struct JBestMethod
#     bestObject::JavaObject
#     fullPath::String
# end

# Stores class full path as key and JCallInfo as class information
importedClasses = Dict{String, JCallInfo}()

#Base.show(io::IO, jv::JCallInfo) =
#    show(io, getfield(jv, :ref))

#_getproperty(jv::JCallInfo, sym::Symbol, values::Any...) =  
#            (values...) -> findBestMethod(getfield(jv,:ref),getfield(jv, :methods)[String(sym)],values...)

# javaImport da erro com isto
#_getproperty(jv::JavaObject, sym::Symbol, values::Any...) = 
#            (values...) -> findBestMethod(jv,getfield(importedClasses[jcall(jv, "getName", JString, ())], :methods)[String(sym)],values...)

Base.show(io::IO, jv::JCallInfo) =
            if(typeof(getfield(jv, :ref)) <: JavaObject)
                show(io, jcall(getfield(jv, :ref), "toString", JString, ()))
            else
                show(io, getfield(jv, :ref))
            end


Base.getproperty(jv::JCallInfo, sym::Symbol) =
            (values...) -> findBestMethod(getfield(jv,:ref),getfield(jv, :methods)[String(sym)],values...)

#Base.getproperty(jv::JBestMethod, sym::Symbol) = 
#            importedClasses[getfield(jv,:fullPath)]

#Base.getproperty(jv::JavaObject, sym::Symbol) = print(typeof(sym))
#            (values...) -> findBestMethod(jv,getfield(importedClasses[jcall(jv, "getName", JString, ())], :methods)[String(sym)],values...)

function javaImport(fullPath::String)
    elem = get(importedClasses, fullPath, ())
    if(elem!=())
        return elem
    end

    originalRef = JavaCall.jimport(fullPath)
    class = classforname(fullPath)
    methods() = jcall(class, "getMethods", Vector{JMethod}, ())
    methodsDict = Dict()
    print(typeof(methodsDict))

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
        push!(types, convertTypes(typeof(value)))
    end

    print(types)

    methodTypes = tuple(types...)
    constructor = object(methodTypes, values...)
    return JCallInfo(constructor, object, getfield(ji, :methods))
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
    return_value = JCallInfo(value, getfield(object,:originalRef), getfield(object,:methods))

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
    return false
end

# TODO: Is this necessary?
function convertTypes(type::Any)
    if type == String
        return JString
    end
    return type
end

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

hashMap = javaImport("java.util.HashMap")
jmap = newInstance(hashMap) # erro no show

url = javaImport("java.net.URL")
newUrl = newInstance(url, "http://www.google.com")
# TODO
# - Temos de tentar com objetos e com as subclasses e superclasses
