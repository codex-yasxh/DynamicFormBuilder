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
            title = "Job Application Form",
            description = "Fill in your details"
        ),
        fields = listOf(

            // ---------- BASIC INFO ----------

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
                    )
                )
            ),

            FieldSchema(
                id = "last_name",
                type = "text",
                label = "Last Name",
                required = true
            ),

            FieldSchema(
                id = "email",
                type = "text",
                label = "Email",
                required = true,
                validations = listOf(
                    ValidationRule(
                        type = ValidationType.REGEX,
                        value = "^[A-Za-z0-9+_.-]+@(.+)$",
                        message = "Invalid email format"
                    )
                )
            ),

            FieldSchema(
                id = "dob",
                type = "date",
                label = "Date of Birth",
                required = true
            ),

            FieldSchema(
                id = "gender",
                type = "radio",
                label = "Gender",
                required = true,
                options = listOf("Male", "Female")
            ),

            FieldSchema(
                id = "phone",
                type = "number",
                label = "Phone Number",
                required = true
            ),

            // ---------- EMPLOYMENT ----------

            FieldSchema(
                id = "are_you_employed",
                type = "radio",
                label = "Are you employed?",
                required = true,
                options = listOf("Yes", "No")
            ),

            // ---------- CONDITIONAL FIELDS ----------

            FieldSchema(
                id = "company_name",
                type = "text",
                label = "Company Name",
                visibility = VisibilityRule(
                    dependsOn = "are_you_employed",
                    condition = VisibilityCondition.EQUALS,
                    value = "Yes"
                )
            ),

            FieldSchema(
                id = "job_title",
                type = "dropdown",
                label = "Job Title",
                options = listOf("Intern", "SDE", "Senior SDE", "Manager"),
                visibility = VisibilityRule(
                    dependsOn = "are_you_employed",
                    condition = VisibilityCondition.EQUALS,
                    value = "Yes"
                )
            ),

            FieldSchema(
                id = "skills",
                type = "text",
                label = "Skills",
                visibility = VisibilityRule(
                    dependsOn = "are_you_employed",
                    condition = VisibilityCondition.EQUALS,
                    value = "Yes"
                )
            ),

            FieldSchema(
                id = "experience",
                type = "dropdown",
                label = "Experience",
                options = listOf(
                    "0 years",
                    "1 year",
                    "2 years",
                    "3 years",
                    "4 years",
                    "5+ years"
                ),
                visibility = VisibilityRule(
                    dependsOn = "are_you_employed",
                    condition = VisibilityCondition.EQUALS,
                    value = "Yes"
                )
            )
        )
    )
}
