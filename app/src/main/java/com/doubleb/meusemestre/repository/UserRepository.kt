package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.models.User
import com.google.firebase.database.DatabaseReference

class UserRepository(private val database: DatabaseReference) {

    companion object {
        private const val DATABASE_USERS = "users"
    }

    fun createUser(user: User) {
        database.child(DATABASE_USERS)
            .child(user.id.orEmpty())
            .setValue(user)
    }

}