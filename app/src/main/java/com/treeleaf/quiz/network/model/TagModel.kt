package com.treeleaf.quiz.network.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TagModel(@SerialName("name") val name: String)
