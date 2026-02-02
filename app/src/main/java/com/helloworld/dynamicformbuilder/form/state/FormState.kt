package com.helloworld.dynamicformbuilder.form.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema

data class FormState(
    val fields : Map<String, FieldState>
)

