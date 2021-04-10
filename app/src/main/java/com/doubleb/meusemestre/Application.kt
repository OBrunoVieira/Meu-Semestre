package com.doubleb.meusemestre

import android.app.Application
import com.doubleb.meusemestre.di.databaseModule
import com.doubleb.meusemestre.di.fragmentModule
import com.doubleb.meusemestre.di.repositoryModule
import com.doubleb.meusemestre.di.viewModelModule
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin

class Application : Application() {

    @KoinExperimentalAPI
    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)

        startKoin {
            androidContext(this@Application)
            androidLogger()
            fragmentFactory()
            modules(fragmentModule, viewModelModule, repositoryModule, databaseModule)
        }
    }
}