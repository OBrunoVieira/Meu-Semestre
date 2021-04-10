package com.doubleb.meusemestre.di

import com.doubleb.meusemestre.viewmodel.DisciplinesViewModel
import com.doubleb.meusemestre.viewmodel.LoginViewModel
import com.doubleb.meusemestre.viewmodel.SemesterViewModel
import com.doubleb.meusemestre.viewmodel.TipsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SemesterViewModel(get()) }
    viewModel { DisciplinesViewModel(get(), get()) }
    viewModel { TipsViewModel(get()) }
}