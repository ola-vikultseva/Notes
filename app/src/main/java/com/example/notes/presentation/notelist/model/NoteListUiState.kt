package com.example.notes.presentation.notelist.model

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteCategory

data class NoteListUiState(
    val selectedCategoryIds: List<Int>? = null,
    val categories: List<NoteCategory> = emptyList(),
    val notes: List<Note> = emptyList(),
    val isSnackbarVisible: Boolean = false,
)