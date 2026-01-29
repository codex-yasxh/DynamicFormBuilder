package com.helloworld.dynamicformbuilder.form.engine

import androidx.compose.runtime.Composable
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState

interface FieldRenderer{
    @Composable
    fun Render(
        field : FieldSchema,
        state: FieldState,
        onValueChange: (Any?) -> Unit
    )
}