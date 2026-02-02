package com.helloworld.dynamicformbuilder.form.validation

data class validationRule(
    val type : ValidationType,
    val value : Any? = null,
    val message : String
)