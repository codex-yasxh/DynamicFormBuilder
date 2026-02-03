package com.helloworld.dynamicformbuilder.form.validation

import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState
import com.helloworld.dynamicformbuilder.form.state.FormState
import com.helloworld.dynamicformbuilder.form.validation.ValidationType

fun validateField(
    fieldSchema: FieldSchema,
    fieldState: FieldState,
    formState: FormState
): String? {
    val rules = fieldSchema.validations ?: return null

    for (rule in rules) {
        val error = when (rule.type) {
            ValidationType.REQUIRED -> {
                if(fieldState.value  == null || fieldState.value.toString().isBlank()){
                    rule.message
                } else null
            }

            ValidationType.MIN_LENGTH -> {
                val min = rule.value as? Int ?: return rule.message
                val text = fieldState.value as? String ?: ""
                if(text.length < min){
                    rule.message
                } else null
            }

            ValidationType.MAX_LENGTH -> {
                val max = rule.value as? Int ?: return rule.message
                val text = fieldState.value as? String ?: ""
                if(text.length > max){
                   rule.message
                } else null
            }

            ValidationType.REGEX -> {
                val pattern = rule.value as? String ?: return rule.message
                val text = fieldState.value as? String ?: ""
                if (!Regex(pattern).matches(text)) rule.message else null
            }


        }
        if (error != null) return error
    }

    return null
}
