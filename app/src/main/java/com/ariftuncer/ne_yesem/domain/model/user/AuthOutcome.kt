package com.ariftuncer.ne_yesem.domain.model.user


/** google ve facebook için ilk girişte "sign up" olup olmadığını buradan anlarız. */
data class AuthOutcome(
    val user: AuthUser,
    val isNewUser: Boolean
)