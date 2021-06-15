package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.extensions.takeIfValid
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.KnowledgeArea
import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.ExamRepository
import com.doubleb.meusemestre.repository.KnowledgeAreasRepository
import com.doubleb.meusemestre.repository.SemesterRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DisciplinesViewModel(
    private val disciplinesRepository: DisciplinesRepository,
    private val examRepository: ExamRepository,
    private val semesterRepository: SemesterRepository,
    private val knowledgeAreasRepository: KnowledgeAreasRepository,
) : ViewModel() {

    val liveDataKnowledgeAreas = MutableLiveData<DataSource<List<KnowledgeArea>>>()
    val liveDataDisciplineRemoval = MutableLiveData<DataSource<List<Discipline>>>()
    val liveDataDisciplineCreation = MutableLiveData<DataSource<Unit>>()
    val liveDataDiscipline = MutableLiveData<DataSource<List<Discipline>>>()

    fun createDiscipline(name: String, knowledgeId: String, semesterId: String?) {
        liveDataDisciplineCreation.value = DataSource(DataState.LOADING)

        disciplinesRepository.createDiscipline(name, knowledgeId) { disciplineId ->

            semesterId.takeIfValid()?.let { semesterId ->
                semesterRepository.updateCurrentSemester(disciplineId = disciplineId, semesterId)
                    .addOnFailureListener {
                        liveDataDisciplineCreation.value = DataSource(DataState.ERROR)
                    }
                    .addOnCanceledListener {
                        liveDataDisciplineCreation.value = DataSource(DataState.ERROR)
                    }
                    .addOnSuccessListener {
                        liveDataDisciplineCreation.value = DataSource(DataState.SUCCESS)
                    }

            } ?: run {
                liveDataDisciplineCreation.value = DataSource(DataState.ERROR)
            }

        }
    }

    fun getKnowledgeAreas() {
        liveDataKnowledgeAreas.postValue(DataSource(DataState.LOADING))

        viewModelScope.launch {
            val knowledgeAreas = async { knowledgeAreasRepository.getKnowledgeAreas() }.await()

            if (knowledgeAreas != null) {
                liveDataKnowledgeAreas.postValue(DataSource(DataState.SUCCESS, knowledgeAreas))
                return@launch
            }

            liveDataKnowledgeAreas.postValue(DataSource(DataState.ERROR))
        }
    }

    fun removeDiscipline(disciplineId: String) {
        viewModelScope.launch {
            val disciplineRemoval =
                async { disciplinesRepository.removeSuspendedDiscipline(disciplineId) }

            val examRemoval =
                async { examRepository.removeSuspendedExams(disciplineId) }

            val result = awaitAll(disciplineRemoval, examRemoval)
            if (result[0] != null && result[1] != null) {
                liveDataDisciplineRemoval.postValue(DataSource(DataState.SUCCESS))
                return@launch
            }

            liveDataDisciplineRemoval.postValue(DataSource(DataState.ERROR))
        }
    }

    fun getDisciplines() {
        viewModelScope.launch {
            val disciplines = disciplinesRepository.getChainedDisciplines(this)

            if (disciplines != null) {
                liveDataDiscipline.postValue(DataSource(DataState.SUCCESS, disciplines))
                return@launch
            }

            liveDataDiscipline.postValue(DataSource(DataState.ERROR))
        }
    }
}