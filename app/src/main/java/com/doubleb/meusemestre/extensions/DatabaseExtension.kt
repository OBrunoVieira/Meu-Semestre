package com.doubleb.meusemestre.extensions

import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun Query.observe() = suspendCoroutine<DataSnapshot> { continuation ->
    addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            continuation.resume(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            continuation.resumeWithException(error.toException())
        }
    })
}

suspend fun DatabaseReference.observe() = suspendCoroutine<DataSnapshot> { continuation ->
    addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            continuation.resume(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            continuation.resumeWithException(error.toException())
        }
    })
}

suspend fun <T> DatabaseReference.observeSetValue(value: T) = suspendCoroutine<T> { continuation ->
    setValue(value) { error, _ ->
        if (error != null) {
            continuation.resumeWithException(error.toException())
        } else {
            continuation.resume(value)
        }
    }
}

suspend fun DatabaseReference.observeRemoveValue() =
    suspendCoroutine<DatabaseReference> { continuation ->
        removeValue { error, ref ->
            if (error != null) {
                continuation.resumeWithException(error.toException())
            } else {
                continuation.resume(ref)
            }
        }
    }