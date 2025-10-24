package com.example.notes.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.notes.domain.model.Note

@Composable
fun NoteCard(
    note: Note,
    onClick: (Offset, Note) -> Unit,
    modifier: Modifier
) {

    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    DisposableEffect(Unit) {
        Log.d("Test", "Card with id: ${note.id} entered composition")
        onDispose {
            Log.d("Test", "Card with id: ${note.id} left composition")
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { layoutCoordinates = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { localOffset ->
                        layoutCoordinates?.let {
                            val globalOffset = it.localToRoot(localOffset)
                            onClick(globalOffset, note)
                        }
                    }
                )
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
//                text = note.title,
                text = "Note #${note.id}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}