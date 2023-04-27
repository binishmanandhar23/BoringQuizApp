package com.treeleaf.quiz.database.entities

import androidx.room.Entity

@Entity
data class QuestionUpdateEntity(
    val id: Int,
    val usersAnsweredCorrectly: String,
)
