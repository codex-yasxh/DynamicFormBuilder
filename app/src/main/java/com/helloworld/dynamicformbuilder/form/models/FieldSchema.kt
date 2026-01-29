package com.helloworld.dynamicformbuilder.form.models

data class FieldSchema(
    val id : String,
    val type : String,
    val label : String,
    val required: Boolean = false,
    val value : Int,
    val options: List<String>? = null,
    val visibility: VisibilityRule? = null,
    val validations: ValidationRule? = null,

)