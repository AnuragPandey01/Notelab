package xyz.droidev.notelab.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.droidev.notelab.R
import xyz.droidev.notelab.data.local.PrefManager
import xyz.droidev.notelab.ui.common.NoteCard

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewNote: () -> Unit,
    onNoteClick : (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val ctx = LocalContext.current

    LaunchedEffect(Unit){
        viewModel.getNotes()
        Log.d("TAG", "HomeScreen: ${PrefManager(ctx).getString(PrefManager.Keys.USER_ID)}")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNewNote() },
                shape = CircleShape,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        },
    ){ innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ){
            when(val state = viewModel.state.collectAsState().value){
                is HomeState.Loading -> {
                    CircularProgressIndicator()
                }
                is HomeState.Success -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .then(modifier),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if(state.pinnedNotes.isNotEmpty()){
                            item(span = StaggeredGridItemSpan.FullLine ){
                                Text("pinned", fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
                            }
                            items(state.pinnedNotes, span = { StaggeredGridItemSpan.FullLine }) { note ->
                                NoteCard(
                                    onClick = { onNoteClick(it) },
                                    note = note,
                                )
                            }
                            item(span = StaggeredGridItemSpan.FullLine ){
                                Spacer(modifier = Modifier.padding(16.dp))
                                Text("others", fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                        items(state.notes) { note ->
                            NoteCard(
                                onClick = { onNoteClick(it) },
                                note = note
                            )
                        }
                    }
                }
                is HomeState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text("An error occurred: ${state.message}", fontSize = 20.sp)
                        Button(onClick = { viewModel.getNotes() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}