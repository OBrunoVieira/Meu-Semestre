package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.ViewModel
import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.SemesterRepository

class DisciplinesViewModel(
    private val disciplinesRepository: DisciplinesRepository,
    private val semesterRepository: SemesterRepository
) : ViewModel() {

    fun createDiscipline(name: String, knowledgeArea: String) {
        disciplinesRepository.createDiscipline(name, knowledgeArea) {
            semesterRepository.updateCurrentSemester(it)
        }
    }
}