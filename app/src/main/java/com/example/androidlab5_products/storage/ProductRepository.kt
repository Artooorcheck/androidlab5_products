package com.example.androidlab5_products.storage

import com.example.androidlab5_products.dao.ProductDao
import com.example.androidlab5_products.models.ProductModel

class ProductRepository(private val productDao: ProductDao) {
    suspend fun insertExercise(training: ProductModel) = productDao.insert(training)
    suspend fun updateExercise(training: ProductModel) = productDao.update(training)
    suspend fun deleteExercise(training: ProductModel) = productDao.delete(training)
}
