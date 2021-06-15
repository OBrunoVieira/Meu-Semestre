package com.doubleb.meusemestre.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KnowledgeArea(
    val id: String? = null,
    val name: String? = null,
    val storage: String? = null,
) : Parcelable