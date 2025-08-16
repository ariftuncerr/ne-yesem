package com.neyesem.data.remote.storage

import com.ariftuncer.ne_yesem.core.result.AppResult


data class RemoteFile(
    val path: String,
    val bytes: ByteArray
)

interface BackupsStorageDataSource {
    suspend fun upload(path: String, bytes: ByteArray): AppResult<Unit>
    suspend fun download(path: String, maxBytes: Long = 10L * 1024 * 1024): AppResult<RemoteFile>
    suspend fun list(prefix: String): AppResult<List<String>> // paths
}
