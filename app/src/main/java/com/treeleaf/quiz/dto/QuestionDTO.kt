package com.treeleaf.quiz.dto

import com.treeleaf.quiz.database.entities.QuestionEntity
import com.treeleaf.quiz.network.model.CorrectAnswersModel
import com.treeleaf.quiz.network.model.QuestionModel
import com.treeleaf.quiz.network.model.TagModel

data class QuestionDTO(
    var id: Int? = null,
    var question: String? = null,
    var description: String? = null,
    var answersDTO: List<AnswerDTO> = emptyList(),
    var multipleCorrectAnswers: String? = null,
    var explanation: String? = null,
    var tip: String? = null,
    var tags: ArrayList<TagModel> = arrayListOf(),
    var category: String? = null,
    var difficulty: String? = null,
    var usersAnswer: String? = null
)

fun QuestionEntity.toDTO() = QuestionDTO(
    id = id, question = question, description = description,
    answersDTO = answers.toAnswerDTO(questionId = id, correctAnswersModel = correctAnswers),
    multipleCorrectAnswers = multipleCorrectAnswers, explanation = explanation, tip = tip,
    tags = tags, category = category, difficulty = difficulty, usersAnswer = usersAnsweredCorrectly
)

fun QuestionModel.AnswersModel.toAnswerDTO(
    questionId: Int,
    correctAnswersModel: CorrectAnswersModel
): List<AnswerDTO> {
    val arrayList = ArrayList<AnswerDTO>()
    answerA?.let {
        if (it.isNotEmpty())
            arrayList.add(
                AnswerDTO(
                    questionId = questionId,
                    answer = it,
                    correctAnswer = correctAnswersModel.answerACorrect?.toBooleanStrictOrNull()
                        ?: false
                )
            )
    }
    answerB?.let {
        if (it.isNotEmpty())
            arrayList.add(
                AnswerDTO(
                    questionId = questionId,
                    answer = it,
                    correctAnswer = correctAnswersModel.answerBCorrect?.toBooleanStrictOrNull()
                        ?: false
                )
            )
    }
    answerC?.let {
        if (it.isNotEmpty())
            arrayList.add(
                AnswerDTO(
                    questionId = questionId,
                    answer = it,
                    correctAnswer = correctAnswersModel.answerCCorrect?.toBooleanStrictOrNull()
                        ?: false
                )
            )
    }
    answerD?.let {
        if (it.isNotEmpty())
            arrayList.add(
                AnswerDTO(
                    questionId = questionId,
                    answer = it,
                    correctAnswer = correctAnswersModel.answerDCorrect?.toBooleanStrictOrNull()
                        ?: false
                )
            )
    }
    answerE?.let {
        if (it.isNotEmpty())
            arrayList.add(
                AnswerDTO(
                    questionId = questionId,
                    answer = it,
                    correctAnswer = correctAnswersModel.answerECorrect?.toBooleanStrictOrNull()
                        ?: false
                )
            )
    }
    answerF?.let {
        if (it.isNotEmpty())
            arrayList.add(
                AnswerDTO(
                    questionId = questionId,
                    answer = it,
                    correctAnswer = correctAnswersModel.answerFCorrect?.toBooleanStrictOrNull()
                        ?: false
                )
            )
    }
    return arrayList.toList()
}


