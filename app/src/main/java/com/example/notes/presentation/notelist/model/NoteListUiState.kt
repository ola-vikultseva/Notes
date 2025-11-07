package com.example.notes.presentation.notelist.model

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Category

data class NoteListUiState(
    val selectedCategoryId: Int? = null,
    val categories: List<Category> = emptyList(),
    val notes: List<Note> = emptyList()
)