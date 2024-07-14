package xyz.droidev.notelab.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import xyz.droidev.notelab.R

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText : String = "Password",
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(labelText) },
        modifier = modifier,
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible)
                        ImageVector.vectorResource(id = R.drawable.ic_visiblity_on)
                    else
                        ImageVector.vectorResource(id = R.drawable.ic_visiblity_off),
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        )
    )
}