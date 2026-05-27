package com.example.giveu.api

import com.example.giveu.model.DummyJsonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DummyJsonApiService {
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<DummyJsonResponse>
}
