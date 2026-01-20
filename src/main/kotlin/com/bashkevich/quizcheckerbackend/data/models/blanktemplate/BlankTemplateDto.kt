package com.bashkevich.quizcheckerbackend.data.models.blanktemplate

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@LLMDescription("A blank template for quiz containing a title, slots amount and list of slots")
data class BlankTemplateRequest(
    @SerialName("round_number")
    @property:LLMDescription("The number of a round")
    val roundNumber: String,
    @SerialName("title")
    @property:LLMDescription("The title of the blank")
    val title: String,
    @SerialName("slots_amount")
    @property:LLMDescription("The total number of slots in this blank")
    val slotsAmount: Int,
    @SerialName("slots")
    @property:LLMDescription("List of lines in the blank")
    val slots: List<SlotTemplateRequest>
)

@Serializable
@LLMDescription("A single line in a quiz blank")
data class SlotTemplateRequest(
    @SerialName("slot_number")
    @property:LLMDescription("The slot number for this line in the quiz blank")
    val slotNumber: Int,
    @SerialName("check_instructions")
    @property:LLMDescription("Instructions for the LLM to determine whether to pass an answer or not")
    val checkInstructions: String?,
    @SerialName("answer_options")
    @property:LLMDescription("List of possible answer options for this slot")
    val answerOptions: List<AnswerTemplateRequest>
)

@Serializable
@LLMDescription("An answer option")
data class AnswerTemplateRequest(
    @SerialName("question_number")
    @property:LLMDescription("The question number for this answer")
    val questionNumber: Int,
    @SerialName("answer")
    @property:LLMDescription("The answer text for the question")
    val answer: String,
    @SerialName("points")
    @property:LLMDescription("Points awarded for this particular answer")
    val points: Double
)


