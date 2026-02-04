package com.helloworld.dynamicformbuilder.form.visibility

data class VisibilityRule(
    val dependsOn: String,
    val condition: VisibilityCondition,
    val value: Any
)