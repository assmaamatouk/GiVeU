package com.example.giveu.network

import com.example.giveu.api.DummyJsonApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://tuo-endpoint-api.com/") // <-- cambia con il tuo
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: DummyJsonApiService = retrofit.create(DummyJsonApiService::class.java)
}