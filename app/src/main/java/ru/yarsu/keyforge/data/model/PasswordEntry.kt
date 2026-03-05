package ru.yarsu.keyforge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class PasswordEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val username: String,
    val password: String,
    val url: String = "",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
