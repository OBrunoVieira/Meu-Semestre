package com.doubleb.meusemestre.di

import com.doubleb.meusemestre.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SemesterViewModel(get(), get()) }
    viewModel { DisciplinesViewModel(get(), get(), get(), get()) }
    viewModel { TipsViewModel(get()) }
    viewModel { UserViewModel(get(), get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { ActiveSemesterViewModel(get(), get(), get()) }
    viewModel { ExamsViewModel(get(), get()) }
}