# include("/home/miguel/Desktop/PA/PA-Project/JuliaProject/javaImporter.jl")

using JavaCall

JavaCall.init(["-Xmx128M"])

struct JCallInfo
    ref: JavaObject
    methods::Dict
end

# Stores class alias as key and JCallInfo as class information
importedClasses = Dict{String, JCallInfo}


# typeMapper = Dict{String, }

function Base.show(io::IO, ::MIME"text/plain", v::Vector{JMethod}) where {T}
    println(io, "Vector with type $T and elements")
    for i in eachindex(v)
        println(io, "  ", v.v[i])
    end
end

# Base.show(io::IO, obj::JavaObject) = print(io, jcall(obj, "toString", JString, ()))
# Base.show(io::IO, obj::Vector{JMethod}) = print(io, jcall(obj, "toString", JString, ()))
# Base.show(io::IO, jv::JCallInfo) = show(io, getfield(jv, :ref))
# Base.getproperty(jv::JCallInfo, sym::Symbol) =getfield(jv, :methods)[sym](getfield(jv, :ref))

# jlM = @jimport java.lang.Math
function javaImport(fullPath::String)
    # 1) Put methods in dictionary (somehow)
    # 2) Create function called fullPath that calls necessary 
    #    methods from dictionary

    class = classforname(fullPath)
    # j_u_arrays = @jimport java.util.Arrays
    methods() = jcall(class, "getMethods", Vector{JMethod}, ())

    methodsDict = Dict()

    for i in eachindex(methods)
        method = methods()[i]
        method()
        methodName = jcall(method, "getName", JString,())
        methodParameterTypes = jcall(method,"getParameterTypes",Vector{JClass},())
        #dá erro aqui acho eu não percebo bem porque
        get!(methodsDict,Pair(methodName,methodParameterTypes),method)
    end
end
    method = methods()[1]
    method2 = methods()[2]
#(methodsDict,Pair("abs",Vector{classforname("java.lang.Float")}))


    jcall(first, "getName", JString,())

    classInfo = JCallInfo(class)

    # TODO
    Meta.parse(fullPath * "(methodName, args...) = {
        
    }")
    
# function typeMapper(javaType::String) {
# }


# jcall(jlM, "sin", jdouble, (jdouble,), pi/2)