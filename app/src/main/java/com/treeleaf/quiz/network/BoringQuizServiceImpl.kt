package com.treeleaf.quiz.network

import android.util.Log
import com.treeleaf.quiz.BuildConfig
import com.treeleaf.quiz.database.BoringQuizDatabase
import com.treeleaf.quiz.network.converters.QuestionTypeConverter.toEntity
import com.treeleaf.quiz.network.model.QuestionModel
import com.treeleaf.quiz.network.service.BoringQuizService
import com.treeleaf.quiz.network.utils.UrlEndPoints
import com.treeleaf.quiz.network.utils.UrlEndPoints.withBaseUrl
import com.treeleaf.quiz.state.NetworkResponseState
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class BoringQuizServiceImpl(private val httpClient: HttpClient, private val database: BoringQuizDatabase): BoringQuizService {
    override suspend fun getQuestions(limit: Int): NetworkResponseState<List<QuestionModel>> =
        try {
            val listOfQuestionModel = httpClient.get<List<QuestionModel>>(UrlEndPoints.Questions.withBaseUrl())
            database.questionDAO().insertAllQuestions(listOfQuestions = listOfQuestionModel.map { it.toEntity() })
            NetworkResponseState.Success(data = listOfQuestionModel)
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            e.printStackTrace()
            NetworkResponseState.Error(message = e.message)
        } catch (e: ClientRequestException) {
            // 4xx - responses
            e.printStackTrace()
            NetworkResponseState.Error(message = e.message)
        } catch (e: ServerResponseException) {
            // 5xx - responses
            e.printStackTrace()
            NetworkResponseState.Error(message = e.message)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponseState.Error(message = e.message)
        }

}