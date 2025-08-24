package com.ariftuncer.ne_yesem.di

import com.ariftuncer.ne_yesem.data.remote.firestore.FirestoreUserRemoteDataSource
import com.ariftuncer.ne_yesem.data.remote.firestore.UserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// di/DataSourceModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds @Singleton
    abstract fun bindUserRemoteDataSource(
        impl: FirestoreUserRemoteDataSource
    ): UserRemoteDataSource
}
