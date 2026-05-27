package com.example.giveu.repository

import com.example.giveu.api.RetrofitInstance
import com.example.giveu.model.Article
import com.example.giveu.model.Product

class ArticleRepository {
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
            throw Exception("Errore nella chiamata API: ${response.code()}")
        }
    }
}