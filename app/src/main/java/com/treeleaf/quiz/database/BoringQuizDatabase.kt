package com.treeleaf.quiz.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.treeleaf.quiz.database.dao.QuestionDAO
import com.treeleaf.quiz.database.dao.UserDAO
import com.treeleaf.quiz.database.entities.QuestionEntity
import com.treeleaf.quiz.database.entities.UserEntity
import com.treeleaf.quiz.network.converters.QuestionTypeConverter

@Database(entities = [UserEntity::class, QuestionEntity::class], version = 1)
@TypeConverters(QuestionTypeConverter::class)
abstract class BoringQuizDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    abstract fun questionDAO(): QuestionDAO
}