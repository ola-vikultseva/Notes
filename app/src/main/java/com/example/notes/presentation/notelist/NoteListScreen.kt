package com.example.notes.presentation.notelist

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notes.domain.model.Note
import com.example.notes.presentation.notelist.components.NoteCard
import com.example.notes.presentation.notelist.components.RadialActionMenu

@Composable
fun NoteListScreen(
    onEditNote: (Int?) -> Unit
) {
    val noteList = remember { mutableStateListOf<Note>() }
    val categoryList = remember { mutableStateListOf<String>() }

    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }
    var menuExpanded by remember { mutableStateOf(false) }

    fun getNotes(): List<Note> =
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

    fun getCategories(): List<String> = listOf("Work", "Personal", "Ideas")

    LaunchedEffect(Unit) {
        noteList.addAll(getNotes())
        categoryList.addAll(getCategories())
    }

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
                    onClick = { onEditNote(null) },
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
                categoryList.forEach { category ->
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text(text = category)
                    }
                }
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = noteList.size,
                    key = { index ->
                        noteList[index].id
                    }
                ) { index ->
                    NoteCard(
                        note = noteList[index],
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
        }
        RadialActionMenu(
            expanded = menuExpanded,
            position = menuPosition,
            onPinClick = { },
            onEditClick = {
                selectedNote?.let {
                    menuExpanded = false
                    selectedNote = null
                    onEditNote(it.id)
                }
            },
            onDeleteClick = {
                selectedNote?.let {
                    menuExpanded = false
                    selectedNote = null
                    noteList.remove(it)
                }
            },
            onDismiss = {
                menuExpanded = false
                selectedNote = null
            }
        )
    }
}