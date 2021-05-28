package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.extensions.generateRandomString
import com.doubleb.meusemestre.extensions.observeRemoveValue
import com.doubleb.meusemestre.extensions.observeSetValue
import com.doubleb.meusemestre.models.Exam
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

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

    suspend fun removeSuspendedExams(disciplineId: String) = try {
        getExams(disciplineId).observeRemoveValue()
    } catch (exception: Exception) {
        null
    }

    inline fun getExams(
        disciplineId: String,
        crossinline success: (List<Pair<Int?, List<Exam>>>) -> Unit,
        crossinline error: () -> Unit,
    ) =
        getOrderedExams(disciplineId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val examList =
                        snapshot.children
                            .mapNotNull { it.getValue(Exam::class.java) }
                            .sortedBy { it.cycle }
                            .groupBy { it.cycle }
                            .toList()

                    success(examList)
                }

                override fun onCancelled(error: DatabaseError) {
                    error()
                }
            })

    fun getExam(disciplineId: String, examId: String) =
        getExams(disciplineId).child(examId)

    fun getOrderedExams(disciplineId: String) =
        getExams(disciplineId)
            .orderByChild(CHILD_ORDERING)

    private fun getExams(disciplineId: String) =
        database.child(DATABASE_EXAMS)
            .child(auth.currentUser?.uid.orEmpty())
            .child(disciplineId)
}