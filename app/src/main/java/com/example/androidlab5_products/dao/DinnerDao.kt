package com.example.androidlab5_products.dao

import androidx.room.*
import com.example.androidlab5_products.models.DinnerModer
import com.example.androidlab5_products.pojo.Dinner
import kotlinx.coroutines.flow.Flow

@Dao
interface DinnerDao {
    @Insert
    suspend fun insert(dinner: DinnerModer): Long

    @Update
    suspend fun update(dinner: DinnerModer)

    @Query("SELECT * FROM dinners ORDER BY date DESC")
    fun getAllDinners(): Flow<List<DinnerModer>>

    @Delete
    suspend fun delete(dinner: DinnerModer)

    @Transaction
    @Query("SELECT * FROM dinners WHERE id = :dinnerId")
    suspend fun getDinner(dinnerId: Long): Dinner
}
