package xyz.droidev.notelab.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
object NoteReadModeText {

    fun apply(text: AnnotatedString): AnnotatedString{
        val combinedPattern = Regex("__(.*?)__|\\*\\*(.*?)\\*\\*|~~(.*?)~~")

        val rawText = text.text
        val annotatedString = AnnotatedString.Builder()
        val offsets = mutableListOf<Pair<Int, Int>>()

        var lastIndex = 0

        combinedPattern.findAll(rawText).forEach { matchResult ->
            val match = matchResult.value
            val start = matchResult.range.first
            val end = matchResult.range.last + 1

            // Append the text before the match
            annotatedString.append(rawText.substring(lastIndex, start))

            val contentStart = annotatedString.length
            var content = match
            var style = SpanStyle()

            when {
                match.startsWith("__") -> {
                    style = SpanStyle(fontStyle = FontStyle.Italic)
                    content = match.removeSurrounding("__")
                }
                match.startsWith("**") -> {
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                    content = match.removeSurrounding("**")
                }
                match.startsWith("~~") -> {
                    style = SpanStyle(textDecoration = LineThrough)
                    content = match.removeSurrounding("~~")
                }
            }

            annotatedString.append(AnnotatedString(content, style))
            offsets.add(Pair(start, contentStart))
            lastIndex = end
        }

        // Append the remaining text
        if (lastIndex < rawText.length) {
            annotatedString.append(rawText.substring(lastIndex))
        }

        return annotatedString.toAnnotatedString()
    }
}
