package com.treeleaf.quiz.network.model

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class CorrectAnswersModel(
    @SerialName("answer_a_correct") var answerACorrect: String? = null,
    @SerialName("answer_b_correct") var answerBCorrect: String? = null,
    @SerialName("answer_c_correct") var answerCCorrect: String? = null,
    @SerialName("answer_d_correct") var answerDCorrect: String? = null,
    @SerialName("answer_e_correct") var answerECorrect: String? = null,
    @SerialName("answer_f_correct") var answerFCorrect: String? = null
)
