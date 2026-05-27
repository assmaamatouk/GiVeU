package com.example.giveu.model

data class OpenLibraryResponse(
    val works: List<OpenLibraryWork>
)

data class OpenLibraryWork(
    val title: String,
    val description: String? = null,
    val cover_id: Int? = null,
    val key: String
)