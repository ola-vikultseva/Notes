package com.example.notes.presentation.editnote

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.saveAndExit(onSaved = onNavigationBack)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { viewModel.saveAndExit(onNavigationBack) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color.White)
        ) {
            BasicTextField(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.Gray),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (uiState.title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color.Gray)
                        )
                    }
                    innerTextField()
                })
            Spacer(Modifier.height(8.dp))
            BasicTextField(
                value = uiState.content,
                onValueChange = { viewModel.updateContent(it) },
                textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.Gray),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (uiState.content.isEmpty()) {
                        Text(
                            text = "Note",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color.Gray)
                        )
                    }
                    innerTextField()
                })
        }
    }
}