package com.bashkevich.quizcheckerbackend.data.models.checkanswer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckAnswerRequest(
    @SerialName("given_answer")
    val givenAnswer: String,
    @SerialName("right_answer")
    val rightAnswer: String
)
