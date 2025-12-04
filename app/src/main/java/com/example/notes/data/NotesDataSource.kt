package com.example.notes.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class NotesDataSource @Inject constructor(
    private val appPreferences: AppPreferences,
    private val dataStore: DataStore<Preferences>
) {

    val notesFlow: Flow<List<Note>> = dataStore.data
        .map { prefs ->
            prefs[NOTES_JSON]?.let { json ->
                Json.decodeFromString<List<Note>>(json)
            } ?: emptyList()
        }

    val categoriesFlow = dataStore.data
        .map { prefs ->
            prefs[NOTE_CATEGORIES_JSON]?.let { json ->
                Json.decodeFromString<List<NoteCategory>>(json)
            } ?: emptyList()
        }

    suspend fun getNoteById(id: Int): Note? = notesFlow.first().find { it.id == id }

    suspend fun saveNote(note: Note) {
        dataStore.edit { prefs ->
            val notes = prefs[NOTES_JSON]?.let { json ->
                Json.decodeFromString<List<Note>>(json)
            } ?: emptyList()
            val isExistingNote = notes.any { it.id == note.id }
            val updatedNotes = if (isExistingNote) {
                notes.map { if (it.id == note.id) note else it }
            } else {
                notes + note
            }
            prefs[NOTES_JSON] = Json.encodeToString(updatedNotes)
        }
    }

    suspend fun deleteNote(noteId: Int) {
        dataStore.edit { prefs ->
            val notes = prefs[NOTES_JSON]!!.let { json ->
                Json.decodeFromString<List<Note>>(json)
            }
            prefs[NOTES_JSON] = Json.encodeToString(notes.filterNot { it.id == noteId })
        }
    }

    suspend fun setupSampleDataIfFirstRun() {
        if (appPreferences.isFirstLaunch()) {
            val notes = listOf(
                Note(
                    id = 1,
                    title = "1 - A Moment of Calm",
                    content = "This morning, the world was quiet. The sky was painted in soft pink and gold. I took a deep breath and felt peaceful. Sometimes, small moments like this are the most magical.",
                    categoryIds = listOf(1),
                    isPinned = false
                ),
                Note(
                    id = 2,
                    title = "2 - Unexpected Joy",
                    content = "A cat jumped onto my windowsill and stared at me. I smiled for no reason—sometimes happiness finds you in the smallest ways.",
                    categoryIds = listOf(1),
                    isPinned = false
                ),
                Note(
                    id = 3,
                    title = "3 - Cozy Winter Reads",
                    content = "The Night Circus” by Erin Morgenstern – a beautifully written fantasy full of mystery, love, and winter charm.\n“Little Women” by Louisa May Alcott – a heartwarming classic about family, dreams, and growing up.\n“The Midnight Library” by Matt Haig – perfect for reflection as the year ends — what if you could live all your possible lives?",
                    categoryIds = listOf(3),
                    isPinned = false
                ),
                Note(
                    id = 4,
                    title = "4 - Reading List",
                    content = "Next books: 'Dune' by Frank Herbert, 'Atomic Habits' by James Clear, 'Project Hail Mary' by Andy Weir.",
                    categoryIds = listOf(3),
                    isPinned = false
                ),
                Note(
                    id = 5,
                    title = "5 - Pancake Recipe",
                    content = "Ingredients: 1 cup flour, 1 egg, 1 cup milk, 1 tbsp sugar. Mix all, fry on a lightly oiled pan until golden. Serve with honey or jam.",
                    categoryIds = listOf(2),
                    isPinned = false
                ),
                Note(
                    id = 6,
                    title = "6 - Personal Thought",
                    content = "Taking a short walk clears my mind better than coffee sometimes.",
                    categoryIds = listOf(1),
                    isPinned = false
                ),
                Note(
                    id = 7,
                    title = "7 - Quick Reminder",
                    content = "Buy milk and batteries on the way home.",
                    categoryIds = null,
                    isPinned = true
                )
            )
            dataStore.edit { prefs ->
                prefs[NOTES_JSON] = Json.encodeToString(notes)
            }
            val categories = listOf(
                NoteCategory(
                    id = 1,
                    name = "Thoughts"
                ),
                NoteCategory(
                    id = 2,
                    name = "Recipes"
                ),
                NoteCategory(
                    id = 3,
                    name = "Books"
                )
            )
            dataStore.edit { prefs ->
                prefs[NOTE_CATEGORIES_JSON] = Json.encodeToString(categories)
            }
            appPreferences.setFirstLaunchCompleted()
        }
    }

    companion object {
        private val NOTES_JSON = stringPreferencesKey("notes_json")
        private val NOTE_CATEGORIES_JSON = stringPreferencesKey("note_categories_json")
    }
}