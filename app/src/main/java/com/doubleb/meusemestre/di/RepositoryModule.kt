package com.doubleb.meusemestre.di

import com.doubleb.meusemestre.repository.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { UserRepository(get(), get()) }
    single { SemesterRepository(get(), get()) }
    single { DisciplinesRepository(get(), get()) }
    single { ExamRepository(get(), get()) }
    single { TipsRepository(get()) }
}