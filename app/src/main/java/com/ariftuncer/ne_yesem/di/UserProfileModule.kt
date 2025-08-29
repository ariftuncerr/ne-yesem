// di/UserProfileModule.kt
package com.ariftuncer.ne_yesem.di

import com.ariftuncer.ne_yesem.data.repository.UserProfileRepositoryImpl
import com.ariftuncer.ne_yesem.domain.repository.UserProfileRepository
import com.ariftuncer.ne_yesem.domain.usecase.user.ChangePasswordUseCase
import com.ariftuncer.ne_yesem.domain.usecase.user.GetCurrentUserUseCase
import com.ariftuncer.ne_yesem.domain.usecase.user.GetUserProfileUseCase
import com.ariftuncer.ne_yesem.domain.usecase.user.UpdateUserProfileUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserProfileBindModule {
    @Binds @Singleton
    abstract fun bindUserProfileRepo(impl: UserProfileRepositoryImpl): UserProfileRepository
}

@Module
@InstallIn(SingletonComponent::class)
object UserProfileUseCaseModule {
    @Provides @Singleton
    fun provideGetUserProfile(repo: UserProfileRepository) = GetUserProfileUseCase(repo)

    @Provides @Singleton
    fun provideUpdateUserProfile(repo: UserProfileRepository) = UpdateUserProfileUseCase(repo)

    @Provides @Singleton
    fun provideGetCurrentUser(repo: UserProfileRepository) = GetCurrentUserUseCase(repo)

    @Provides @Singleton
    fun provideChangePassword(repo: UserProfileRepository) = ChangePasswordUseCase(repo)
}
