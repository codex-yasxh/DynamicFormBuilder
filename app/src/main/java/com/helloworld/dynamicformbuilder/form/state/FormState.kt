package com.helloworld.dynamicformbuilder.form.state

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

data class FormState(
    val fields : Map<String, FieldState>
)
// isFormValid = all FieldState.error == null AND required filled
// and also derivedState is function of Formstate

val isFormValid by remember(formState) {
    derivedStateOf {
        formSchema.fields.all{
            
        }
    }
}
