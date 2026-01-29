package com.helloworld.dynamicformbuilder.form.state

data class FieldState(
    val value : Any? = null,
    val error : String? = null,
    val isTouched : Boolean = false
)