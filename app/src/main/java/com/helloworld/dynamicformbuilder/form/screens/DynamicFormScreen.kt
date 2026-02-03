package com.helloworld.dynamicformbuilder.form.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
//import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.helloworld.dynamicformbuilder.form.engine.FieldRegistry
import com.helloworld.dynamicformbuilder.form.fields.text.TextRenderer
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema
import com.helloworld.dynamicformbuilder.form.preview.sampleFixtures
import com.helloworld.dynamicformbuilder.form.state.FieldState
import com.helloworld.dynamicformbuilder.form.state.FormState
import com.helloworld.dynamicformbuilder.form.submission.SubmissionState
import com.helloworld.dynamicformbuilder.form.validation.validateField
//import java.lang.reflect.Field
import kotlin.collections.plus
import kotlin.text.get

//import kotlin.plus
//import kotlin.text.get


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
    var formState by remember {
        mutableStateOf(
            FormState(
                fields = rootSchema.fields.associate { field ->
                    field.id to FieldState()
                }
            )
        )
    }

    var submissionState by remember {
        mutableStateOf<SubmissionState>(SubmissionState.Idle)
    }


    @Composable
    fun StaticHeader(

    ) {
        Log.d("RECOMPOSE", "Header recomposed")
        Text(rootSchema.screen.title)
    }

     //displaying the title
    StaticHeader()

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

    // isFormValid = all FieldState.error == null AND required filled
// and also derivedState is function of FormState

    val isFormValid by remember(formState) {
        derivedStateOf {
            rootSchema.fields.all{ fieldSchema ->

                val fieldState = formState.fields[fieldSchema.id] ?: return@derivedStateOf false

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


    fun validateOnSubmit(){
        var updateFields = formState.fields

        rootSchema.fields.forEach { fieldSchema ->
            val fieldState = formState.fields[fieldSchema.id] ?: return@forEach

            val error = validateField(
                fieldSchema = fieldSchema,
                fieldState = fieldState,
                formState = formState
            )
            val updateFieldState = fieldState.copy(
                error = error,
                isTouched = true
            )
            updateFields = updateFields + (fieldSchema.id to updateFieldState)
            Log.d("VALIDATION", "Validating field: ${fieldSchema.id}, error = $error")
        }

        formState = formState.copy(fields = updateFields)
        Log.d("SUBMIT", "button clicked")
        Log.d("FORM_STATE", formState.fields.toString())


    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp)
    ){
        Button(
            onClick = {
                if(submissionState is SubmissionState.Idle){
                    submissionState = SubmissionState.Submitting
                    Log.d("SUBMISSION", "State changed to Submitting")
                }
            },
               enabled = submissionState is SubmissionState.Idle
            ) {
            Text("Submit")

        }
    }

    fun runValidation(): Boolean {
        var updatedFields = formState.fields
        var hasError = false

        rootSchema.fields.forEach { fieldSchema ->
            val fieldState = formState.fields[fieldSchema.id] ?: return@forEach

            val error = validateField(
                fieldSchema = fieldSchema,
                fieldState = fieldState,
                formState = formState
            )

            if (error != null) {
                hasError = true
            }

            updatedFields = updatedFields + (
                    fieldSchema.id to fieldState.copy(
                        error = error,
                        isTouched = true
                    )
                    )
        }

        formState = formState.copy(fields = updatedFields)
        return hasError
    }

    //used only when submissionState changes

    LaunchedEffect(submissionState){

        if(submissionState is SubmissionState.Submitting){
            val hasErrors = runValidation()

            if(hasErrors){
                submissionState = SubmissionState.Idle
            }
            // else stay in submitting state
        }
    }

}
//4.4 only recomposing the specific affected field
@Composable
fun FieldItem(
    field : FieldSchema,
    fieldState : FieldState,
    registry: FieldRegistry,
    onValueChange : (Any?) -> Unit
){
    Log.d("RECOMPOSE", "FieldItem recomposed: ${field.id}")
    val renderer = registry.getRenderer(field.type) ?: return

    renderer.Render(
        field = field,
        state = fieldState,
        onValueChange = onValueChange
    )
}





@Preview(showBackground = true)
@Composable
fun DynamicFormScreenPreview() {
    DynamicFormScreen(
        rootSchema = sampleFixtures.simpleTextForm
    )
}




