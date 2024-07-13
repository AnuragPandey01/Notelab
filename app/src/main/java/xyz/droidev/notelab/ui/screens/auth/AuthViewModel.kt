package xyz.droidev.notelab.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState
        get() = _authState.asStateFlow()

    fun signup(
        name: String,
        email: String,
        password: String
    ){
        viewModelScope.launch {
            authRepository.signup(name,email, password).collect{
                when(it){
                    is NetworkResult.Loading -> _authState.value = AuthState.Loading
                    is NetworkResult.Success -> _authState.value = AuthState.Success
                    is NetworkResult.Error -> _authState.value = AuthState.Error(it.message!!)
                }
            }
        }
    }

    fun login(
        email: String,
        password: String
    ){
        viewModelScope.launch {
            authRepository.login(email, password).collect{
                when(it){
                    is NetworkResult.Loading -> _authState.value = AuthState.Loading
                    is NetworkResult.Success -> _authState.value = AuthState.Success
                    is NetworkResult.Error -> _authState.value = AuthState.Error(it.message!!)
                }
            }
        }
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}