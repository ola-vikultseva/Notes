package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                NotesScreen()
            }
        }
    }
}

@Composable
fun NotesScreen() {

    val database = remember { NotesDatabase() }
    val notes by remember { mutableStateOf(database.getNotes()) }

    var selectedNoteId by remember { mutableStateOf<Int?>(null) }
    var menuPosition by remember { mutableStateOf(Offset.Zero) }
    var showMenu by remember { mutableStateOf(true) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = notes.size) { index ->
            NoteCard(
                note = notes[index],
                onLongPress = { offset, id ->
                    selectedNoteId = id
                    menuPosition = offset
                    showMenu = true
                })
        }
    }
    if (showMenu) {
        RadialMenu(
            centerPosition = menuPosition,
            onPinOptionClick = { },
            onEditOptionClick = { },
            onDeleteOptionClick = { }
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    onLongPress: (Offset, Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        onLongPress(offset, note.id)
                    })
            },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Gray,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Gray
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun RadialMenu(
    centerPosition: Offset,
    onPinOptionClick: () -> Unit,
    onEditOptionClick: () -> Unit,
    onDeleteOptionClick: () -> Unit,
) {

}

data class MenuOption(
    val id: Int,
    val name: String
)

@Preview(showBackground = true)
@Composable
fun NotesScreen_Preview() {
    NotesScreen()
}


class NotesDatabase() {
    fun getNotes(): List<Note> = listOf(
        Note(
            id = 1,
            title = "Groceries",
            description = "Milk\nBread\nEggs\nCheese\nTomatoes\nLettuce\nBananas\nChicken breast\nCoffee"
        ),
        Note(
            id = 2,
            title = "Favorite Recipes",
            description = "Chicken Curry\nRoasted Vegetables\nMashed Potatoes\nFried Rice\nTomato Soup\nTuna Salad"
        ),
        Note(
            id = 3,
            title = "New Recipes to Try",
            description = "Pumpkin Soup\nBanana Bread\nLasagna"
        ),
        Note(
            id = 4,
            title = "Weekend Ideas",
            description = "Cook a new recipe\nGo for a nature hike\nStart a DIY craft or home project\nHave a digital detox day\nTry a new coffee shop or bakery"
        ),
        Note(
            id = 5,
            title = "New Year Movie Picks",
            description = "New Year's Eve\nKlaus\nIt's a Wonderful Life\nHome Alone\nThe Grinch\nLove Actually\nThe Holiday"
        )
    )
}

data class Note(
    val id: Int,
    val title: String,
    val description: String
)