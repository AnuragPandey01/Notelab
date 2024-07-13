package xyz.droidev.notelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.droidev.notelab.ui.screens.auth.AuthScreen
import xyz.droidev.notelab.ui.screens.home.HomeScreen
import xyz.droidev.notelab.ui.screens.note.NoteScreen
import xyz.droidev.notelab.ui.screens.splash.SplashScreen
import xyz.droidev.notelab.ui.theme.NotelabTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel by viewModels<MainViewModel>()

        setContent {
            NotelabTheme(
                darkTheme = true,
                dynamicColor = true
            ) {
                val navController = rememberNavController()
                val loginState by viewModel.loginState.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = when(loginState){
                        is LoginState.Loading -> Screen.SplashScreen
                        is LoginState.LoggedIn -> Screen.Home
                        is LoginState.NotLoggedIn -> Screen.Auth
                        is LoginState.Error -> finish()
                    },
                    modifier = Modifier.fillMaxSize()
                ){

                    composable<Screen.SplashScreen> { SplashScreen() }

                    composable<Screen.Auth> {
                        AuthScreen(
                            onAuthSuccess = { navController.navigate(Screen.Home) }
                        )
                    }

                    composable<Screen.Home> {
                        HomeScreen(
                            onNewNote = { navController.navigate(Screen.Note()) },
                            onNoteClick = { noteId -> navController.navigate(Screen.Note(noteId)) }
                        )
                    }
                    composable<Screen.Note> {
                        NoteScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

sealed class Screen {

    @Serializable
    data object SplashScreen : Screen()

    @Serializable
    data object Auth : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data class Note(
        val noteId: String? = null
    ) : Screen()
}




