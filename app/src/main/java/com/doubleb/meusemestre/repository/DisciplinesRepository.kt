package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.extensions.generateRandomString
import com.doubleb.meusemestre.extensions.observe
import com.doubleb.meusemestre.extensions.observeRemoveValue
import com.doubleb.meusemestre.extensions.observeSetValue
import com.doubleb.meusemestre.models.Discipline
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class DisciplinesRepository(
    private val knowledgeAreasRepository: KnowledgeAreasRepository,
    private val database: DatabaseReference,
    private val auth: FirebaseAuth,
) {
    companion object {
        const val DATABASE_CHILD_AVERAGE = "average"
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
        knowledgeId: String,
        onComplete: (disciplineId: String) -> Unit = {},
    ) =
        generateRandomString().let { disciplineId ->
            getDiscipline(disciplineId)
                .setValue(
                    Discipline(disciplineId,
                        name,
                        knowledgeId,
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
        knowledgeId: String,
    ) = try {
        generateRandomString().let { disciplineId ->
            getDiscipline(disciplineId)
                .observeSetValue(
                    Discipline(
                        disciplineId,
                        name,
                        knowledgeId,
                        timestamp = System.currentTimeMillis())
                )
        }
    } catch (exception: Exception) {
        null
    }

    suspend fun updateSuspendedDiscipline(disciplineId: String, childName: String, value: Any?) =
        try {
            getDiscipline(disciplineId)
                .child(childName)
                .observeSetValue(value)
        } catch (exception: Exception) {
            null
        }

    suspend fun getChainedDisciplines(scope: CoroutineScope) = scope.run {
        val knowledgeAreas = async { knowledgeAreasRepository.getKnowledgeAreas() }.await()
        val disciplines = async { getSuspendedDisciplines() }.await()

        disciplines?.map { discipline ->
            val knowledgeArea = knowledgeAreas?.find { it.id == discipline.knowledge_id }
            discipline.copy(image = knowledgeArea?.storage)
        }
    }

    suspend fun getSuspendedDisciplines() =
        try {
            getDisciplines().observe()
                .children
                .mapNotNull { it.getValue(Discipline::class.java) }
        } catch (exception: Exception) {
            null
        }
}