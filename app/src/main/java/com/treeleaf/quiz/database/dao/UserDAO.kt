package com.treeleaf.quiz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treeleaf.quiz.database.entities.UserEntity

@Dao
interface UserDAO {
    @Query("SELECT * FROM user WHERE id=1")
    fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)
}