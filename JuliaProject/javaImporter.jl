# include("/home/miguel/Desktop/PA/PA-Project/JuliaProject/javaImporter.jl")

using JavaCall

JavaCall.init(["-Xmx128M"])

# Stores class alias as key and JCallInfo as class information
importedClasses = Dict{String, JCallInfo}

typeMapper = Dict{String, }

struct JCallInfo
    ref: JavaObject
    methods::Dict
end

# jlM = @jimport java.lang.Math
function javaImport(fullPath::String)
    jlM = @jimport fullPath
    class = jcall(jlM, getClass, JObject, ())
    methods = jcall(class, getMethods, JObject, ())
    # 1) Put methods in dictionary (somehow)
    # 2) Create function called fullPath that calls necessary 
    #    methods from dictionary

    # TODO
    Meta.parse(fullPath * "(methodName, args...) = {
        
    }")
    
function typeMapper(javaType::String) {
    
}


jcall(jlM, "sin", jdouble, (jdouble,), pi/2)