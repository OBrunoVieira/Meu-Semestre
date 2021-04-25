package com.doubleb.meusemestre.models

import android.os.Parcelable
import com.doubleb.meusemestre.extensions.isValid
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GraduationInfo(
    var institution_type: String? = null,
    var approval_average: Double? = null,
    var exams_per_semester: Int? = null
) : Parcelable {

    @Exclude
    fun isValid() = institution_type.isValid()
            && approval_average.isValid()
            && exams_per_semester.isValid()
}