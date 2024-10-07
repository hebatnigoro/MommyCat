package com.example.mommycat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT COUNT(*) FROM users WHERE username = :username AND password = :password")
    suspend fun validateUser(username: String, password: String): Int


    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun getUsernameCount(username: String): Int

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: UUID): User?

}
