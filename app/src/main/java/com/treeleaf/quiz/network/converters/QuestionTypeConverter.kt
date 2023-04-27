package com.treeleaf.quiz.network.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.treeleaf.quiz.database.entities.QuestionEntity
import com.treeleaf.quiz.network.model.CorrectAnswersModel
import com.treeleaf.quiz.network.model.QuestionModel
import com.treeleaf.quiz.network.model.TagModel

object QuestionTypeConverter {
    @TypeConverter
    fun answerModelToString(answersModel: QuestionModel.AnswersModel): String {
        val gson = Gson()
        val type = object : TypeToken<QuestionModel.AnswersModel>() {}.type
        return gson.toJson(answersModel, type)
    }

    @TypeConverter
    fun stringToAnswerModel(info: String): QuestionModel.AnswersModel {
        val gson = Gson()
        val type = object : TypeToken<QuestionModel.AnswersModel>() {}.type
        return gson.fromJson(info, type)
    }

    @TypeConverter
    fun correctAnswersModelToString(correctAnswersModel: CorrectAnswersModel): String {
        val gson = Gson()
        val type = object : TypeToken<CorrectAnswersModel>() {}.type
        return gson.toJson(correctAnswersModel, type)
    }

    @TypeConverter
    fun stringToCorrectAnswersModel(info: String): CorrectAnswersModel {
        val gson = Gson()
        val type = object : TypeToken<CorrectAnswersModel>() {}.type
        return gson.fromJson(info, type)
    }

    @TypeConverter
    fun listOfTagsToString(list: ArrayList<TagModel>): String {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<TagModel>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun stringToListOfTags(info: String): ArrayList<TagModel> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<TagModel>>() {}.type
        return gson.fromJson(info, type)
    }


    fun QuestionModel.toEntity() = QuestionEntity(
        id = id ?: 0,
        question = question,
        description = description,
        answers = answers,
        multipleCorrectAnswers = multipleCorrectAnswers,
        correctAnswers = correctAnswers,
        explanation = explanation,
        tip = tip,
        tags = tags,
        category = category,
        difficulty = difficulty
    )
}