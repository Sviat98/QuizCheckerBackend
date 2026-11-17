package com.bashkevich.quizcheckerbackend.data.models.checkanswer

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@LLMDescription("Result of checking the correctness of an answer")
data class AnswerCheckDto(
    @SerialName("probability")
    @property:LLMDescription("Probability of answer correctness from 0 to 1, where 0 is completely incorrect and 1 is absolutely right")
    val probability: Double
)
