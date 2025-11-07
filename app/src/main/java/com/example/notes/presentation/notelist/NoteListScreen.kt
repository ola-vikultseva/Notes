package com.example.notes.presentation.notelist

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes.presentation.notelist.components.NoteCard
import com.example.notes.presentation.notelist.components.RadialActionMenu

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    onEditNote: (Int) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }
    var selectedNoteId by remember { mutableIntStateOf(-1) }

    LaunchedEffect(showMenu) {
        if (!showMenu) selectedNoteId = -1
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = { onEditNote(selectedNoteId) },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
//                uiState.categories.forEach { category ->
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Button(
//                        onClick = { },
//                        colors = ButtonColors(
//                            containerColor = MaterialTheme.colorScheme.primary,
//                            contentColor = MaterialTheme.colorScheme.onPrimary,
//                            disabledContainerColor = MaterialTheme.colorScheme.primary,
//                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
//                        ),
//                        modifier = Modifier.height(56.dp)
//                    ) {
//                        Text(text = category.name)
//                    }
//                }
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = uiState.notes.size,
                    key = { index ->
                        uiState.notes[index].id
                    }
                ) { index ->
                    NoteCard(
                        note = uiState.notes[index],
                        onClick = { offset, note ->
                            selectedNoteId = note.id
                            menuPosition = offset
                            showMenu = true
                        },
                        modifier = Modifier.animateItem(
                            fadeInSpec = tween(durationMillis = 500),
                            fadeOutSpec = tween(durationMillis = 500),
                            placementSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    )
                }
            }
        }
        if (showMenu) {
            RadialActionMenu(
                position = menuPosition,
                onPinClick = { },
                onEditClick = {
                    onEditNote(selectedNoteId)
                    showMenu = false
                },
                onDeleteClick = {
                    viewModel.onDeleteNote(selectedNoteId)
                    showMenu = false
                },
                onDismiss = { showMenu = false }
            )
        }
    }
}