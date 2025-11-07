package com.example.notes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.presentation.editnote.EditNoteScreen
import com.example.notes.presentation.notelist.NoteListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "note_list"
    ) {
        composable(route = "note_list") {
            NoteListScreen { noteId ->
                navController.navigate("edit_note/$noteId")
            }
        }
        composable(
            route = "edit_note/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            EditNoteScreen { navController.popBackStack() }
        }
    }
}