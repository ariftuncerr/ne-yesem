package com.ariftuncer.ne_yesem.core.result

sealed interface AppError {
    data class Auth(val code: String?, val message: String?) : AppError
    data class Network(val message: String? = null) : AppError
    data class NotFound(val what: String) : AppError
    data class Validation(val message: String) : AppError
    data class Unknown(val throwable: Throwable? = null) : AppError
}