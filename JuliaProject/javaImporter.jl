# include("/home/miguel/Desktop/PA/PA-Project/JuliaProject/javaImporter.jl")

using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref::JavaObject
    methods::Dict
end

Base.getproperty(jv::JCallInfo, sym::Symbol) =
            getfield(jv, :methods)[String(sym)]

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

    #Se calhar em vez de ter uma chave desta forma podemos ter apenas o nome da funcao e o value ser um vector de todos as redefiniçoes do metodo
    #Depois dependendo do que o utilizador colocar nos avaliamos o tipo do argumento e chamamos a função certa

    ret = JCallInfo(class, methodsDict)

    get!(importedClasses, alias, JCallInfo(class, methodsDict))
    # Meta.parse(alias * " = 
    #     importedClasses[alias]")
    return ret
end

function selectBestMethod(methods::Vector, values::Any...)
    print(values)
    print(values[1])
end

function findBestMethod(methods::Vector, values::Any...)
    finalMethod =""
    valid = true
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
   
    return finalMethod
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