package com.katyrin.thundergram.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.katyrin.thundergram.model.entities.FirebaseEventResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun DatabaseReference.singleValueEvent(): FirebaseEventResponse =
    suspendCoroutine { continuation ->
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError): Unit =
                continuation.resume(FirebaseEventResponse.Cancelled(error))

            override fun onDataChange(snapshot: DataSnapshot): Unit =
                continuation.resume(FirebaseEventResponse.Success(snapshot))
        }
        addListenerForSingleValueEvent(valueEventListener)
    }

suspend fun DatabaseReference.valueEventFlow(): Flow<FirebaseEventResponse> = callbackFlow {
    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            trySend(FirebaseEventResponse.Success(snapshot))
        }

        override fun onCancelled(error: DatabaseError) {
            trySend(FirebaseEventResponse.Cancelled(error))
        }
    }
    addValueEventListener(valueEventListener)
    awaitClose { removeEventListener(valueEventListener) }
}