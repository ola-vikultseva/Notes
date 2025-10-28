package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notes.presentation.ProductListScreen
import com.example.notes.presentation.TestScreen
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                TestScreen()
//                ProductListScreen()
//                val navController = rememberNavController()
//                AppNavGraph(navController)
            }
        }
    }
}