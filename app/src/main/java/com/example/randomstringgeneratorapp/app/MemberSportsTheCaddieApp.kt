package com.example.randomstringgeneratorapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RandomStringGeneratorApp : Application(){

    override fun onCreate() {
        super.onCreate()
    }
}