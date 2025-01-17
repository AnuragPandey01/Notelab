package xyz.droidev.notelab.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.droidev.notelab.ui.common.PasswordField

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@Composable
fun SignupScreen(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<AuthViewModel>()
    val authState by viewModel.authState.collectAsState()

    var name: String by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    if(authState is AuthState.Success){
        LaunchedEffect(Unit){ onSuccess() }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label  = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label  = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        PasswordField(
            value = password,
            onValueChange = { password = it },
            labelText  = "Password",
            modifier = Modifier.fillMaxWidth(),
        )

        PasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            labelText  =  "Confirm Password",
            modifier = Modifier.fillMaxWidth(),
        )

        if(authState is AuthState.Loading){
            CircularProgressIndicator()
        }else{
            Button(
                onClick = { viewModel.signup(name,email,password)},
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Signup", fontSize = 20.sp)
            }
        }
    }
}

