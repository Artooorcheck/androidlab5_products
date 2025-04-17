package com.example.androidlab5_products.di

import android.app.Application
import androidx.room.Room
import com.example.androidlab5_products.ui.HealthViewModel
import com.example.androidlab5_products.storage.ContentProvider
import com.example.androidlab5_products.storage.ProductRepository
import com.example.androidlab5_products.storage.DinnerRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(get<Application>(), ContentProvider::class.java, "dinners_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<ContentProvider>().dinnerDao() }
    single { get<ContentProvider>().productDao() }

    single { DinnerRepository(get()) }
    single { ProductRepository(get()) }

    viewModel { HealthViewModel(get(), get()) }
}
