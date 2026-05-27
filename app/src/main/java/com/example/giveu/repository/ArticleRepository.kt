package com.example.giveu.repository

import com.example.giveu.api.OpenLibraryRetrofit
import com.example.giveu.api.RetrofitInstance
import com.example.giveu.model.Article

class ArticleRepository {

    // API 1: DummyJson
    suspend fun fetchArticles(category: String): List<Article> {
        val response = RetrofitInstance.api.getProductsByCategory(category)
        if (response.isSuccessful) {
            return response.body()?.products?.map {
                Article(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    imageUrl = it.thumbnail,
                    phoneNumber = "N/A",
                    location = "Online",
                    category = it.category
                )
            } ?: emptyList()
        } else {
            throw Exception("Errore DummyJson: ${response.code()}")
        }
    }

    // API 2: OpenLibrary
    suspend fun fetchBooks(subject: String): List<Article> {
        val response = OpenLibraryRetrofit.api.getBooksBySubject(subject)
        if (response.isSuccessful) {
            return response.body()?.works?.mapIndexed { index, work ->
                val imageUrl = if (work.cover_id != null)
                    "https://covers.openlibrary.org/b/id/${work.cover_id}-M.jpg"
                else ""
                Article(
                    id = index,
                    title = work.title,
                    description = work.description ?: "Nessuna descrizione",
                    imageUrl = imageUrl,
                    phoneNumber = "N/A",
                    location = "Online",
                    category = "books"
                )
            } ?: emptyList()
        } else {
            throw Exception("Errore OpenLibrary: ${response.code()}")
        }
    }
}