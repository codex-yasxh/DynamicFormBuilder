package com.helloworld.dynamicformbuilder.form.models

import com.helloworld.dynamicformbuilder.form.validation.ValidationRule

data class FieldSchema(
    val id : String,
    val type : String,
    val label : String,
    val required: Boolean = false,
    val options: List<String>? = null,
    val visibility: VisibilityRule? = null,
//    val validations: ValidationRule? = null, old code
    val validations : List<ValidationRule>? = null

)