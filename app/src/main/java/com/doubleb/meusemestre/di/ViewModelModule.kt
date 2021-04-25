package com.doubleb.meusemestre.di

import com.doubleb.meusemestre.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SemesterViewModel(get()) }
    viewModel { DisciplinesViewModel(get(), get()) }
    viewModel { TipsViewModel(get()) }
    viewModel { UserViewModel(get(), get()) }
}