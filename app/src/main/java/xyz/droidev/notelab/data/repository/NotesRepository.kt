package xyz.droidev.notelab.data.repository

import android.util.Log
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.extensions.fromJson
import io.appwrite.extensions.toJson
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.droidev.notelab.data.local.PrefManager
import xyz.droidev.notelab.data.model.Note
import xyz.droidev.notelab.data.model.NoteResponse
import xyz.droidev.notelab.util.Constants
import xyz.droidev.notelab.util.NetworkResult

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
class NotesRepository(
    private val database: Databases,
    private val client: Client,
    private val prefManager: PrefManager
) {

    private val userId
        get() = prefManager.getString(PrefManager.Keys.USER_ID)!!

    /**
     * inserts a note
     * @param note the note to insert
     * @return a flow of [NetworkResult] of [Note]
     */
    suspend fun insertNote(
        note: Note
    ): Flow<NetworkResult<NoteResponse>> = flow {
        emit(NetworkResult.Loading())
        val perms = listOf(
            Permission.read(Role.user(userId)),
            Permission.delete(Role.user(userId)),
            Permission.update(Role.user(userId)),
        )
        Log.d("perms", perms.toString())
        try {
            val result = database.createDocument(
                databaseId = Constants.DATABASE_ID,
                collectionId = Constants.NOTES_COLLECTION_ID,
                documentId = ID.unique(),
                data = note,
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.delete(Role.user(userId)),
                    Permission.update(Role.user(userId)),
                ),
            )
            emit(NetworkResult.Success(result.data.toJson().fromJson()))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.stackTraceToString()))
        }
    }

    /**
     * gets all notes
     * @return a flow of [NetworkResult] of list of [Note]
     * */
    suspend fun getNotes(): Flow<NetworkResult<List<NoteResponse>>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = database.listDocuments(
                databaseId = Constants.DATABASE_ID,
                collectionId = Constants.NOTES_COLLECTION_ID
            )
            emit(NetworkResult.Success(response.documents.map { it.data.toJson().fromJson() }))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message))
        }
    }

    /**
     * toggles the pin status of a note to the passed state
     * @param noteId the id of the note to toggle the pin status
     * @param isPinned the new pin status
     * @return a flow of [NetworkResult] of updated [Note]
     * */
    suspend fun togglePin(
        noteId: String,
        isPinned: Boolean
    ): Flow<NetworkResult<Note>> = flow {
        emit(NetworkResult.Loading())
        try {
            val result = database.updateDocument(
                databaseId = Constants.DATABASE_ID,
                collectionId = Constants.NOTES_COLLECTION_ID,
                documentId = noteId,
                data = mapOf("pinned" to isPinned),
            )
            emit(NetworkResult.Success(result.toJson().fromJson()))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message))
        }
    }

    /**
     * gets a note by its id
     * @param noteId the id of the note to get
     * @return a flow of [NetworkResult] of [Note]
     * */
    suspend fun getNote(
        noteId: String
    ): Flow<NetworkResult<NoteResponse>> = flow {
        emit(NetworkResult.Loading())
        try {
            val result = database.getDocument(
                databaseId = Constants.DATABASE_ID,
                collectionId = Constants.NOTES_COLLECTION_ID,
                documentId = noteId
            )
            emit(NetworkResult.Success(result.data.toJson().fromJson()))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message))
        }
    }

    /**
     * deletes a note by its id
     * @param noteId the id of the note to delete
     * @return a flow of [NetworkResult] of [Unit]
     * */
    suspend fun deleteNote(
        noteId: String
    ): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading())
        try {
            database.deleteDocument(
                databaseId = Constants.DATABASE_ID,
                collectionId = Constants.NOTES_COLLECTION_ID,
                documentId = noteId
            )
            emit(NetworkResult.Success(Unit))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message))
        }
    }

    suspend fun updateNote(
        newNote: Note,
        noteId : String
    ): Flow<NetworkResult<NoteResponse>> = flow {
        emit(NetworkResult.Loading())
        try {
            val result = database.updateDocument(
                databaseId = Constants.DATABASE_ID,
                collectionId = Constants.NOTES_COLLECTION_ID,
                documentId = noteId,
                data = newNote,
            )
            emit(NetworkResult.Success(result.toJson().fromJson()))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message))
        }
    }

}