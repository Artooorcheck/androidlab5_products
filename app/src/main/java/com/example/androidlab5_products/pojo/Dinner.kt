package com.example.androidlab5_products.pojo

import com.example.androidlab5_products.models.ProductModel
import com.example.androidlab5_products.models.DinnerModer
import androidx.room.Embedded
import androidx.room.Relation

data class Dinner(
    @Embedded val dinner: DinnerModer,
    @Relation(
        parentColumn = "id",
        entityColumn = "dinnerId"
    )
    val products: List<ProductModel>
)
