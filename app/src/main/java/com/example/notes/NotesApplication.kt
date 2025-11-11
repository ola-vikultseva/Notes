package com.example.notes

import android.app.Application
import com.example.notes.data.NotesDataSource
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class NotesApplication : Application() {

    @Inject
    lateinit var notesDataSource: NotesDataSource

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            notesDataSource.setupSampleDataIfFirstRun()
        }
    }
}