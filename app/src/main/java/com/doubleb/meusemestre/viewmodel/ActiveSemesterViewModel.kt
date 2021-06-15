package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.models.ActiveSemester
import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.ExamRepository
import com.doubleb.meusemestre.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ActiveSemesterViewModel(
    private val userRepository: UserRepository,
    private val disciplinesRepository: DisciplinesRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    val liveData = MutableLiveData<DataSource<ActiveSemester>>()

    fun getActiveSemester() {
        liveData.postValue(DataSource(DataState.LOADING))

        viewModelScope.launch {
            val user = async { userRepository.getSuspendedUser() }.await()
            val disciplines = disciplinesRepository.getChainedDisciplines(this)

            val examsByDisciplines =
                disciplines
                    ?.mapNotNull { discipline ->
                        discipline.id?.let { disciplineId ->
                            val exams = async { examRepository.getSuspendedExamsById(disciplineId) }
                            discipline.id to exams.await()
                        }
                    }?.toMap()


            if (user == null && disciplines == null || user == null) {
                liveData.postValue(DataSource(DataState.ERROR))
                return@launch
            }

            liveData.postValue(
                DataSource(
                    DataState.SUCCESS,
                    ActiveSemester(user, disciplines, examsByDisciplines)
                )
            )
        }
    }

}