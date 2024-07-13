package xyz.droidev.notelab.util

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
object DateFormatter {
    private val formatter = SimpleDateFormat("dd LLL yyyy", Locale.getDefault())
    fun format(date: Any): String = formatter.format(date)
}