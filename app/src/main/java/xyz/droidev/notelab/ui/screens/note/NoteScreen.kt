package xyz.droidev.notelab.ui.screens.note

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.droidev.notelab.R
import xyz.droidev.notelab.ui.common.NoteInputField
import xyz.droidev.notelab.ui.util.NoteReadModeText
import xyz.droidev.notelab.util.DateFormatter

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onBack: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<NoteScreenViewModel>()

    if (viewModel.state == NoteScreenState.DELETED) {
        LaunchedEffect(Unit) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (viewModel.isInEditMode) {
                        TextButton(
                            onClick = { viewModel.addNote() },
                            enabled = viewModel.state != NoteScreenState.LOADING,
                        ) {
                            Text("Save")
                        }
                        return@TopAppBar
                    }
                    IconButton(
                        onClick = { viewModel.isInEditMode = true },
                    ) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_edit),
                            contentDescription = "Edit",
                        )
                    }
                    IconButton(
                        onClick = { viewModel.pinNote() },
                    ) {
                        Icon(
                            if (viewModel.pinned) ImageVector.vectorResource(id = R.drawable.ic_pin)
                            else ImageVector.vectorResource(id = R.drawable.ic_pin_outline),
                            contentDescription = "Pin",
                        )
                    }
                    IconButton(
                        onClick = { viewModel.deleteNote() },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_delete),
                            contentDescription = "Delete",
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_color_palette),
                        contentDescription = "options",
                    )
                }
                Text(
                    "edited ${DateFormatter.format(viewModel.updatedAt)}",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert, contentDescription = "options",
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .imePadding()
        ) {
            AnimatedVisibility(viewModel.state == NoteScreenState.LOADING) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            NoteInputField(
                value = if(viewModel.isInEditMode) AnnotatedString(viewModel.title)
                    else NoteReadModeText.apply(AnnotatedString(viewModel.title)),
                onValueChange = { viewModel.title = it },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                placeHolder = "Title",
                isInEditMode = viewModel.isInEditMode,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            NoteInputField(
                value = if (viewModel.isInEditMode) AnnotatedString(viewModel.content)
                    else NoteReadModeText.apply(AnnotatedString(viewModel.content)),
                onValueChange = { viewModel.content = it },
                modifier = Modifier
                    .weight(1f),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
                isInEditMode = viewModel.isInEditMode,
                placeHolder = "Content"
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewNoteScreen() {
    NoteScreen()
}