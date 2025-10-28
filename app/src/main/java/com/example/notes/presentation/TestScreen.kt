package com.example.notes.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit

@Composable
fun TestScreen() {

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("Prefs", Context.MODE_PRIVATE) }

    val textList = remember {
        val prefSet = prefs.getStringSet("Data", emptySet())!!.toList()
        SnapshotStateList(prefSet.size) { prefSet[it] }
    }

    LaunchedEffect(textList) {
        Log.d("Test", "")
    }

    var text by remember { mutableStateOf("") }

    fun saveText() {
        val keyedText = "" + System.currentTimeMillis() + "|$text"
        textList.add(keyedText)
        prefs.edit {
            val prefSet = textList.toSet()
            putStringSet("Data", prefSet)
        }
        text = ""
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { saveText() }),
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { saveText() }) {
                    Text("Save")
                }
            }
            LazyColumn {
                items(textList) { keyedText ->
                    ListItem(headlineContent = {
                        Text(keyedText.split("|").last())
                    })
                }
            }
        }
    }
}

@Preview
@Composable
fun TestScreen_Preview() {
    TestScreen()
}