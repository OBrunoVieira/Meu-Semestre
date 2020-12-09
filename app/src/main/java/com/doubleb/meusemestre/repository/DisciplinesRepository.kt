package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.models.Discipline
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class DisciplinesRepository(
    private val database: DatabaseReference,
    private val auth: FirebaseAuth
) {
    companion object {
        private const val DATABASE_DISCIPLINES = "disciplines"
    }

    fun createDiscipline(
        name: String,
        knowledgeArea: String,
        onComplete: (disciplineId: String) -> Unit = {}
    ) =
        database.push().key?.let { disciplineId ->
            database.child(DATABASE_DISCIPLINES)
                .child(auth.currentUser?.uid.orEmpty())
                .child(disciplineId)
                .setValue(Discipline(disciplineId, name, knowledgeArea)) { error, ref ->
                    onComplete.invoke(disciplineId)
                }
        }
}