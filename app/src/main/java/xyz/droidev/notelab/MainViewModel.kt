package xyz.droidev.notelab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.droidev.notelab.data.repository.AuthRepository
import xyz.droidev.notelab.util.NetworkResult
import javax.inject.Inject

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel(){

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState
        get() = _loginState.asStateFlow()


    init{
        isLoggedIn()
    }

    private fun isLoggedIn(){
        viewModelScope.launch{
            repository.isLoggedIn().collect{
                when(it){
                    is NetworkResult.Error -> _loginState.value = LoginState.Error(it.message ?: "An error occurred")
                    is NetworkResult.Loading -> _loginState.value = LoginState.Loading
                    is NetworkResult.Success -> {
                        if (it.data == true) {
                            _loginState.value = LoginState.LoggedIn
                        } else {
                            _loginState.value = LoginState.NotLoggedIn
                        }
                    }
                }
            }
        }
    }

}

sealed class LoginState {
    data object Loading : LoginState()
    data object LoggedIn : LoginState()
    data object NotLoggedIn : LoginState()
    data class Error(val message: String) : LoginState()
}