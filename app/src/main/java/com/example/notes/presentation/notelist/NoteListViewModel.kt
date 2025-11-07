package com.example.notes.presentation.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NotesDataSource
import com.example.notes.presentation.notelist.model.NoteListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val dataSource: NotesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeNotesData()
    }

    fun togglePin(noteId: Int) {
        viewModelScope.launch {
            val note = uiState.value.notes.find { it.id == noteId } ?: return@launch
            val updatedNote = note.copy(isPinned = !note.isPinned)
            dataSource.saveNote(updatedNote)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            dataSource.deleteNote(noteId)
        }
    }

    private fun observeNotesData() {
        viewModelScope.launch {
            dataSource.notesDataFlow.collect { notesData ->
                _uiState.value = NoteListUiState(
                    selectedCategoryId = null,
                    categories = notesData.categories,
                    notes = notesData.notes
                )
            }
        }
    }
}