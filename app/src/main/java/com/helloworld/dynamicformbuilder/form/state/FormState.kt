package com.helloworld.dynamicformbuilder.form.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema

data class FormState(
    val fields : Map<String, FieldState>
)
// isFormValid = all FieldState.error == null AND required filled
// and also derivedState is function of Formstate

val isFormValid by remember(formState) {
    derivedStateOf {
        RootSchema.fields.all{


            val fieldState = formState.fields[FieldSchema.id] ?: return@derivedStateOf false

            // If field is required, it must have a value and no error
            if(fieldSchema.required){
                fieldState.value != null && fieldState.error == null
            }else{
                // Optional field is valid if it has no error
                fieldState.error == null
            }
        }
    }
}
