package xyz.droidev.notelab.ui.screens.note

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.droidev.notelab.data.model.Note
import xyz.droidev.notelab.data.repository.NotesRepository
import xyz.droidev.notelab.util.NetworkResult
import java.util.Date
import javax.inject.Inject

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@HiltViewModel
class NoteScreenViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var noteId = savedStateHandle.get<String?>("noteId")
    private val isNewNote = noteId == null

    var title by mutableStateOf("")
    var content by mutableStateOf("")
    var isInEditMode by mutableStateOf(isNewNote)
    var updatedAt by mutableStateOf(Date(System.currentTimeMillis()))
    var pinned by mutableStateOf(false)

    var state by mutableStateOf(NoteScreenState.IDLE)

    init{
        if(isNewNote.not()){
            viewModelScope.launch {
                notesRepository.getNote(noteId!!).collectLatest {
                    when(it){
                        is NetworkResult.Error -> {
                            state = NoteScreenState.FAILED
                            Log.e(TAG, "init: ${it.message}")
                        }
                        is NetworkResult.Loading -> {
                            state = NoteScreenState.LOADING
                        }
                        is NetworkResult.Success -> {
                            title = it.data!!.title
                            content = it.data.content
                            state = NoteScreenState.IDLE
                            updatedAt = it.data.updatedAt!!
                            pinned = it.data.pinned
                        }
                    }
                }
            }
        }
    }


    fun addNote(){
        if(title.isEmpty() || content.isEmpty()){
            return
        }
        if(noteId == null){
            insertNote()
        }else{
            saveNote()
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            notesRepository.updateNote(
                noteId = noteId!!,
                newNote = Note(title = title, content = content, pinned = pinned)
            ).collect{
                when(it){
                    is NetworkResult.Error -> {
                        state = NoteScreenState.FAILED
                        Log.e(TAG, "saveNote: ${it.message}")
                    }
                    is NetworkResult.Loading -> {
                        state = NoteScreenState.LOADING
                    }
                    is NetworkResult.Success -> {
                        isInEditMode = false
                        state = NoteScreenState.IDLE
                    }
                }
            }
        }
    }

    private fun insertNote() {
        viewModelScope.launch {
            notesRepository.insertNote(
                Note(
                    title = title,
                    content = content
                )
            ).collect{
                when(it){
                    is NetworkResult.Error -> {
                        state = NoteScreenState.FAILED
                        Log.e(TAG, "addNote: ${it.message}")
                    }
                    is NetworkResult.Loading -> {
                        state = NoteScreenState.LOADING
                    }
                    is NetworkResult.Success -> {
                        isInEditMode = false
                        noteId = it.data!!.id
                        state = NoteScreenState.IDLE
                    }
                }
            }
        }
    }

    fun pinNote(){
        viewModelScope.launch {
            notesRepository.togglePin(noteId!!, !pinned).collectLatest {
                when(it){
                    is NetworkResult.Error -> {
                        state = NoteScreenState.FAILED
                        Log.e(TAG, "pinNote: ${it.message}")
                    }
                    is NetworkResult.Loading -> {
                        state = NoteScreenState.LOADING
                    }
                    is NetworkResult.Success -> {
                        pinned = !pinned
                        state = NoteScreenState.IDLE
                    }
                }
            }
        }
    }

    fun deleteNote(){
        viewModelScope.launch {
            notesRepository.deleteNote(noteId!!).collectLatest {
                when(it){
                    is NetworkResult.Error -> {
                        state = NoteScreenState.FAILED
                        Log.e(TAG, "deleteNote: ${it.message}")
                    }
                    is NetworkResult.Loading -> {
                        state = NoteScreenState.LOADING
                    }
                    is NetworkResult.Success -> {
                        state = NoteScreenState.DELETED
                    }
                }
            }
        }
    }

    companion object{
        private const val TAG = "NoteScreenViewModel"
    }
}

enum class NoteScreenState{
    IDLE,
    LOADING,
    FAILED,
    DELETED
}
