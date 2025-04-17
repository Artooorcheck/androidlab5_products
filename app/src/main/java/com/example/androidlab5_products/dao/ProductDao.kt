package com.example.androidlab5_products.dao

import androidx.room.*
import com.example.androidlab5_products.models.ProductModel

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: ProductModel)

    @Update
    suspend fun update(product: ProductModel)

    @Query("SELECT * FROM products WHERE dinnerId = :dinnerId")
    suspend fun getProductsForDinner(dinnerId: Int): List<ProductModel>

    @Delete
    suspend fun delete(product: ProductModel)
}
