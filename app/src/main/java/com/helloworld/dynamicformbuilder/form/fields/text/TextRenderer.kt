import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.helloworld.dynamicformbuilder.form.engine.FieldRenderer
import com.helloworld.dynamicformbuilder.form.models.FieldSchema
import com.helloworld.dynamicformbuilder.form.state.FieldState

class TextRenderer : FieldRenderer {

    @Composable
    override fun Render(
        field: FieldSchema,
        state: FieldState,
        onValueChange: (Any?) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()   // ✅ take full horizontal space
        ) {

            Text(
                text = field.label,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(), // ✅ THIS is the key
                value = state.value as? String ?: "",
                onValueChange = { newValue ->
                    onValueChange(newValue)
                }
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
