package com.doubleb.meusemestre.di

import com.doubleb.meusemestre.ui.fragments.*
import org.koin.dsl.module

val fragmentModule = module {
    factory { ActiveSemesterFragment() }
    factory { DashboardFragment() }
    factory { DisciplineDetailsFragment() }
//    factory { RegisterAverageFragment() }
//    factory { RegisterExamsFragment() }
//    factory { RegisterInstitutionFragment() }
    factory { SemesterHistoryFragment() }
    factory { TipsFragment() }
    factory { WelcomeInfoFragment() }
}