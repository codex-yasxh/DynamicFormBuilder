package com.helloworld.dynamicformbuilder.form.screens

import TextRenderer
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.helloworld.dynamicformbuilder.form.engine.FieldRegistry
import com.helloworld.dynamicformbuilder.form.fields.date.DateRenderer
import com.helloworld.dynamicformbuilder.form.fields.dropdown.DropdownRenderer
import com.helloworld.dynamicformbuilder.form.fields.radio.RadioRenderer
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema
import com.helloworld.dynamicformbuilder.form.preview.sampleFixtures
import com.helloworld.dynamicformbuilder.form.state.FieldState
import com.helloworld.dynamicformbuilder.form.state.FormState
import com.helloworld.dynamicformbuilder.form.submission.SubmissionState
import com.helloworld.dynamicformbuilder.form.validation.validateField
import com.helloworld.dynamicformbuilder.form.visibility.isFieldVisible
import kotlinx.coroutines.delay
//import java.lang.reflect.Field
import kotlin.collections.plus
import kotlin.text.get

//import kotlin.plus
//import kotlin.text.get


@Composable
fun DynamicFormScreen(
    rootSchema: RootSchema,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        //creating the registry to map the field types in JSON to renderers of that type
        val registry = remember {
            FieldRegistry(
                mapOf(
                    "text" to TextRenderer(),
                    "radio" to RadioRenderer(),
                    "dropdown" to DropdownRenderer(),
                    "date" to DateRenderer()
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

        //displaying the title
        StaticHeader(rootSchema.screen.title)


        //looping thru all the fields within a scrollable container
        Column(
            modifier = Modifier
                .weight(1f) // ← occupies remaining space
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rootSchema.fields.filter { field ->
                isFieldVisible(field, formState)
            }.forEach { field ->


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
                    fieldId: String,
                    newValue: Any?
                ) {
                    val oldFieldState = formState.fields[fieldId]
                        ?: return //only reading the old state that how it changed

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
                        onValueChange = { newValue ->
                            updateFieldValue(field.id, newValue)
                        }
                    )
                }
            }
        }


        // isFormValid = all FieldState.error == null AND required filled
// and also derivedState is function of FormState

        val isFormValid by remember(formState) {
            derivedStateOf {
                rootSchema.fields.all { fieldSchema ->

                    val fieldState = formState.fields[fieldSchema.id] ?: return@derivedStateOf false

                    // If field is required, it must have a value and no error
                    if (fieldSchema.required) {
                        fieldState.value != null && fieldState.error == null
                    } else {
                        // Optional field is valid if it has no error
                        fieldState.error == null
                    }
                }
            }
        }


        fun validateOnSubmit() {
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

        Button(
            onClick = {
                if (submissionState is SubmissionState.Idle) {
                    submissionState = SubmissionState.Submitting
                    Log.d("SUBMISSION", "State changed to Submitting")
                    val payload = buildSubmitPayload(rootSchema, formState)
                    Log.d("PAYLOAD", payload.toString())

                }
            },
            enabled = submissionState is SubmissionState.Idle,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")

        }

        fun runValidation(): Boolean {
            var updatedFields = formState.fields
            var hasError = false

            rootSchema.fields.forEach { fieldSchema ->

                val fieldState = formState.fields[fieldSchema.id] ?: return@forEach
                val isVisible = isFieldVisible(fieldSchema, formState)


                if (!isVisible) {
                    updatedFields = updatedFields + (
                            fieldSchema.id to fieldState.copy(error = null)
                            )
                    return@forEach
                }
                //if field is visible then we need to handle errors as well
                val error = validateField(
                    fieldSchema = fieldSchema,
                    fieldState = fieldState,
                    formState = formState
                )

                updatedFields = updatedFields + (
                        fieldSchema.id to fieldState.copy(
                            error = error,
                            isTouched = true
                        )
                        )
                if (error != null) hasError = true
            }

            formState = formState.copy(fields = updatedFields)
            return hasError
        }

        //used only when submissionState changes
        LaunchedEffect(submissionState) {
            if (submissionState is SubmissionState.Submitting) {

                val hasErrors = runValidation()
                if (hasErrors) {
                    submissionState = SubmissionState.Idle
                    return@LaunchedEffect
                }

                // Validation passed → build payload
                val payload = buildSubmitPayload(rootSchema, formState)
                Log.d("PAYLOAD", payload.toString())

                // Fake API call
                val result = fakeSubmitApi(payload)

                submissionState = if (result.isSuccess) {
                    SubmissionState.Success
                } else {
                    SubmissionState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        }

        when (submissionState) {
            SubmissionState.Success -> {
                Text(
                    text = "Form submitted successfully!",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            is SubmissionState.Error -> {
                Text(
                    text = (submissionState as SubmissionState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            else -> Unit
        }

    }




}

@Composable
fun StaticHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Black,
        textAlign = TextAlign.Center
    )
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


fun buildSubmitPayload(
    rootSchema: RootSchema,
    formState: FormState
): Map<String, Any?> {

    val payload = mutableMapOf<String, Any?>()

    rootSchema.fields.forEach { fieldSchema ->
        if (!isFieldVisible(fieldSchema, formState)) {
            return@forEach
        }
        val fieldState = formState.fields[fieldSchema.id] ?: return@forEach

        val value = when (fieldSchema.type) {
            "text" -> fieldState.value as? String
            "number" -> fieldState.value as? Int
            "boolean" -> fieldState.value as? Boolean
            "date" -> fieldState.value?.toString() // later format properly
            else -> fieldState.value
        }

        payload[fieldSchema.id] = value
    }

    return payload
}

suspend fun fakeSubmitApi(payload: Map<String, Any?>): Result<Unit> {
    delay(1500) // simulate network delay

    return if (payload.isNotEmpty()) {
        Result.success(Unit)
    } else {
        Result.failure(Exception("Submission failed"))
    }
}



@Preview(showBackground = true)
@Composable
fun DynamicFormScreenPreview() {
    DynamicFormScreen(
        rootSchema = sampleFixtures.simpleTextForm
    )
}




