package com.doubleb.meusemestre.repository

import com.doubleb.meusemestre.models.Tip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TipsRepository(
    @PublishedApi
    internal val databaseReference: DatabaseReference
) {

    companion object {
        @PublishedApi
        internal const val DATABASE_TIPS = "tips"
    }

    //region immutable vars
    val databaseTips by lazy { databaseReference.child(DATABASE_TIPS).apply { keepSynced(true) } }
    //endregion

    inline fun getTips(
        crossinline success: (List<Tip>) -> Unit,
        crossinline error: () -> Unit
    ) = runBlocking(Dispatchers.IO) {

        databaseTips.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tipList = snapshot.children.mapNotNull { it.getValue(Tip::class.java) }
                success(tipList)
            }

            override fun onCancelled(error: DatabaseError) {
                error()
            }
        })
    }
}