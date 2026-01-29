package com.helloworld.dynamicformbuilder.form.engine

class FieldRegistry(
    private val renderers : Map<String, FieldRenderer>
){
    fun getRenderer(type : String) : FieldRenderer?{ //inheriting an interface into class
        return renderers[type]
    }
}
