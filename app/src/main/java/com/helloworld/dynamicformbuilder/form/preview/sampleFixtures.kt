package com.helloworld.dynamicformbuilder.form.preview

import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema
import com.helloworld.dynamicformbuilder.form.models.ScreenSchema
import com.helloworld.dynamicformbuilder.form.validation.ValidationRule
import com.helloworld.dynamicformbuilder.form.validation.ValidationType
import com.helloworld.dynamicformbuilder.form.visibility.VisibilityCondition
import com.helloworld.dynamicformbuilder.form.visibility.VisibilityRule

object sampleFixtures {

    val simpleTextForm = RootSchema(

        screen = ScreenSchema(
            title = "Sample Dynamic Form",
            description = "Preview only"
        ),
        fields = listOf(
            FieldSchema(
                id = "first_name",
                type = "text",
                label = "First Name",
                required = true,
                validations = listOf(
                    ValidationRule(
                        type = ValidationType.REQUIRED,
                        message = "First name is required",
                        value = true
                    ),
                    ValidationRule(
                        type = ValidationType.MIN_LENGTH,
                        value = 3,
                        message = "Minimum 3 characters"
                    ),
                    ValidationRule(
                        type = ValidationType.REGEX,
                        value = "^[a-zA-Z0-9_]+$",
                        message = "Only letters, numbers, underscore allowed"
                    )
                ),
            ),
            FieldSchema(
                id = "company_name",
                type = "text",
                label = "Company Name",
                visibility = VisibilityRule(
                    dependsOn = "are_you_employed",
                    condition = VisibilityCondition.EQUALS,
                    value = true
                )
            ),
        )
    )
}
