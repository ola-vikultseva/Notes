package com.example.notes.presentation.notelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NotesDataSource
import com.example.notes.domain.model.Note
import com.example.notes.presentation.notelist.model.NoteListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val dataSource: NotesDataSource
) : ViewModel() {

    private val selectedCategoryIds = MutableStateFlow<List<Int>?>(null)

    private val deletedNoteId = MutableStateFlow<Int?>(null)
    private var deletedNote: Note? = null

    val uiState: StateFlow<NoteListUiState> = combine(
        selectedCategoryIds,
        dataSource.categoriesFlow,
        dataSource.notesFlow,
        deletedNoteId
    ) { selectedCategoryIds, categories, notes, deletedNoteId ->
        val visibleNotes = notes
            .filter { note ->
                Log.d("Test", "1 operator - $note")
                selectedCategoryIds?.let { ids ->
                    note.categoryIds?.containsAll(ids) == true
                } ?: true
            }
            .filterNot { note ->
                Log.d("Test", "2 operator - $note")
                deletedNoteId != null && note.id == deletedNoteId
            }
            .sortedByDescending { it.isPinned }
        NoteListUiState(
            selectedCategoryIds = selectedCategoryIds,
            categories = categories,
            notes = visibleNotes
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NoteListUiState()
    )

    fun filterNotesByCategories(categoryIds: List<Int>?) {
        selectedCategoryIds.value = categoryIds
    }

    fun togglePin(noteId: Int) {
        viewModelScope.launch {
            val note = uiState.value.notes.find { it.id == noteId } ?: return@launch
            dataSource.saveNote(note.copy(isPinned = !note.isPinned))
        }
    }

    fun deleteNote(noteId: Int) {
        deletedNote = uiState.value.notes.find { it.id == noteId }
        deletedNoteId.value = noteId
    }

    fun undoDelete() {
        deletedNoteId.value = null
        deletedNote = null
    }

    fun confirmDeletion() {
        viewModelScope.launch {
            deletedNoteId.value?.let {
                dataSource.deleteNote(it)
                deletedNoteId.value = null
                deletedNote = null
            }
        }
    }
}