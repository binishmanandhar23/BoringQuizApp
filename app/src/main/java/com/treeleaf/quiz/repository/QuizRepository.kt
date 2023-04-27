package com.treeleaf.quiz.repository

import com.treeleaf.quiz.database.BoringQuizDatabase
import com.treeleaf.quiz.database.entities.QuestionUpdateEntity
import com.treeleaf.quiz.database.entities.UserEntity
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
class QuizRepository @Inject constructor(private val database: BoringQuizDatabase) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUserName(): String = suspendCancellableCoroutine { cont ->
        cont.resume(database.userDAO().getUser()?.userName ?: "") {
            cont.cancel(it)
        }
    }

    fun saveUserName(name: String) = database.userDAO().insertUser(UserEntity(userName = name))

    fun getUnansweredQuestions() = database.questionDAO().getAllQuestionsInFlow()

    fun getNumberOfUnansweredQuestions() = database.questionDAO().getAllQuestions()

    fun updateUsersAnswer(id: Int, answer: String) = database.questionDAO().updateQuestion(
        QuestionUpdateEntity(id = id, usersAnsweredCorrectly = answer)
    )

    fun getQuestionsByIds(ids: List<Int>) = database.questionDAO().getQuestionsByIds(ids = ids)
}