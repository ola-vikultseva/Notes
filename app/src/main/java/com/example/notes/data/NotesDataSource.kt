package com.example.notes.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NotesData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NotesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val notesDataFlow = dataStore.data
        .map { prefs ->
            prefs[NOTES_JSON]?.let { json ->
                Json.decodeFromString<NotesData>(json)
            } ?: NotesData()
        }

    suspend fun getNoteById(id: Int): Note? = notesDataFlow.first().notes.find { it.id == id }

    suspend fun saveNote(note: Note) {
        modifyNotesData { notesData ->
            val isExistingNote = notesData.notes.any { it.id == note.id }
            val updatedNotesDate = if (isExistingNote) {
                notesData.notes.map { if (it.id == note.id) note else it }
            } else {
                notesData.notes + note
            }
            notesData.copy(notes = updatedNotesDate)
        }
    }

    suspend fun deleteNote(noteId: Int) {
        modifyNotesData { current ->
            current.copy(notes = current.notes.filterNot { it.id == noteId })
        }
    }

    private suspend fun modifyNotesData(
        transform: (NotesData) -> NotesData
    ) {
        dataStore.edit { prefs ->
            val current = prefs[NOTES_JSON]?.let { json ->
                Json.decodeFromString<NotesData>(json)
            } ?: NotesData()
            val updated = transform(current)
            prefs[NOTES_JSON] = Json.encodeToString(updated)
        }
    }

    companion object {
        private val NOTES_JSON = stringPreferencesKey("notes_json")
    }
}