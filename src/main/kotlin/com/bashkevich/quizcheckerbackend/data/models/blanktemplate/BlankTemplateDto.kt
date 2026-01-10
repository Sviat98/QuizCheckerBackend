package com.bashkevich.quizcheckerbackend.data.models.blanktemplate

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.Serializable

@Serializable
data class AnswerTemplateRequest(
    @property:LLMDescription("The question number for this answer")
    val questionNumber: Int,
    @property:LLMDescription("The answer text for the question")
    val answer: String
)

@Serializable
@LLMDescription("A blank template for quiz answers containing a title and list of answers")
data class BlankTemplateRequest(
    @property:LLMDescription("The title of the blank template")
    val title: String,
    @property:LLMDescription("List of answers for each question in the template")
    val answers: List<AnswerTemplateRequest>
)
