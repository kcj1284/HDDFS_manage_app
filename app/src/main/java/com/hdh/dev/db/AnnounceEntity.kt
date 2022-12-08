package com.hdh.dev.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName="announce")
data class AnnounceEntity(
    @PrimaryKey(autoGenerate=true) var annNo: Int? = null,
    @ColumnInfo(name="annTitle") val annTitle: String,
    @ColumnInfo(name="annContent") val annContent: String
): Serializable
