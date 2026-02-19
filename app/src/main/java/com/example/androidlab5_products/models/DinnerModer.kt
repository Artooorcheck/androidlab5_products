package com.example.androidlab5_products.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "dinners")
data class DinnerModer(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var date: Date,
    var location: String
)
