package com.helloworld.dynamicformbuilder.form.models

data class RootSchema(
    val fields : List<FieldSchema>,
    val screen : ScreenSchema
)