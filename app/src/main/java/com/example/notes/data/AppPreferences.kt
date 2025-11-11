package com.example.notes.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun isFirstLaunch(): Boolean =
        dataStore.data.map { prefs ->
            prefs[IS_FIRST_LAUNCH] ?: true
        }.first()

    suspend fun setFirstLaunchCompleted() {
        dataStore.edit { prefs ->
            prefs[IS_FIRST_LAUNCH] = false
        }
    }

    companion object {
        private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    }
}