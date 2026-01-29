package com.helloworld.dynamicformbuilder.form.models

data class VisibilityRule(
    val dependsOn : String,
    val condition : String,
    val value : Any
)
