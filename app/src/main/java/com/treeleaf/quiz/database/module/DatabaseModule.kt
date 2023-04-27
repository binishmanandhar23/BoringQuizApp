package com.treeleaf.quiz.database.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.treeleaf.quiz.database.BoringQuizDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context): BoringQuizDatabase =
        Room.databaseBuilder(
            context = context,
            BoringQuizDatabase::class.java,
            "boring-quiz-database"
        ).build()
}