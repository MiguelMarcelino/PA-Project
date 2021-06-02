using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref::JavaObject
    methods::Dict
end

struct JBestMethod
    #bestObject::JavaObject
    fullPath::String
end

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
            show(io, jcall(getfield(jv, :ref), "toString", JString, ()))

Base.getproperty(jv::JCallInfo, sym::Symbol) =
            (values...) -> findBestMethod(getfield(jv,:ref),getfield(jv, :methods)[String(sym)],values...)

#Base.getproperty(jv::JBestMethod, sym::Symbol) = 
#            importedClasses[getfield(jv,:fullPath)]

#Base.getproperty(jv::JavaObject, sym::Symbol) = print(typeof(sym))
#            (values...) -> findBestMethod(jv,getfield(importedClasses[jcall(jv, "getName", JString, ())], :methods)[String(sym)],values...)

function javaImport(fullPath::String)

    elem = get(importedClasses, fullPath, ())
    if(elem!=())
        print("asda") 
        return elem
    end

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

    ret = JCallInfo(class, methodsDict)
    get!(importedClasses, fullPath, ret)
    return ret
end


function findBestMethod(class::JavaObject, methods::Vector, values::Any...)
    if(isempty(methods)) 
        return "No Such Method"
    end

    finalMethod = methods[1][2]
    valid = true
    parsedMethods = []

    # remove all elements that don't match required length
    for method in methods
        if(length(method[1]) == length(values))
            push!(parsedMethods, method)
        end
    end

    for method in parsedMethods
        if(!isempty(method[1]))
            for i in eachindex(method[1])
                if !compareTypes(method[1][i],values[i])
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
   #este primeiro tem que ser para dos estaticos
    # value = jcall(class,finalMethod,values...)
    value = jcall(class,finalMethod,values...)
    if(typeof(class)!=JClass)
        class = jcall(class,"getClass",JClass,())
    end
    return_value = JCallInfo(value, getfield((importedClasses[jcall(class, "getCanonicalName", JString,())]), :methods))
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



time = javaImport("java.time.LocalDate")
now = time.now()
now.plusDays(4)

math = javaImport("java.lang.Math")
math.abs(-1)

time2 = javaImport("java.time.LocalDate") # Nao devia ser possivel
now.plusDays(4)


# TODO
# - Temos de tentar com objetos e com as subclasses e superclasses

