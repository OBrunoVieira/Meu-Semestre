package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.ViewModel
import com.doubleb.meusemestre.repository.SemesterRepository

class SemesterViewModel(private val repository: SemesterRepository) : ViewModel() {

    fun createSemester(name: String) {
        repository.createSemester(name)
    }

}