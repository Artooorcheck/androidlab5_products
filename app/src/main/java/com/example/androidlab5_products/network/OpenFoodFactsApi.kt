package com.example.androidlab5_products.network

import com.example.androidlab5_products.models.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface OpenFoodFactsApi {
    @GET("product/{barcode}.json")
    suspend fun getProduct(
        @Path("barcode") barcode: String
    ): Response<ProductResponse>
}
