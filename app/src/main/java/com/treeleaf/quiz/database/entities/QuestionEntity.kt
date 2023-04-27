package com.treeleaf.quiz.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.treeleaf.quiz.network.model.CorrectAnswersModel
import com.treeleaf.quiz.network.model.QuestionModel
import com.treeleaf.quiz.network.model.TagModel

@Entity(tableName = "question")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") var id: Int = 0,
    @SerializedName("question") var question: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("answers") var answers: QuestionModel.AnswersModel = QuestionModel.AnswersModel(),
    @SerializedName("multiple_correct_answers") var multipleCorrectAnswers: String? = null,
    @SerializedName("correct_answers") var correctAnswers: CorrectAnswersModel = CorrectAnswersModel(),
    @SerializedName("explanation") var explanation: String? = null,
    @SerializedName("tip") var tip: String? = null,
    @SerializedName("tags") var tags: ArrayList<TagModel> = arrayListOf(),
    @SerializedName("category") var category: String? = null,
    @SerializedName("difficulty") var difficulty: String? = null,
    var usersAnsweredCorrectly: String? = null
)
