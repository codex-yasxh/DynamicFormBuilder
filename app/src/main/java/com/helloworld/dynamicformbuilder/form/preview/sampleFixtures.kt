package com.helloworld.dynamicformbuilder.form.preview

import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema
import com.helloworld.dynamicformbuilder.form.models.ScreenSchema
import com.helloworld.dynamicformbuilder.form.validation.ValidationRule
import com.helloworld.dynamicformbuilder.form.validation.ValidationType

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
                    )
                ),

            )
        )
    )
}
