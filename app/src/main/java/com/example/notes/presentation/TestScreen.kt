package com.example.notes.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val PrefsDataKey = stringSetPreferencesKey("Data")

@Composable
fun TestScreen() {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val prefs = remember {
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("Prefs")
        }
    }

    var text by remember { mutableStateOf("") }
    val textList by prefs.data
        .map { it[PrefsDataKey]?.toList() ?: emptyList() }
        .collectAsState(emptyList())

    fun saveText() {
        if (text.isBlank()) return
        val keyedText = "${System.currentTimeMillis()} | $text"
        text = ""
        coroutineScope.launch {
            prefs.updateData {
                val mutablePrefs = it.toMutablePreferences()
                mutablePrefs[PrefsDataKey] = (textList + keyedText).toSet()
                mutablePrefs.toPreferences()
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { saveText() }),
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = { saveText() },
                    content = { Text("Save") }
                )
            }
            LazyColumn {
                items(textList) { keyedText ->
                    ListItem(headlineContent = {
                        Text(keyedText.split(" | ").last())
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