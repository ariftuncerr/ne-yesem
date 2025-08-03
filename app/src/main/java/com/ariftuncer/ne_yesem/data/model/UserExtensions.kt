package com.ariftuncer.ne_yesem.data.model
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email ?: "",
        isEmailVerified = this.isEmailVerified
    )
}
