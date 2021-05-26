# include("/home/miguel/Desktop/PA/PA-Project/JuliaProject/javaImporter.jl")

using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref::JClass
    methods::Dict
end

# Stores class alias as key and JCallInfo as class information
importedClasses = Dict{String, JCallInfo}

# typeMapper = Dict{String, }

# function Base.show(io::IO, ::MIME"text/plain", v::Vector{JMethod}) where {T}
#     println(io, "Vector with type $T and elements")
#     for i in eachindex(v)
#         println(io, "  ", v.v[i])
#     end
# end

# Base.show(io::IO, obj::JavaObject) = print(io, jcall(obj, "toString", JString, ()))
# Base.show(io::IO, obj::Vector{JMethod}) = print(io, jcall(obj, "toString", JString, ()))
# Base.show(io::IO, jv::JCallInfo) = show(io, getfield(jv, :ref))
# Base.getproperty(jv::JCallInfo, sym::Symbol) =getfield(jv, :methods)[sym](getfield(jv, :ref))

# jlM = @jimport java.lang.Math
methodsDict = Dict()

function javaImport(fullPath::String)
    class = classforname(fullPath)
    methods() = jcall(class, "getMethods", Vector{JMethod}, ())

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
end

math = JCallInfo(class, methodsDict)
math.abs(4)
#get(methodsDict,Pair("abs",Vector{classforname("java.lang.Float")}))

# jcall(first, "getName", JString,())
# classInfo = JCallInfo(class)

# TODO
# Meta.parse(fullPath * "(methodName, args...) = {
# }")