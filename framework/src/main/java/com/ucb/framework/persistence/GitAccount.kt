package com.ucb.framework.persistence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity ( tableName = "github_account")
data class GitAccount(
    @ColumnInfo(name = "alias")
    var alias: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnInfo(name = "create_at")
    var createAt: String = LocalDateTime.now().toString()
}