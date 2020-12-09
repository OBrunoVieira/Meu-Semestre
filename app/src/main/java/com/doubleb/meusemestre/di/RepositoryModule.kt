package com.doubleb.meusemestre.di

import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.SemesterRepository
import com.doubleb.meusemestre.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository(get()) }
    single { SemesterRepository(get(), get()) }
    single { DisciplinesRepository(get(), get()) }
}