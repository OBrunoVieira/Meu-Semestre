package com.doubleb.meusemestre.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Discipline(
    val id: String? = null,
    val name: String? = null,
    val knowledge_id: String? = null,
    val average: Float? = null,
    val timestamp: Long? = null,
    @Exclude
    val image: String? = null,
) : Parcelable