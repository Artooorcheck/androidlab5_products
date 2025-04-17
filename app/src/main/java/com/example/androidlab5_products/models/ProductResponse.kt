package com.example.androidlab5_products.models

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val status: Int,
    val product: Product?
)

data class Product(
    val product_name: String?,
    val nutriments: Nutriments?
)

data class Nutriments(
    @SerializedName("energy-kcal_100g")
    val energyKcalPer100g: Float?,

    @SerializedName("energy-kcal")
    val energyKcal: Float?,

    @SerializedName("energy-kcal_serving")
    val energyKcalSer: Float?
)