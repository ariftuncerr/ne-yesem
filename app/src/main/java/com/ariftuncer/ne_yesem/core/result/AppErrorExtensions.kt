// core/result/AppErrorExtensions.kt
package com.ariftuncer.ne_yesem.core.result

fun AppError.readableMessage(): String = when (this) {
    is AppError.Auth -> message ?: "Kimlik doğrulama hatası"
    is AppError.Network -> message ?: "Ağ bağlantısı hatası"
    is AppError.NotFound -> "$what bulunamadı"
    is AppError.Validation -> message
    is AppError.Unknown -> throwable?.message ?: "Bilinmeyen bir hata oluştu"
}
