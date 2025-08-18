// domain/repository/PreferencesRepository.kt
package com.ariftuncer.ne_yesem.domain.repository

import com.ariftuncer.ne_yesem.domain.model.Preferences

interface PreferencesRepository {
    suspend fun get(uid: String): Preferences?
    suspend fun save(uid: String, prefs: Preferences)
}
