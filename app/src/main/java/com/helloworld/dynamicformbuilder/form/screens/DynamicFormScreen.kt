package com.helloworld.dynamicformbuilder.form.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.helloworld.dynamicformbuilder.form.engine.FieldRegistry
import com.helloworld.dynamicformbuilder.form.fields.text.TextRenderer
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState
import com.helloworld.dynamicformbuilder.form.state.FormState
import java.lang.reflect.Field
import kotlin.collections.plus
import kotlin.plus
import kotlin.text.get

@Preview(showSystemUi = true)
@Composable
fun DynamicFormScreen(
    rootSchema: RootSchema,
){

    //creating the registry to map the field types in JSON to renderers of that type
    val registry = remember {
        FieldRegistry(
            mapOf(
                "text" to TextRenderer()
                //similarly for boolean and dropdowns etc.
            )
        )
    }
    // 3.1 and 3.2 Hold Form state
    val formState = remember {
        FormState(
            fields = rootSchema.fields.associate { field ->
                field.id to FieldState() // textFieldID -> textFieldState(value = null, error = null, isTouched = false)
            }
        )
    }




     //displaying the title
    Text(rootSchema.screen.title)

    //looping thru all the fields
    rootSchema.fields.forEach { field ->


        // hey getRenderer help me find the exact renderer for this field
        //using the registry while rendering
//        val renderer = registry.getRenderer(field.type)
//
//        if(renderer !== null){
//            // rendering the fields using Render func()
//            renderer.Render(
//                field = field,
//                state = FieldState(),
//                onValueChange = {
//                    //update the value -> 3.4
//                    newValue -> updateFieldValue(field.id, newValue)
//                }
//            )
//        }
        // above is old code

        //now new code is

        val fieldState = formState.fields[field.id] ?: return@forEach

        //4.3 and 4.5
        key(field.id) {
            FieldItem(
                field = field,
                fieldState = fieldState,
                registry = registry,
                onValueChange = {
                        newValue -> updateFieldValue(field.id, newValue)
                }
            )
        }
    }

}
    fun updateFieldValue(
        fieldId : String,
        newValue : Any?
    ){
        val oldFieldState = formState.fields[fieldId] ?: return //only reading the old state that how it changed

        val updatedFieldState = oldFieldState.copy(
            value = newValue,
            isTouched = true
        ) //created brand new object with FieldState(newValue, isTouched = true) while having the exact things as oldFieldState


        formState = formState.copy(
            fields = formState.fields + (fieldId to updatedFieldState) //fieldID is new id here "to" i for you know 'mapping to new field instance'
            // left side of + is map<fieldID, FieldState()> and right side is new entry or pair
            // now + creates a new map and insert the pair
            // insertion rule : if key already exists, replace the value of that particular pair else entry is added
        ) //Give me a NEW map where everything stays the same, EXCEPT this one key.

    }

//4.4 only recomposing the specific affected field
@Composable
fun FieldItem(
    field : FieldSchema,
    fieldState : FieldState,
    registry: FieldRegistry,
    onValueChange : (Any?) -> Unit
){
    val renderer = registry.getRenderer(field.type) ?: return

    renderer.Render(
        field = FieldSchema,
        state = FieldState,
        onValueChange = onValueChange
    )
}


