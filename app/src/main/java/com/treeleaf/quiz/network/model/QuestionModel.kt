package com.treeleaf.quiz.network.model

import androidx.room.TypeConverters
import com.treeleaf.quiz.network.converters.QuestionTypeConverter
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class QuestionModel(
    @SerialName("id") var id: Int? = null,
    @SerialName("question") var question: String? = null,
    @SerialName("description") var description: String? = null,
    @TypeConverters(QuestionTypeConverter::class)
    @SerialName("answers") var answers: AnswersModel = AnswersModel(),
    @SerialName("multiple_correct_answers") var multipleCorrectAnswers: String? = null,
    @TypeConverters(QuestionTypeConverter::class)
    @SerialName("correct_answers") var correctAnswers: CorrectAnswersModel = CorrectAnswersModel(),
    @SerialName("explanation") var explanation: String? = null,
    @SerialName("tip") var tip: String? = null,
    @SerialName("tags") var tags: ArrayList<TagModel> = arrayListOf(),
    @SerialName("category") var category: String? = null,
    @SerialName("difficulty") var difficulty: String? = null
){
    @kotlinx.serialization.Serializable
    data class AnswersModel(
        @SerialName("answer_a") var answerA: String? = null,
        @SerialName("answer_b") var answerB: String? = null,
        @SerialName("answer_c") var answerC: String? = null,
        @SerialName("answer_d") var answerD: String? = null,
        @SerialName("answer_e") var answerE: String? = null,
        @SerialName("answer_f") var answerF: String? = null
    )
}

