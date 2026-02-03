package com.helloworld.dynamicformbuilder.form.fields.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.helloworld.dynamicformbuilder.form.engine.FieldRenderer
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState

//rendering the textField UI using FieldSchema(get the value from the schema),
// FieldState(Map will store these and treated as runtime memory)
// and Callback to update value.


class TextRenderer : FieldRenderer{
    @Composable
    override fun Render(field: FieldSchema, state: FieldState, onValueChange: (Any?) -> Unit) {
        Column(
            modifier = Modifier.padding(128.dp).fillMaxWidth()
        ) {
            Text(field.label)

            TextField(
                value = state.value as? String?: "",
                onValueChange = { newValue ->
                    onValueChange(newValue)
                }
            )
        }

        //getting if getting error also show it
        if(state.error != null){
            Column(
                modifier = Modifier.padding(32.dp,32.dp)
            ) {
                Text(
                    state.error,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }

        }
    }
}


