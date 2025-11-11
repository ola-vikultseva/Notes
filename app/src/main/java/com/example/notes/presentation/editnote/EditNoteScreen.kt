package com.example.notes.presentation.editnote

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.saveAndExit(onNavigationBack)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "All Notes")
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.saveAndExit(onNavigationBack) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color.Gray,
                    navigationIconContentColor = Color.Gray
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    textStyle = MaterialTheme.typography.titleLarge.copy(color = Color.Gray),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (uiState.title.isEmpty()) {
                            Text(
                                text = "Title",
                                style = MaterialTheme.typography.titleLarge.copy(color = Color.LightGray)
                            )
                        }
                        innerTextField()
                    })
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_pin_angle),
                    contentDescription = null,
                    tint = if (uiState.isPinned) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { viewModel.togglePin() }
                    )
                )
            }
            Spacer(Modifier.height(8.dp))
            BasicTextField(
                value = uiState.content,
                onValueChange = { viewModel.updateContent(it) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                decorationBox = { innerTextField ->
                    if (uiState.content.isEmpty()) {
                        Text(
                            text = "Note",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
                        )
                    }
                    innerTextField()
                })
        }
    }
}