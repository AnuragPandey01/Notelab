package xyz.droidev.notelab.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.droidev.notelab.data.model.NoteResponse
import xyz.droidev.notelab.data.repository.NotesRepository
import xyz.droidev.notelab.util.NetworkResult
import javax.inject.Inject

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NotesRepository
): ViewModel(){

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state
        get() = _state.asStateFlow()

    fun getNotes(){
        viewModelScope.launch {
            repository.getNotes().collect{ res->
                when(res){
                    is NetworkResult.Success -> {

                        _state.update { HomeState.Success(
                            res.data?.filter { it.pinned } ?: emptyList(),
                            res.data?.filter { !it.pinned } ?: emptyList()
                        ) }
                    }
                    is NetworkResult.Error -> {
                        Log.d(TAG, "getNotes: ${res.message}")
                        _state.update { HomeState.Error(res.message!!) }
                    }
                    is NetworkResult.Loading -> {
                        _state.update { HomeState.Loading }
                    }
                }

            }
        }
    }
}

private const val TAG = "HomeViewModel"

sealed class HomeState{
    data object Loading: HomeState()
    data class Success(val pinnedNotes: List<NoteResponse>,val notes: List<NoteResponse>): HomeState()
    data class Error(val message: String): HomeState()
}