package com.example.giveu.api

import com.example.giveu.model.OpenLibraryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenLibraryApiService {
    @GET("subjects/{subject}.json?limit=20")
    suspend fun getBooksBySubject(
        @Path("subject") subject: String
    ): Response<OpenLibraryResponse>
}