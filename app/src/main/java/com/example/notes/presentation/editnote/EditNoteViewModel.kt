package com.example.notes.presentation.editnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NotesDataSource
import com.example.notes.domain.model.Note
import com.example.notes.presentation.editnote.model.EditNoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val dataSource: NotesDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId = savedStateHandle.get<Int>(ARG_NOTE_ID)?.takeIf { it != -1 }

    private val _uiState = MutableStateFlow(EditNoteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        noteId?.let { loadNote(it) }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateContent(content: String) {
        _uiState.update { it.copy(content = content) }
    }

    fun togglePin() {
        val isPinned = _uiState.value.isPinned
        _uiState.update { it.copy(isPinned = !isPinned) }
    }

    fun saveAndExit(onSaved: () -> Unit) {
        viewModelScope.launch {
            val uiState = _uiState.value
            when {
                uiState.hasTitleOrContent() -> {
                    val note = buildNoteFromUiState(uiState)
                    dataSource.saveNote(note)
                }
                noteId != null -> {
                    dataSource.deleteNote(noteId)
                }
                else -> {}
            }
            onSaved()
        }
    }

    private fun loadNote(id: Int) {
        viewModelScope.launch {
            dataSource.getNoteById(id)?.let { note ->
                _uiState.value = EditNoteUiState(
                    title = note.title,
                    content = note.content,
                    isPinned = note.isPinned
                )
            }
        }
    }

    private fun buildNoteFromUiState(uiState: EditNoteUiState): Note =
        Note(
            id = noteId ?: generateId(),
            title = uiState.title,
            content = uiState.content,
            categoryIds = null,
            isPinned = uiState.isPinned
        )

    private fun generateId(): Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

    companion object {
        private const val ARG_NOTE_ID = "noteId"
    }
}