package com.example.vaccineapp

import android.app.Application
import com.example.vaccineapp.utils.httpClientModule
import com.example.vaccineapp.utils.serviceModule
import com.example.vaccineapp.utils.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
class MyApplication: Application()  {
    override fun onCreate() {
        startKoin{
            androidContext(this@MyApplication)
            modules(listOf(httpClientModule, viewModelModule, serviceModule))
        }
        super.onCreate()
    }

}