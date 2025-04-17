package com.example.androidlab5_products.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(
        entity = DinnerModer::class,
        parentColumns = ["id"],
        childColumns = ["dinnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var dinnerId: Long,
    var name: String,
    var kcal: Long,
    var weight: Long,
    var lastEdited: Date
)
