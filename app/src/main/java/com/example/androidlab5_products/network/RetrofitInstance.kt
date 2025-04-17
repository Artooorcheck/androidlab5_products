package com.example.androidlab5_products.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://world.openfoodfacts.org/api/v0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: OpenFoodFactsApi = retrofit.create(OpenFoodFactsApi::class.java)
}
