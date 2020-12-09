package com.doubleb.meusemestre.models

class Semester(
    val id: String,
    val name: String,
    val timestamp_start: Long,
    val timestamp_end: Long? = null
)