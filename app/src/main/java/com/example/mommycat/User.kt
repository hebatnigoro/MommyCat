package com.example.mommycat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class
User(
    @PrimaryKey val id: String,
    val username: String,
    val password: String,
    val phoneNumber: String
)
