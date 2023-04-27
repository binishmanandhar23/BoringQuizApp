package com.treeleaf.quiz.network.service

import com.treeleaf.quiz.network.model.QuestionModel
import com.treeleaf.quiz.state.NetworkResponseState

interface BoringQuizService {
    suspend fun getQuestions(limit: Int = 60): NetworkResponseState<List<QuestionModel>>
}