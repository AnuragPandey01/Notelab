package xyz.droidev.notelab.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.util.Date


/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */

@Keep
data class NoteResponse (

    @SerializedName("\$id")
    val id: String,

    val title: String,

    val content: String,

    val pinned: Boolean,

    @SerializedName("\$createdAt")
    val createdAt: Date? = null,

    @SerializedName("\$updatedAt")
    val updatedAt: Date? = null,
)
@Keep
data class Note (
    val title: String,
    val content: String,
    val pinned: Boolean= false
)