package com.doubleb.meusemestre.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val databaseModule = module {
    factory { FirebaseDatabase.getInstance().reference }
    factory { FirebaseAuth.getInstance() }
}