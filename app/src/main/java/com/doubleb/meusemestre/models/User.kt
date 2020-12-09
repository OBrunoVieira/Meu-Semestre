package com.doubleb.meusemestre.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String?,
    val name: String,
    val email: String,
    val picture: String?,
    val institution_type : String,
    val approval_average: Double,
    val exams_per_semester: Int
)