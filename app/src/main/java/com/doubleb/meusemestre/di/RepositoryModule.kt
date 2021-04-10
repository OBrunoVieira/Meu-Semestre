package com.doubleb.meusemestre.di

import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.SemesterRepository
import com.doubleb.meusemestre.repository.TipsRepository
import com.doubleb.meusemestre.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { UserRepository(get()) }
    single { SemesterRepository(get(), get()) }
    single { DisciplinesRepository(get(), get()) }
    single { TipsRepository(get()) }
}