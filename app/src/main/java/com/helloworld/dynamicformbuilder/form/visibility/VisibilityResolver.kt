package com.helloworld.dynamicformbuilder.form.visibility

import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FormState

fun isFieldVisible(
    fieldSchema: FieldSchema,
    formState: FormState
): Boolean {

    //Case 1 : There's no visibility rule so always visible
    val rule = fieldSchema.visibility ?: return true

    // Find the field we depend on
    val dependencyState = formState.fields[rule.dependsOn] ?: return false //if dependency missing -> hide safely

    val dependencyValue = dependencyState.value

    return when (rule.condition){
        VisibilityCondition.EQUALS ->
            dependencyValue == rule.value
        //and if not equals
        VisibilityCondition.NOT_EQUALS ->
            dependencyValue != rule.value
    }

}