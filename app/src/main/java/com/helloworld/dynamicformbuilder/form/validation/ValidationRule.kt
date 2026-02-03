package com.helloworld.dynamicformbuilder.form.validation

data class ValidationRule(
    val type : ValidationType,
    val value : Any,
    val message : String
)