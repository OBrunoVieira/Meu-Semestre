package com.doubleb.meusemestre.models

data class Discipline(
    val id: String,
    val name: String,
    val knowledge_area: String,
    val grade: Float? = null
)