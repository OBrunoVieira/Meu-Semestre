package com.doubleb.meusemestre.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val name: String, val email: String, val picture:String?)