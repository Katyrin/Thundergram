package com.katyrin.thundergram.di.modules

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideUsersReference(firebaseDatabase: FirebaseDatabase): DatabaseReference =
        firebaseDatabase.getReference(USERS_REFERENCE)

    private companion object {
        const val USERS_REFERENCE = "users"
    }
}