package com.helloworld.dynamicformbuilder.form.models


data class ValidationRule(
    val rule : String,
    val value : Any,
    val errorMessage : String
)