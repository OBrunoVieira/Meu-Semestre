package com.doubleb.meusemestre.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GradeComparison(val disciplineName: String, val exams: List<Exam>?) : Parcelable