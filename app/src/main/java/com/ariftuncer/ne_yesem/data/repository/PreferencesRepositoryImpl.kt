package com.ariftuncer.ne_yesem.data.repository

import com.ariftuncer.ne_yesem.domain.model.Preferences
import com.ariftuncer.ne_yesem.domain.repository.PreferencesRepository
import com.ariftuncer.ne_yesem.data.remote.preferences.PreferencesRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val remote: PreferencesRemoteDataSource
) : PreferencesRepository {
    override suspend fun get(uid: String): Preferences? = remote.load(uid)
    override suspend fun save(uid: String, prefs: Preferences) = remote.save(uid, prefs)
}
