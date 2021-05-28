package com.doubleb.meusemestre.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exam(
    val id: String? = null,
    val discipline_id: String? = null,
    val name: String? = null,
    val cycle: Int? = null,
    val grade_value: Float? = null,
    val grade_result: Float? = null,
    val timestamp: Long? = null,
) : Parcelable