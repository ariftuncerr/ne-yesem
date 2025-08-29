package com.ariftuncer.ne_yesem.domain.usecase.preferences

import com.ariftuncer.ne_yesem.domain.model.Preferences
import com.ariftuncer.ne_yesem.domain.repository.PreferencesRepository
import javax.inject.Inject

class GetPreferences @Inject constructor(
    private val repo: PreferencesRepository
) {
    suspend operator fun invoke(uid: String) = repo.get(uid)
}

class SavePreferences @Inject constructor(
    private val repo: PreferencesRepository
) {
    suspend operator fun invoke(uid: String, prefs: Preferences) = repo.save(uid, prefs)
}

