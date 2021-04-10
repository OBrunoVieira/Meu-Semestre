package com.doubleb.meusemestre.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Tip(
    val title: String? = null,
    val description: String? = null,
    val storage: String? = null,
)