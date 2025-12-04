package com.example.notes.presentation.notelist

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteCategory
import com.example.notes.presentation.notelist.components.NoteCard
import com.example.notes.presentation.notelist.components.RadialActionMenu
import com.example.notes.presentation.notelist.model.NoteListUiState
import com.example.notes.presentation.theme.NotesTheme

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    onEditNoteClick: (Int) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    NoteListScreen(
        uiState = uiState.value,
        onUndoDeleteClick = viewModel::onUndoDeleteClick,
        onSnackbarDismissed = viewModel::onSnackbarDismissed,
        onEditNoteClick = onEditNoteClick,
        onCategoryFilterChanged = viewModel::onCategoryFilterChanged,
        onPinToggleClick = viewModel::onPinToggleClick,
        onDeleteNoteClick = viewModel::onDeleteNoteClick,
    )
}

@Composable
fun NoteListScreen(
    uiState: NoteListUiState,
    onUndoDeleteClick: () -> Unit,
    onSnackbarDismissed: () -> Unit,
    onEditNoteClick: (Int) -> Unit,
    onCategoryFilterChanged: (List<Int>?) -> Unit,
    onPinToggleClick: (Int) -> Unit,
    onDeleteNoteClick: (Int) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }
    var selectedNoteId by remember { mutableIntStateOf(-1) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSnackbarVisible) {
        if (uiState.isSnackbarVisible) {
            val result = snackbarHostState.showSnackbar(
                message = "Note deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Long
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    onUndoDeleteClick()
                }
                SnackbarResult.Dismissed -> {
                    onSnackbarDismissed()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        actionColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Gray,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    FloatingActionButton(
                        onClick = { onEditNoteClick(selectedNoteId) },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
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
                }
                items(uiState.categories) { category ->
                    val selectedIds = uiState.selectedCategoryIds.orEmpty()
                    val isSelected = category.id in selectedIds
                    val onCategoryClick = {
                        val updatedSelection = if (!isSelected) {
                            selectedIds + category.id
                        } else {
                            selectedIds - category.id
                        }
                        onCategoryFilterChanged(updatedSelection.takeIf { it.isNotEmpty() })
                    }
                    Button(
                        onClick = { onCategoryClick() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            disabledContentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                        ),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text(text = category.name)
                    }
                }
            }
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            snackbarHostState.currentSnackbarData?.dismiss()
            RadialActionMenu(
                position = menuPosition,
                onPinClick = {
                    onPinToggleClick(selectedNoteId)
                    showMenu = false
                },
                onEditClick = {
                    onEditNoteClick(selectedNoteId)
                    showMenu = false
                },
                onDeleteClick = {
                    onDeleteNoteClick(selectedNoteId)
                    showMenu = false
                },
                onDismiss = {
                    showMenu = false
                }
            )
        }
    }
}

@Preview(
    name = "Light Theme",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun NoteListScreenPreview() {
    val categories = listOf(
        NoteCategory(
            id = 1,
            name = "Thoughts",
        ),
        NoteCategory(
            id = 2,
            name = "Recipes",
        ),
        NoteCategory(
            id = 3,
            name = "Books",
        )
    )
    val notes = listOf(
        Note(
            id = 1,
            title = "A Moment of Calm",
            content = "This morning, the world was quiet. The sky was painted in soft pink and gold. I took a deep breath and felt peaceful. Sometimes, small moments like this are the most magical.",
            categoryIds = listOf(1),
            isPinned = false,
        ),
        Note(
            id = 2,
            title = "Unexpected Joy",
            content = "A cat jumped onto my windowsill and stared at me. I smiled for no reason—sometimes happiness finds you in the smallest ways.",
            categoryIds = listOf(1),
            isPinned = false,
        ),
        Note(
            id = 3,
            title = "Cozy Winter Reads",
            content = "The Night Circus” by Erin Morgenstern – a beautifully written fantasy full of mystery, love, and winter charm.\n“Little Women” by Louisa May Alcott – a heartwarming classic about family, dreams, and growing up.\n“The Midnight Library” by Matt Haig – perfect for reflection as the year ends — what if you could live all your possible lives?",
            categoryIds = listOf(3),
            isPinned = false,
        ),
        Note(
            id = 4,
            title = "Reading List",
            content = "Next books: 'Dune' by Frank Herbert, 'Atomic Habits' by James Clear, 'Project Hail Mary' by Andy Weir.",
            categoryIds = listOf(3),
            isPinned = false,
        ),
        Note(
            id = 5,
            title = "Pancake Recipe",
            content = "Ingredients: 1 cup flour, 1 egg, 1 cup milk, 1 tbsp sugar. Mix all, fry on a lightly oiled pan until golden. Serve with honey or jam.",
            categoryIds = listOf(2),
            isPinned = false,
        ),
        Note(
            id = 6,
            title = "Personal Thought",
            content = "Taking a short walk clears my mind better than coffee sometimes.",
            categoryIds = listOf(1),
            isPinned = false,
        ),
        Note(
            id = 7,
            title = "Quick Reminder",
            content = "Buy milk and batteries on the way home.",
            categoryIds = null,
            isPinned = true,
        )
    )
    val uiState = NoteListUiState(
        selectedCategoryIds = listOf(1),
        categories = categories,
        notes = notes,
    )
    NotesTheme {
        NoteListScreen(
            uiState = uiState,
            onUndoDeleteClick = {},
            onSnackbarDismissed = {},
            onEditNoteClick = {},
            onCategoryFilterChanged = {},
            onPinToggleClick = {},
            onDeleteNoteClick = {},
        )
    }
}