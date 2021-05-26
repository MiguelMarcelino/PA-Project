# include("/home/miguel/Desktop/PA/PA-Project/JuliaProject/javaImporter.jl")

using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref::JClass
    methods::Dict
end

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
        get!(methodsDict,Pair(methodName,finalParamNames),method)
    end

    get!(importedClasses, alias, JCallInfo(class, methodsDict))
    # Meta.parse(alias * " = 
    #     importedClasses[alias]")
end


#get(methodsDict,Pair("abs",Vector{classforname("java.lang.Float")}))

# jcall(first, "getName", JString,())
# classInfo = JCallInfo(class)

# TODO
# Meta.parse(fullPath * "(methodName, args...) = {
# }")