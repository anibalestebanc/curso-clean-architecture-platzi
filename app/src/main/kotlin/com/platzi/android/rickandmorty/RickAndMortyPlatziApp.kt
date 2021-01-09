package com.platzi.android.rickandmorty

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class RickAndMortyPlatziApp: Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}
