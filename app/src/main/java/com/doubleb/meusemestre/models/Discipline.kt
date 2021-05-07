package com.doubleb.meusemestre.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Discipline(
    val id: String? = null,
    val name: String? = null,
    val knowledge_area: String? = null,
    val grade: Float? = null,
) : Parcelable