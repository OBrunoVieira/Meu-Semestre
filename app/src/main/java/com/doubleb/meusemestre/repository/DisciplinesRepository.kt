package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.di.observe
import com.doubleb.meusemestre.extensions.generateRandomString
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
        onComplete: (disciplineId: String) -> Unit = {},
    ) =
        generateRandomString().let { disciplineId ->
            database.child(DATABASE_DISCIPLINES)
                .child(auth.currentUser?.uid.orEmpty())
                .child(disciplineId)
                .setValue(Discipline(disciplineId, name, knowledgeArea)) { error, ref ->
                    onComplete.invoke(disciplineId)
                }
        }

    fun getDisciplines() =
        database.child(DATABASE_DISCIPLINES)
            .child(auth.currentUser?.uid.orEmpty())

    fun removeDiscipline(disciplineId: String) =
        database.child(DATABASE_DISCIPLINES)
            .child(auth.currentUser?.uid.orEmpty())
            .child(disciplineId)
            .removeValue()


    suspend fun getSuspendedDisciplines() =
        try {
            getDisciplines().observe()
                .children
                .map { it.getValue(Discipline::class.java) }
        } catch (exception: Exception) {
            null
        }
}