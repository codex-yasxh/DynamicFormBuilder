package com.helloworld.dynamicformbuilder.form.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.helloworld.dynamicformbuilder.form.engine.FieldRegistry
import com.helloworld.dynamicformbuilder.form.fields.text.TextRenderer
import com.helloworld.dynamicformbuilder.form.models.RootSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState

@Preview(showSystemUi = true)
@Composable
fun DynamicFormScreen(
    rootSchema: RootSchema,
){
    // 1. Hold Form state
    //creating the registry to map the field types in JSON to renderers of that type
    val registry = remember {
        FieldRegistry(
            mapOf(
                "text" to TextRenderer()
                //similarly for boolean and dropdowns etc.
            )
        )
    }
     //displaying the title
    Text(rootSchema.screen.title)

    //looping thru all the fields
    rootSchema.fields.forEach { field ->

        // hey getRenderer help me find the exact renderer for this field
        //using the registry while rendering
        val renderer = registry.getRenderer(field.type)

        if(renderer !== null){
            // rendering the fields using Render func()
            renderer.Render(
                field = field,
                state = FieldState(),
                onValueChange = {
                    //update the value
                }
            )
        }
    }

}