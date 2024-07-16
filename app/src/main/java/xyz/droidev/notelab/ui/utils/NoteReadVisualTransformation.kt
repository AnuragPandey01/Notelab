package xyz.droidev.notelab.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
object NoteReadVisualTransformation: VisualTransformation {

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

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = offset
                offsets.forEach { (original, transformed) ->
                    if (offset >= original) {
                        transformedOffset -= 2
                    }
                    if (offset >= original + 1) {
                        transformedOffset -= 2
                    }
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = offset
                offsets.forEach { (original, transformed) ->
                    if (offset >= transformed) {
                        originalOffset += 2
                    }
                    if (offset >= transformed + 1) {
                        originalOffset += 2
                    }
                }
                return originalOffset
            }
        }

        return TransformedText(annotatedString.toAnnotatedString(), offsetMapping)
    }
}