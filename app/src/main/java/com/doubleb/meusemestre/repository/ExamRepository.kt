package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.extensions.generateRandomString
import com.doubleb.meusemestre.extensions.observe
import com.doubleb.meusemestre.extensions.observeRemoveValue
import com.doubleb.meusemestre.extensions.observeSetValue
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.models.extensions.groupByCycle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ExamRepository(
    @PublishedApi
    internal val database: DatabaseReference,
    @PublishedApi
    internal val auth: FirebaseAuth,
) {
    companion object {
        @PublishedApi
        internal const val DATABASE_EXAMS = "exams"

        private const val CHILD_ORDERING = "timestamp"
    }

    suspend fun createSuspendedExam(
        disciplineId: String,
        name: String?,
        cycle: Int?,
        gradeValue: Float?,
        gradeResult: Float? = null,
    ) = try {
        generateRandomString().let { examId ->
            getExam(disciplineId, examId)
                .observeSetValue(
                    Exam(examId,
                        disciplineId,
                        name,
                        cycle,
                        gradeValue,
                        gradeResult,
                        System.currentTimeMillis())
                )
        }
    } catch (exception: Exception) {
        null
    }

    suspend fun updateSuspendedExam(
        disciplineId: String,
        examId: String,
        name: String?,
        cycle: Int?,
        gradeValue: Float?,
        gradeResult: Float? = null,
    ) =
        try {
            getExam(disciplineId, examId)
                .observeSetValue(
                    Exam(examId,
                        disciplineId,
                        name,
                        cycle,
                        gradeValue,
                        gradeResult,
                        System.currentTimeMillis())
                )
        } catch (exception: Exception) {
            null
        }

    suspend fun removeSuspendedExams(disciplineId: String) = try {
        getExamsById(disciplineId).observeRemoveValue()
    } catch (exception: Exception) {
        null
    }

    suspend fun removeSuspendedExam(disciplineId: String, examId: String) = try {
        getExamsById(disciplineId)
            .child(examId).observeRemoveValue()
    } catch (exception: Exception) {
        null
    }

    suspend fun getSuspendedExamsById(disciplineId: String) = try {
        getOrderedExams(disciplineId).observe()
            .children
            .mapNotNull { it.getValue(Exam::class.java) }
    } catch (exception: Exception) {
        null
    }

    suspend fun getSuspendedGroupedExams(disciplineId: String) = try {
        getOrderedExams(disciplineId).observe()
            .children
            .mapNotNull { it.getValue(Exam::class.java) }
            .groupByCycle()
    } catch (exception: Exception) {
        null
    }

    private fun getExam(disciplineId: String, examId: String) =
        getExamsById(disciplineId).child(examId)

    private fun getOrderedExams(disciplineId: String) =
        getExamsById(disciplineId)
            .orderByChild(CHILD_ORDERING)

    private fun getExamsById(disciplineId: String) =
        getExams().child(disciplineId)

    private fun getExams() =
        database.child(DATABASE_EXAMS)
            .child(auth.currentUser?.uid.orEmpty())
}