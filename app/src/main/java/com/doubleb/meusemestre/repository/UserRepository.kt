package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.models.GraduationInfo
import com.doubleb.meusemestre.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class UserRepository(private val database: DatabaseReference) {

    companion object {
        private const val DATABASE_USERS = "users"
    }

    //region immutable vars
    val databaseUser by lazy { database.child(DATABASE_USERS).apply { keepSynced(true) } }
    //endregion

    fun getUser(userId: String) = database.child(userId).addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
    )

    fun createUser(user: User) {
        databaseUser
            .child(user.id.orEmpty())
            .setValue(user)
    }

    fun createGraduationInfo(userId: String, graduationInfo: GraduationInfo) {
        databaseUser
            .child(userId)
            .setValue(graduationInfo)
    }

}