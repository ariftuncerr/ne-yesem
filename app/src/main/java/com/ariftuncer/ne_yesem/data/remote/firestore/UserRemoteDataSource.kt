package com.ariftuncer.ne_yesem.data.remote.firestore

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ne_yesem.domain.model.UserProfile

interface UserRemoteDataSource {
    suspend fun ensureUserDocument(profile: UserProfile): AppResult<Unit>
    suspend fun fetchUserProfile(uid: String): AppResult<UserProfile?>
}