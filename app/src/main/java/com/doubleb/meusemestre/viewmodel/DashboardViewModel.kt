package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.models.Dashboard
import com.doubleb.meusemestre.models.extensions.groupByCycle
import com.doubleb.meusemestre.models.extensions.transformToVariationList
import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.ExamRepository
import com.doubleb.meusemestre.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userRepository: UserRepository,
    private val examRepository: ExamRepository,
    private val disciplinesRepository: DisciplinesRepository,
) : ViewModel() {

    val liveData = MutableLiveData<DataSource<Dashboard>>()

    fun getDashboard() {
        liveData.postValue(DataSource(DataState.LOADING))

        viewModelScope.launch {
            val disciplines = disciplinesRepository.getChainedDisciplines(this)
            val user = async { userRepository.getSuspendedUser() }.await()

            val examsByDisciplines =
                disciplines
                    ?.mapNotNull { discipline ->
                        discipline.id?.let { disciplineId ->
                            val exams = async { examRepository.getSuspendedExamsById(disciplineId) }
                            discipline.id to exams.await()
                        }
                    }?.toMap()

            val gradeVariationByDisciplines = disciplines?.mapNotNull { discipline ->
                discipline.id?.let { disciplineId ->
                    val exams = examsByDisciplines?.getValue(disciplineId)
                    disciplineId to exams.groupByCycle()?.transformToVariationList()
                }
            }?.toMap()

            if (user == null && disciplines == null || user == null) {
                liveData.postValue(DataSource(DataState.ERROR))
                return@launch
            }

            liveData.postValue(DataSource(DataState.SUCCESS,
                Dashboard(user, disciplines, examsByDisciplines, gradeVariationByDisciplines)))
        }
    }
}