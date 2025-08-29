package com.ariftuncer.ne_yesem.domain.model.user

data class AuthUser(
    val uid: String,
    val email: String?,
    val providerIds: List<String>
)