package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.models.GraduationInfo
import com.doubleb.meusemestre.models.User
import com.google.firebase.database.DatabaseReference

class UserRepository(private val database: DatabaseReference) {

    companion object {
        private const val DATABASE_USERS = "users"
        private const val DATABASE_GRADUATION_INFO = "graduation_info"
    }

    //region immutable vars
    val databaseUser by lazy { database.child(DATABASE_USERS).apply { keepSynced(true) } }
    //endregion

    fun getUser(userId: String) = databaseUser.child(userId)

    fun createUser(user: User) =
        databaseUser
            .child(user.id.orEmpty())
            .setValue(user)

    fun createGraduationInfo(userId: String, graduationInfo: GraduationInfo) =
        databaseUser
            .child(userId)
            .child(DATABASE_GRADUATION_INFO)
            .setValue(graduationInfo)
}