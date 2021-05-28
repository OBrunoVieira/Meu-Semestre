package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.extensions.takeIfValid
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.ExamRepository
import com.doubleb.meusemestre.repository.SemesterRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class DisciplinesViewModel(
    private val disciplinesRepository: DisciplinesRepository,
    private val examRepository: ExamRepository,
    private val semesterRepository: SemesterRepository,
) : ViewModel() {

    val liveDataDisciplineRemoval = MutableLiveData<DataSource<List<Discipline>>>()
    val liveDataDisciplineCreation = MutableLiveData<DataSource<Unit>>()
    val liveDataDiscipline = MutableLiveData<DataSource<List<Discipline>>>()

    fun createDiscipline(name: String, knowledgeArea: String, semesterId: String?) {
        liveDataDisciplineCreation.value = DataSource(DataState.LOADING)

        disciplinesRepository.createDiscipline(name, knowledgeArea) { disciplineId ->

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

    fun getDisciplines() =
        disciplinesRepository.getDisciplines()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val disciplineList =
                        snapshot.children.mapNotNull { it.getValue(Discipline::class.java) }

                    if (disciplineList.isNotEmpty()) {
                        liveDataDiscipline.value = DataSource(DataState.SUCCESS, disciplineList)
                    } else {
                        liveDataDiscipline.value = DataSource(DataState.ERROR)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    liveDataDiscipline.value = DataSource(DataState.ERROR)
                }

            })
}