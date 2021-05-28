package com.doubleb.meusemestre.models

import android.os.Parcelable
import com.doubleb.meusemestre.extensions.isValid
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GraduationInfo(
    var institution_type: String? = null,
    var approval_average: Double? = null,
    var cycles: Int? = null
) : Parcelable {

    @Exclude
    fun isValid() = institution_type.isValid()
            && approval_average.isValid()
            && cycles.isValid()
}