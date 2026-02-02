package com.helloworld.dynamicformbuilder.form.preview

import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.models.RootSchema
import com.helloworld.dynamicformbuilder.form.models.ScreenSchema

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
            ),
            FieldSchema(
                id = "last_name",
                type = "text",
                label = "Last Name",
                required = true
            )
        )
    )
}
