package com.helloworld.dynamicformbuilder.form.fields.text

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.helloworld.dynamicformbuilder.form.engine.FieldRenderer
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState

//rendering the textField UI using FieldSchema(get the value from the schema),
// FieldState(Map will store these and treated as runtime memory)
// and Callback to update value.


class TextRenderer : FieldRenderer{
    @Composable
    @Composable
    override fun Render(field: FieldSchema, state: FieldState, onValueChange: (Any?) -> Unit) {
        Text(field.label)

        TextField(
            value = state.value as? String?: "",
            onValueChange = { newValue ->
                onValueChange(newValue)
            }
        )
        //getting if getting error also show it
        if(state.error != null){
            Text(state.error)
        }
    }
}


