package com.example.notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val categoryIds: List<Int>?,
    val isPinned: Boolean
)