package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.di.observe
import com.doubleb.meusemestre.extensions.takeIfValid
import com.doubleb.meusemestre.models.GraduationInfo
import com.doubleb.meusemestre.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class UserRepository(private val database: DatabaseReference, private val auth: FirebaseAuth) {

    companion object {
        private const val DATABASE_USERS = "users"
        private const val DATABASE_GRADUATION_INFO = "graduation_info"
        private const val DATABASE_CURRENT_SEMESTER = "current_semester"
    }

    //region immutable vars
    val databaseUser by lazy { database.child(DATABASE_USERS).apply { keepSynced(true) } }
    //endregion

    fun getUser(userId: String) =
        databaseUser.child(userId)

    suspend fun getSuspendedUser() =
        try {
            auth.currentUser?.uid.takeIfValid()?.let {
                getUser(it).observe().getValue(User::class.java)
            }
        } catch (exception: Exception) {
            null
        }

    fun createUser(user: User) =
        databaseUser
            .child(auth.currentUser?.uid.orEmpty())
            .setValue(user)

    fun updateUser(currentSemester: String) =
        databaseUser
            .child(auth.currentUser?.uid.orEmpty())
            .updateChildren(mapOf(Pair(DATABASE_CURRENT_SEMESTER, currentSemester)))

    fun createGraduationInfo(userId: String, graduationInfo: GraduationInfo) =
        databaseUser
            .child(userId)
            .child(DATABASE_GRADUATION_INFO)
            .setValue(graduationInfo)
}