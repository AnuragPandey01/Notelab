package xyz.droidev.notelab.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import xyz.droidev.notelab.ui.theme.DarkGreen
import xyz.droidev.notelab.ui.theme.LightGreen

object MessageBar{

    @Composable
    fun Success(
        msg: String,
        modifier: Modifier = Modifier,
    ) {
        Snackbar(
            containerColor = if(isSystemInDarkTheme()) DarkGreen else LightGreen,
            contentColor = Color.White,
            modifier = modifier,
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                )
                Text(
                    text = msg,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

    @Composable
    fun Error(
        msg: String,
        modifier: Modifier = Modifier
    ) {

        val clipboardManager: ClipboardManager = LocalClipboardManager.current

        Snackbar(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            modifier = modifier,
            action = {
                TextButton(onClick = { clipboardManager.setText(AnnotatedString(msg)) }) {
                    Text(
                        text = "Copy",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                )
                Text(
                    text = msg,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

    @Composable
    fun Info(
        msg: String,
        modifier: Modifier = Modifier,
    ) {
        Snackbar(
            modifier = modifier,
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                )
                Text(
                    text = msg,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}