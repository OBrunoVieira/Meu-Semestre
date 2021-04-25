package com.doubleb.meusemestre.models

import android.os.Parcelable
import com.doubleb.meusemestre.extensions.isValid
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val picture: String? = null,
    val graduation_info: GraduationInfo? = null,
) : Parcelable {

    fun isValid() = id.isValid() && name.isValid() && email.isValid()
}