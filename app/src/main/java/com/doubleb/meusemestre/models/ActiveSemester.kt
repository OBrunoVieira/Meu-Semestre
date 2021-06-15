package com.doubleb.meusemestre.models

data class ActiveSemester(
    val user: User? = null,
    val disciplines: List<Discipline>? = null,
    val examsByDisciplines: Map<String, List<Exam>?>? = null
)