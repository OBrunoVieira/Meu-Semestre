package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.extensions.generateRandomString
import com.doubleb.meusemestre.extensions.observe
import com.doubleb.meusemestre.extensions.observeRemoveValue
import com.doubleb.meusemestre.extensions.observeSetValue
import com.doubleb.meusemestre.models.Discipline
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class DisciplinesRepository(
    private val database: DatabaseReference,
    private val auth: FirebaseAuth,
) {
    companion object {
        private const val DATABASE_DISCIPLINES = "disciplines"
        private const val CHILD_ORDERING = "timestamp"
    }

    fun getDisciplines() =
        database.child(DATABASE_DISCIPLINES)
            .child(auth.currentUser?.uid.orEmpty())
            .orderByChild(CHILD_ORDERING)

    fun getDiscipline(disciplineId: String) =
        database.child(DATABASE_DISCIPLINES)
            .child(auth.currentUser?.uid.orEmpty())
            .child(disciplineId)

    fun createDiscipline(
        name: String,
        knowledgeArea: String,
        onComplete: (disciplineId: String) -> Unit = {},
    ) =
        generateRandomString().let { disciplineId ->
            getDiscipline(disciplineId)
                .setValue(
                    Discipline(disciplineId,
                        name,
                        knowledgeArea,
                        timestamp = System.currentTimeMillis())
                ) { _, _ ->
                    onComplete.invoke(disciplineId)
                }
        }

    suspend fun removeSuspendedDiscipline(disciplineId: String) =
        try {
            getDiscipline(disciplineId).observeRemoveValue()
        } catch (exception: Exception) {
            null
        }

    suspend fun createSuspendedDiscipline(
        name: String,
        knowledgeArea: String,
    ) = try {
        generateRandomString().let { disciplineId ->
            getDiscipline(disciplineId)
                .observeSetValue(
                    Discipline(
                        disciplineId,
                        name,
                        knowledgeArea,
                        timestamp = System.currentTimeMillis())
                )
        }
    } catch (exception: Exception) {
        null
    }

    suspend fun getSuspendedDisciplines() =
        try {
            getDisciplines().observe()
                .children
                .map { it.getValue(Discipline::class.java) }
        } catch (exception: Exception) {
            null
        }
}