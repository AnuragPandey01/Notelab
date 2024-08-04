package xyz.droidev.notelab.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import xyz.droidev.notelab.ui.common.MessageBar

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedAuthOption by remember {
        mutableStateOf(AuthOptions.LOGIN)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                snackbar = { MessageBar.Error(msg = it.visuals.message) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "NoteLab.",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "\"Your Laboratory for Brilliant Ideas.\"",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.weight(0.5f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            ) {
                Row {
                    AuthOptions.entries.forEach {
                        val isSelected = (selectedAuthOption == it)
                        Text(
                            it.name.lowercase().replaceFirstChar { it.titlecaseChar() },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedAuthOption = it
                                }
                                .padding(10.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(selectedAuthOption == AuthOptions.SIGNUP) {
                SignupScreen(
                    onSuccess = onAuthSuccess,
                    onError = {
                        scope.launch { snackbarHostState.showSnackbar(it) }
                    }
                )
            }
            AnimatedVisibility(selectedAuthOption == AuthOptions.LOGIN) {
                LoginScreen(
                    onSuccess = onAuthSuccess,
                    onError = {
                        scope.launch { snackbarHostState.showSnackbar(it) }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

    }

}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen({})
}

enum class AuthOptions {
    LOGIN,
    SIGNUP
}