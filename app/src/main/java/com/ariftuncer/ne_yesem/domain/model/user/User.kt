package com.ariftuncer.ne_yesem.domain.model.user

data class User (
    val uid : String = "",
    val email : String = "",
    val isEmailVerified : Boolean = false, )