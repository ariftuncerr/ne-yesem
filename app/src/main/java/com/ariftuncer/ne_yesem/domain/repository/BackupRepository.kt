package com.neyesem.domain.repository

import com.ariftuncer.ne_yesem.core.result.AppResult

data class BackupMeta(
    val path: String,          // Storage path: users/{uid}/backups/{ts}.json.gz
    val exportedAt: Long,
    val sizeBytes: Long?,
    val version: Int?,
    val hash: String?
)

interface BackupRepository {
    suspend fun createAndUploadSnapshot(): AppResult<BackupMeta>
    suspend fun listBackups(): AppResult<List<BackupMeta>>
    suspend fun restoreFrom(path: String): AppResult<Unit>
    suspend fun restoreLatest(): AppResult<Unit>
}