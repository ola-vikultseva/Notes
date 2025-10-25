package com.example.notes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notes.presentation.editnote.EditNoteScreen
import com.example.notes.presentation.notelist.NoteListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "note_list"
    ) {
        composable(route = "note_list") {
            NoteListScreen(
                onEditNote = { noteId ->
                    if (noteId == null) {
                        navController.navigate("edit_note")
                    } else {
                        navController.navigate("edit_note?noteId=$noteId")
                    }
                }
            )
        }
        composable(
            route = "edit_note?noteId={noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")?.takeIf { it != -1 }
            EditNoteScreen(
                noteId = noteId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}