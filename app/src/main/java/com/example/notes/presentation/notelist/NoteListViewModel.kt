package com.example.notes.presentation.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NotesDataSource
import com.example.notes.presentation.notelist.model.NoteListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val dataSource: NotesDataSource,
) : ViewModel() {
    private val selectedCategoryIds = MutableStateFlow<List<Int>?>(null)
    private val pendingDeletionNoteId = MutableStateFlow<Int?>(null)
    private val isSnackbarVisible = MutableStateFlow(false)
    private val pendingDeletionMutex = Mutex()

    val uiState: StateFlow<NoteListUiState> = combine(
        selectedCategoryIds,
        dataSource.categoriesFlow,
        dataSource.notesFlow,
        pendingDeletionNoteId,
        isSnackbarVisible
    ) { selectedCategoryIds, categories, notes, pendingDeletionNoteId, isSnackbarVisible ->
        val filteredNotes = notes
            .filter { note ->
                selectedCategoryIds?.let { ids ->
                    note.categoryIds?.containsAll(ids) == true
                } ?: true
            }
            .filterNot { note ->
                pendingDeletionNoteId != null && note.id == pendingDeletionNoteId
            }
            .sortedByDescending { it.isPinned }
        NoteListUiState(
            selectedCategoryIds = selectedCategoryIds,
            categories = categories,
            notes = filteredNotes,
            isSnackbarVisible = isSnackbarVisible
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NoteListUiState()
        )

    fun onCategoryFilterChanged(categoryIds: List<Int>?) {
        setCategoryFilter(categoryIds)
    }

    fun onPinToggleClick(noteId: Int) {
        viewModelScope.launch {
            toggleNotePin(noteId)
        }
    }

    fun onDeleteNoteClick(noteId: Int) {
        viewModelScope.launch {
            pendingDeletionMutex.withLock {
                pendingDeletionNoteId.value = noteId
                isSnackbarVisible.value = true
            }
        }
    }

    fun onUndoDeleteClick() {
        isSnackbarVisible.value = false
        cancelPendingDeletion()
    }

    fun onSnackbarDismissed() {
        viewModelScope.launch {
            pendingDeletionMutex.withLock {
                isSnackbarVisible.value = false
                finalizePendingDeletion()
            }
        }
    }

    private fun setCategoryFilter(categoryIds: List<Int>?) {
        selectedCategoryIds.value = categoryIds
    }

    private suspend fun toggleNotePin(noteId: Int) {
        val note = uiState.value.notes.find { it.id == noteId } ?: return
        val updatedNote = note.copy(isPinned = !note.isPinned)
        dataSource.saveNote(updatedNote)
    }

    private fun cancelPendingDeletion() {
        pendingDeletionNoteId.value = null
    }

    private suspend fun finalizePendingDeletion() {
        val noteId = pendingDeletionNoteId.value ?: return
        dataSource.deleteNote(noteId)
        pendingDeletionNoteId.value = null
    }
}