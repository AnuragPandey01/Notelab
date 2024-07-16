package xyz.droidev.notelab.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.withStyle

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
object NoteEditVisualTransformation: VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
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
            var stylingChars = ""

            when {
                match.startsWith("__") -> {
                    style = SpanStyle(fontStyle = FontStyle.Italic)
                    content = match.removeSurrounding("__")
                    stylingChars = "__"
                }
                match.startsWith("**") -> {
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                    content = match.removeSurrounding("**")
                    stylingChars = "**"
                }
                match.startsWith("~~") -> {
                    style = SpanStyle(textDecoration = LineThrough)
                    content = match.removeSurrounding("~~")
                    stylingChars = "~~"
                }
            }
            annotatedString.withStyle(SpanStyle(color = Color.LightGray)){
                append(stylingChars)
            }
            annotatedString.append(AnnotatedString(content, style))
            annotatedString.withStyle(SpanStyle(color = Color.LightGray)){
                append(stylingChars)
            }

            offsets.add(Pair(start, contentStart))
            lastIndex = end
        }

        // Append the remaining text
        if (lastIndex < rawText.length) {
            annotatedString.append(rawText.substring(lastIndex))
        }

        return TransformedText(annotatedString.toAnnotatedString(), OffsetMapping.Identity)
    }
}