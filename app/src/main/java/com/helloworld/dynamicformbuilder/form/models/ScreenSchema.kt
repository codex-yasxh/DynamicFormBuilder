package com.helloworld.dynamicformbuilder.form.models

data class ScreenSchema(
    val description: String,
    val title : String,
    val submit : SubmitConfig? = null,
)
