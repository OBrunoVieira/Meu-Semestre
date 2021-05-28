package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.repository.ExamRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ExamsViewModel(
    private val examRepository: ExamRepository,
) : ViewModel() {

    val liveData = MutableLiveData<DataSource<List<Pair<Int?, List<Exam>>>>>()
    val liveDataExamCreation = MutableLiveData<DataSource<Exam>>()

    fun getExams(disciplineId: String?) {
        liveData.value = DataSource(DataState.LOADING)

        if (disciplineId == null) {
            liveData.value = DataSource(DataState.ERROR)
            return
        }

        examRepository.getExams(
            disciplineId,
            success = {
                liveData.value = DataSource(DataState.SUCCESS, it)
            }, error = {
                liveData.value = DataSource(DataState.ERROR)
            }
        )
    }

    fun createExam(
        disciplineId: String,
        examName: String?,
        cycle: Int?,
        gradeValue: Float?,
        gradeResult: Float?,
    ) {
        viewModelScope.launch {
            val exam = async {
                examRepository.createSuspendedExam(disciplineId,
                    examName,
                    cycle,
                    gradeValue,
                    gradeResult)
            }.await()

            if (exam != null) {
                liveDataExamCreation.postValue(DataSource(DataState.SUCCESS, exam))
                return@launch
            }

            liveDataExamCreation.value = DataSource(DataState.ERROR)
        }
    }
}