package xyz.droidev.notelab.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.droidev.notelab.data.model.NoteResponse
import xyz.droidev.notelab.util.DateFormatter
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@Composable
fun NoteCard(
    note: NoteResponse,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(note.id) },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(note.title, fontSize = 18.sp, fontWeight = FontWeight(500))
            Text(note.content)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                DateFormatter.format(note.updatedAt!!),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp
            )
        }
    }
}