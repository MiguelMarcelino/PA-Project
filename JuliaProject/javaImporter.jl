# include("/home/miguel/Desktop/PA/PA-Project/JuliaProject/javaImporter.jl")

using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref::JavaObject
    methods::Dict
end

Base.getproperty(jv::JCallInfo, sym::Symbol) =
            getfield(jv, :methods)[sym](getfield(jv, :ref))

# Stores class alias as key and JCallInfo as class information
importedClasses = Dict{String, JCallInfo}()

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
        finalParamNames = tuple(parameterNames...)
        get!(methodsDict,(methodName,finalParamNames),method)
    end

    #Se calhar em vez de ter uma chave desta forma podemos ter apenas o nome da funcao e o value ser um vector de todos as redefiniçoes do metodo
    #Depois dependendo do que o utilizador colocar nos avaliamos o tipo do argumento e chamamos a função certa

    ret = JCallInfo(class, methodsDict)

    get!(importedClasses, alias, JCallInfo(class, methodsDict))
    # Meta.parse(alias * " = 
    #     importedClasses[alias]")
    return ret
end


#get(methodsDict,Pair("abs",Vector{classforname("java.lang.Float")}))

# jcall(first, "getName", JString,())
# classInfo = JCallInfo(class)

# TODO
# Meta.parse(fullPath * "(methodName, args...) = {
# }")