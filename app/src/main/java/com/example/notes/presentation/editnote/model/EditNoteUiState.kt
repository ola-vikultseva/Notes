package com.example.notes.presentation.editnote.model

data class EditNoteUiState(
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false
) {
    fun hasTitleOrContent(): Boolean = title.isNotBlank() || content.isNotBlank()
}