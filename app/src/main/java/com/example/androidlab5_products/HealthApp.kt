package com.example.androidlab5_products

import android.app.Application
import com.example.androidlab5_products.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class HealthApp  : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HealthApp)
            modules(appModule)
        }
    }
}