package com.doubleb.meusemestre.models

data class Dashboard(
    val user: User? = null,
    val disciplines: List<Discipline>? = null,
    val examsByDisciplines : Map<String, List<Exam>?>? = null,
    val gradeVariationByDisciplines: Map<String, Float?>? = null,
)