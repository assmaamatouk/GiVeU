package com.example.giveu.repository

import com.example.giveu.api.DummyJsonApiService
import com.example.giveu.api.RetrofitInstance
import com.example.giveu.model.Product

class ProductRepository {

    private val api = RetrofitInstance.api

    suspend fun fetchProducts(category: String): List<Product> {
        // Usa il metodo corretto: getProductsByCategory()
        val response = api.getProductsByCategory(category)
        if (response.isSuccessful) {
            return response.body()?.products ?: emptyList() // Restituisce la lista dei prodotti
        } else {
            throw Exception("Errore nella chiamata API")
        }
    }
}
