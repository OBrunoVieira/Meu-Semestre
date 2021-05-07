package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.models.Semester
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class SemesterRepository(private val database: DatabaseReference, private val auth: FirebaseAuth) {

    companion object {
        private const val DATABASE_SEMESTERS = "semesters"
        private const val DATABASE_DISCIPLINES = "disciplines"
    }

    fun createSemester(name: String, semesterId: String) =
        database.child(DATABASE_SEMESTERS)
            .child(auth.currentUser?.uid.orEmpty())
            .child(semesterId)
            .setValue(Semester(semesterId, name, System.currentTimeMillis()))

    fun updateCurrentSemester(disciplineId: String, semesterId: String) =
        database.child(DATABASE_SEMESTERS)
            .child(auth.currentUser?.uid.orEmpty())
            .child(semesterId)
            .child(DATABASE_DISCIPLINES)
            .setValue(disciplineId)
}