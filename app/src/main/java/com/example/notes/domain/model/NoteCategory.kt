package com.example.notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteCategory(
    val id: Int,
    val name: String
)