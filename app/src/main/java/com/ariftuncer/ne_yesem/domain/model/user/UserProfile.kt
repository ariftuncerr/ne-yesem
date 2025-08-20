package com.ne_yesem.domain.model

data class UserProfile(
    val email: String,
    val uid: String,
    val name: String?,
    val phoneNumber: String?
)
