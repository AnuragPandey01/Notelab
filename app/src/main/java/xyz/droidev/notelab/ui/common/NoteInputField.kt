package xyz.droidev.notelab.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import xyz.droidev.notelab.ui.util.NoteEditModeText

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@Composable
fun NoteInputField(
    modifier: Modifier = Modifier,
    value : AnnotatedString,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    isInEditMode: Boolean,
    textStyle: TextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    if(isInEditMode){
        BasicTextField(
            value = value.toString(),
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .then(modifier),
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        placeHolder,
                        style = textStyle
                    )
                }
                innerTextField()
            },
            visualTransformation = {
                TransformedText(NoteEditModeText.apply(it), OffsetMapping.Identity)
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        )
    }else{
        Text(
            text = value,
            style = textStyle,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .then(modifier),
        )
    }

}