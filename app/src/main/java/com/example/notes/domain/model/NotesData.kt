package com.example.notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotesData(
    val notes: List<Note> = emptyList(),
    val categories: List<Category> = emptyList()
)

@Serializable
data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val categoryIds: List<Int>?,
    val isPinned: Boolean
)

@Serializable
data class Category(
    val id: Int,
    val name: String
)