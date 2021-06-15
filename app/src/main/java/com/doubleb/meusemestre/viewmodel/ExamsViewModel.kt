package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.models.extensions.groupByCycle
import com.doubleb.meusemestre.models.extensions.transformToAverageList
import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.DisciplinesRepository.Companion.DATABASE_CHILD_AVERAGE
import com.doubleb.meusemestre.repository.ExamRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ExamsViewModel(
    private val examRepository: ExamRepository,
    private val disciplineRepository: DisciplinesRepository,
) : ViewModel() {

    val liveData = MutableLiveData<DataSource<List<Pair<Int?, List<Exam>>>>>()
    val liveDataExamCreation = MutableLiveData<DataSource<Exam>>()
    val liveDataExamUpdate = MutableLiveData<DataSource<Exam>>()
    val liveDataExamRemoval = MutableLiveData<DataSource<Exam>>()

    fun getExams(disciplineId: String?) {
        liveData.value = DataSource(DataState.LOADING)

        if (disciplineId == null) {
            liveData.value = DataSource(DataState.ERROR)
            return
        }

        viewModelScope.launch {
            val exams = async { examRepository.getSuspendedGroupedExams(disciplineId) }.await()

            if (exams != null) {
                liveData.postValue(DataSource(DataState.SUCCESS, exams))
                return@launch
            }

            liveData.postValue(DataSource(DataState.ERROR))
        }
    }

    fun removeExam(disciplineId: String, examId: String) {
        viewModelScope.launch {
            val examList =
                async { examRepository.getSuspendedExamsById(disciplineId) }.await()
            val examRemoval =
                async { examRepository.removeSuspendedExam(disciplineId, examId) }.await()

            if (examRemoval != null) {
                val formattedExamList = examList?.filter { it.id != examId }
                val gradeResultList = formattedExamList.groupByCycle()?.transformToAverageList()

                val average =
                    if (gradeResultList.isNullOrEmpty()) null else gradeResultList.average()
                        .toFloat()

                async {
                    disciplineRepository.updateSuspendedDiscipline(
                        disciplineId,
                        DATABASE_CHILD_AVERAGE,
                        average)
                }.await()

                liveDataExamRemoval.postValue(DataSource(DataState.SUCCESS))
                return@launch
            }

            liveDataExamRemoval.postValue(DataSource(DataState.ERROR))
        }
    }

    fun createExam(
        disciplineId: String,
        examName: String?,
        cycle: Int?,
        gradeValue: Float?,
        gradeResult: Float?,
    ) {
        viewModelScope.launch {
            val examListRequest = async { examRepository.getSuspendedExamsById(disciplineId) }
            val examRequest = async {
                examRepository.createSuspendedExam(disciplineId,
                    examName,
                    cycle,
                    gradeValue,
                    gradeResult)
            }

            val result = awaitAll(examRequest, examListRequest)

            val exam = result[0] as? Exam
            val exams = (result[1] as? List<Exam?>)?.plus(exam)?.filterNotNull()

            if (exams != null && exam != null) {
                val gradeResultList = exams.groupByCycle()?.transformToAverageList()

                val average =
                    if (gradeResultList.isNullOrEmpty()) null else gradeResultList.average()
                        .toFloat()

                async {
                    disciplineRepository.updateSuspendedDiscipline(
                        disciplineId,
                        DATABASE_CHILD_AVERAGE,
                        average)
                }.await()

                liveDataExamCreation.postValue(DataSource(DataState.SUCCESS, exam))
                return@launch
            }

            liveDataExamCreation.value = DataSource(DataState.ERROR)
        }
    }

    fun updateExam(
        disciplineId: String,
        examId: String?,
        examName: String?,
        cycle: Int?,
        gradeValue: Float?,
        gradeResult: Float?,
    ) {
        viewModelScope.launch {
            if (examId == null) {
                liveDataExamUpdate.postValue(DataSource(DataState.ERROR))
                return@launch
            }

            val examListRequest = async { examRepository.getSuspendedExamsById(disciplineId) }
            val updatedExamRequest = async {
                examRepository.updateSuspendedExam(disciplineId,
                    examId,
                    examName,
                    cycle,
                    gradeValue,
                    gradeResult)
            }

            val result = awaitAll(updatedExamRequest, examListRequest)
            val exam = result[0] as? Exam
            val exams = (result[1] as? List<Exam?>)
                ?.filterNotNull()
                ?.mapNotNull {
                    if (it.id == exam?.id) {
                        exam?.copy()
                    } else {
                        it
                    }
                }

            if (exams != null && exam != null) {
                val gradeResultList = exams.groupByCycle()?.transformToAverageList()

                val average =
                    if (gradeResultList.isNullOrEmpty()) null else gradeResultList.average()
                        .toFloat()

                async {
                    disciplineRepository.updateSuspendedDiscipline(
                        disciplineId,
                        DATABASE_CHILD_AVERAGE,
                        average)
                }.await()

                liveDataExamUpdate.postValue(DataSource(DataState.SUCCESS, exam))
                return@launch
            }

            liveDataExamUpdate.postValue(DataSource(DataState.ERROR))
        }

    }
}