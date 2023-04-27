package com.treeleaf.quiz.dto

data class AnswerDTO(
    val questionId: Int,
    val answer: String,
    val correctAnswer: Boolean,
)
