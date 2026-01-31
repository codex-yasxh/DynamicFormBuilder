package com.helloworld.dynamicformbuilder.form.engine

class FieldRegistry(
    private val renderers : Map<String, FieldRenderer>
){
    //it will be used in getting the renderer suitable to render and it will be called by registry
    fun getRenderer(type : String) : FieldRenderer?{ //inheriting an interface into class
        return renderers[type]
    }
}
