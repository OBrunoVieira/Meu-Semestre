package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.models.Semester
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class SemesterRepository(private val database: DatabaseReference, private val auth: FirebaseAuth) {

    companion object {
        private const val DATABASE_SEMESTERS = "semesters"
    }

    fun createSemester(name: String) {
        database.push().key?.let { semesterId ->
            database.child(DATABASE_SEMESTERS)
                .child(auth.currentUser?.uid.orEmpty())
                .child(semesterId)
                .setValue(Semester(semesterId, name, System.currentTimeMillis()))
        }
    }

    fun updateCurrentSemester(disciplineId: String) {
        database.child(DATABASE_SEMESTERS)
            .child(auth.currentUser?.uid.orEmpty())
            .child("-MNBbXlO2V4xuZqIo6cx")
            .child("disciplines")
            .push()
            .setValue(disciplineId)
    }
}