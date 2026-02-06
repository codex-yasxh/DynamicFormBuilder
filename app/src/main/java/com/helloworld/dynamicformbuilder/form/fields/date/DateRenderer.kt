package com.helloworld.dynamicformbuilder.form.fields.date

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.helloworld.dynamicformbuilder.form.engine.FieldRenderer
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState
import java.util.*

class DateRenderer : FieldRenderer {

    @Composable
    override fun Render(
        field: FieldSchema,
        state: FieldState,
        onValueChange: (Any?) -> Unit
    ) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = field.label,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                val date = "%04d-%02d-%02d".format(
                                    year,
                                    month + 1,
                                    day
                                )
                                onValueChange(date)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                readOnly = true,
                value = state.value as? String ?: "",
                onValueChange = {},
                placeholder = { Text("Select date") }
            )

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
