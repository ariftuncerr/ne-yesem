// Kotlin
package com.ariftuncer.ne_yesem.di

import com.ariftuncer.ne_yesem.domain.repository.PreferencesRepository
import com.ariftuncer.ne_yesem.domain.usecase.preferences.GetPreferences
import com.ariftuncer.ne_yesem.domain.usecase.preferences.SavePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides @Singleton
    fun provideGetPreferences(repo: PreferencesRepository): GetPreferences =
        GetPreferences(repo)

    @Provides @Singleton
    fun provideSavePreferences(repo: PreferencesRepository): SavePreferences =
        SavePreferences(repo)
}
