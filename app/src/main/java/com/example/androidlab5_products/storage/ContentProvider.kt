package com.example.androidlab5_products.storage

import com.example.androidlab5_products.converter.DateConverter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidlab5_products.dao.ProductDao
import com.example.androidlab5_products.dao.DinnerDao
import com.example.androidlab5_products.models.ProductModel
import com.example.androidlab5_products.models.DinnerModer

@Database(entities = [DinnerModer::class, ProductModel::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ContentProvider : RoomDatabase() {
    abstract fun dinnerDao(): DinnerDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ContentProvider? = null

        fun getDatabase(context: Context): ContentProvider {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContentProvider::class.java,
                    "training_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
