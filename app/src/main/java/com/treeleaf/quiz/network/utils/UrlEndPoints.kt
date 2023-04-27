package com.treeleaf.quiz.network.utils

import com.treeleaf.quiz.BuildConfig

object UrlEndPoints {
    const val Questions = "questions"

    fun String.withBaseUrl() = BuildConfig.BASE_URL + this
}