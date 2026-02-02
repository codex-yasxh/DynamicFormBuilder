package com.helloworld.dynamicformbuilder.form.validation

import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState
import com.helloworld.dynamicformbuilder.form.state.FormState

fun validateField(
    fieldSchema: FieldSchema,
    fieldState: FieldState,
    formState: FormState
): String? {
    val rules = fieldSchema.validations ?: return null

    for (rule in rules) {
        val error = when (rule.validation) {
            ValidationType.REQUIRED -> {
                if(fieldState.value  == null || fieldState.value.toString().isBlank()){
                    rule.errorMessage
                } else null
            }

            ValidationType.MIN_LENGTH -> {
                val min = rule.value as? Int ?: return rule.errorMessage
                val text = fieldState.value as? String ?: ""
                if(text.length < min){
                    rule.errorMessage
                } else null
            }

            ValidationType.MAX_LENGTH -> {
                val max = rule.value as? Int ?: return rule.errorMessage
                val text = fieldState.value as? String ?: ""
                if(text.length > max){
                   rule.errorMessage
                } else null
            }

            ValidationType.REGEX -> {
                val pattern = rule.value as? String ?: return rule.errorMessage
                val text = fieldState.value as? String ?: ""
                if (!Regex(pattern).matches(text)) rule.errorMessage else null
            }


            else -> null
        }
        if (error != null) return error
    }

    return null
}
