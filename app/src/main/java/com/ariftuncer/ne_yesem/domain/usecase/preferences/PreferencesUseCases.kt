package com.ariftuncer.ne_yesem.domain.usecase.preferences
import com.ariftuncer.ne_yesem.domain.model.Preferences
import com.ariftuncer.ne_yesem.domain.repository.PreferencesRepository

class SavePreferences(private val repo: PreferencesRepository) {
    suspend operator fun invoke(uid: String, prefs: Preferences) =
        repo.save(uid, prefs)
}
class GetPreferences(private val repo: PreferencesRepository) {
    suspend operator fun invoke(uid: String): Preferences? =
        repo.get(uid)
}
