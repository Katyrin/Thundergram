package com.katyrin.thundergram.model.entities

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

sealed class FirebaseEventResponse {
    data class Success(val snapshot: DataSnapshot) : FirebaseEventResponse()
    data class Cancelled(val error: DatabaseError) : FirebaseEventResponse()
}
