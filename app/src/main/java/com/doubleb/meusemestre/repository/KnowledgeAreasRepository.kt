package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.extensions.observe
import com.doubleb.meusemestre.models.KnowledgeArea
import com.google.firebase.database.DatabaseReference

class KnowledgeAreasRepository(private val databaseReference: DatabaseReference) {

    companion object {
        private const val DATABASE_KNOWLEDGE_AREAS = "knowledge_areas"
    }

    //region immutable vars
    private val databaseKnowledgeAreas by lazy {
        databaseReference.child(DATABASE_KNOWLEDGE_AREAS).apply { keepSynced(true) }
    }
    //endregion

    suspend fun getKnowledgeAreas() = try {
        databaseKnowledgeAreas.observe()
            .children
            .mapNotNull { it.getValue(KnowledgeArea::class.java) }
    } catch (exception: Exception) {
        null
    }
}