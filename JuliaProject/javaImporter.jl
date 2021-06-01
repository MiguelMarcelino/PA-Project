# include("/home/miguel/Desktop/PA/PA-Project/JuliaProject/javaImporter.jl")

using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref::JavaObject
    methods::Dict
end

Base.show(io::IO, obj::JavaObject) =
            print(io, jcall(obj, "toString", JString, ()))

#Temos de tentar com objetos e com as subclasses e superclasses

#Esta a dar erro se for sem parametros por exemplo import java.time.LocalDate   date.now()
Base.getproperty(jv::JCallInfo, sym::Symbol) =
            (values...)->findBestMethod(getfield(jv,:ref),getfield(jv, :methods)[String(sym)],values...)


# Stores class alias as key and JCallInfo as class information
importedClasses = Dict{String, JCallInfo}()

# to call= jcall(math.ref,method,args)

function javaImport(alias::String, fullPath::String)
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

    ret = JCallInfo(class, methodsDict)
    get!(importedClasses, alias, JCallInfo(class, methodsDict))
    return ret
end


function findBestMethod(class::JavaObject,methods::Vector, values::Any...)
    finalMethod = methods[1][2]
    valid = true
    if(!isempty(values))
        for method in methods
            for i in eachindex(method[1])
                if !compareTypes(method[1][i],values[i])
                    valid = false
                    break
                end
            end
            if(valid)
                finalMethod = method[2]
            end
            valid = true
        end
        if(finalMethod == "") 
            return "No Such Method"
        end        
    end 
    value = jcall(class,finalMethod,values...)
    return value
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



#get(methodsDict,Pair("abs",Vector{classforname("java.lang.Float")}))

# jcall(first, "getName", JString,())
# classInfo = JCallInfo(class)

# TODO
# Meta.parse(fullPath * "(methodName, args...) = {
# }")