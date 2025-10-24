package com.example.notes.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.notes.domain.model.Note
import com.example.notes.presentation.components.NoteCard
import com.example.notes.presentation.components.RadialActionMenu

@Composable
fun NotesScreen() {

    val notes = remember { mutableStateListOf<Note>() }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }
    var menuExpanded by remember { mutableStateOf(false) }

    fun getDefaultNotes(): List<Note> =
        listOf(
            Note(
                id = 1,
                title = "Groceries",
                content = "Milk\nBread\nEggs\nCheese\nTomatoes\nLettuce\nBananas\nChicken breast\nCoffee"
            ),
            Note(
                id = 2,
                title = "Favorite Recipes",
                content = "Chicken Curry\nRoasted Vegetables\nMashed Potatoes\nFried Rice\nTomato Soup\nTuna Salad"
            ),
            Note(
                id = 3,
                title = "New Recipes to Try",
                content = "Pumpkin Soup\nBanana Bread\nLasagna"
            ),
            Note(
                id = 4,
                title = "Weekend Ideas",
                content = "Cook a new recipe\nGo for a nature hike\nStart a DIY craft or home project\nHave a digital detox day\nTry a new coffee shop or bakery"
            ),
            Note(
                id = 5,
                title = "New Year Movie Picks",
                content = "New Year's Eve\nKlaus\nIt's a Wonderful Life\nHome Alone\nThe Grinch\nLove Actually\nThe Holiday"
            )
        )

    LaunchedEffect(Unit) {
        notes.addAll(getDefaultNotes())
    }

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    end = 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp
                ),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = notes.size,
                    key = { index ->
                        notes[index].id
                    }
                ) { index ->
                    NoteCard(
                        note = notes[index],
                        onClick = { offset, note ->
                            selectedNote = note
                            menuPosition = offset
                            menuExpanded = true
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
            RadialActionMenu(
                expanded = menuExpanded,
                position = menuPosition,
                onPinClick = { },
                onEditClick = { },
                onDeleteClick = {
                    selectedNote?.let {
                        notes.remove(it)
                        menuExpanded = false
                        selectedNote = null
                    }
                },
                onDismiss = {
                    menuExpanded = false
                    selectedNote = null
                }
            )
        }
    }
}