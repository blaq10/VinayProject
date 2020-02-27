package com.hylton.vinayproject

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * from user_table ORDER BY first_name ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    @Update
    suspend fun updateUser(vararg user: User)

    @Delete
    suspend fun deleteUser(vararg user: User)
}