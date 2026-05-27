package com.example.giveu.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenLibraryRetrofit {
    val api: OpenLibraryApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenLibraryApiService::class.java)
    }
}