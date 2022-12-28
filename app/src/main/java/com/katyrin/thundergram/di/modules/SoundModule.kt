package com.katyrin.thundergram.di.modules

import com.katyrin.thundergram.model.repository.SoundRepository
import com.katyrin.thundergram.model.repository.SoundRepositoryImpl
import com.katyrin.thundergram.model.storage.SoundStorage
import com.katyrin.thundergram.model.storage.SoundStorageImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface SoundModule {

    @Binds
    @Singleton
    fun bindSoundRepository(soundRepositoryImpl: SoundRepositoryImpl): SoundRepository

    @Binds
    @Singleton
    fun bindSoundStorage(soundStorageImpl: SoundStorageImpl): SoundStorage
}