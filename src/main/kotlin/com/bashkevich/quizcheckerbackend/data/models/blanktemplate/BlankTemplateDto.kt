package com.bashkevich.quizcheckerbackend.data.models.blanktemplate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlankTemplateDto(
    @SerialName("id")
    val id: Int,
    @SerialName("round_number")
    val roundNumber: Int,
    @SerialName("title")
    val title: String,
    @SerialName("slots_amount")
    val slotsAmount: Int,
    @SerialName("slots")
    val slots: List<SlotTemplateDto>
)

@Serializable
data class SlotTemplateDto(
    @SerialName("id")
    val id: Int,
    @SerialName("slot_number")
    val slotNumber: Int,
    @SerialName("check_instructions")
    val checkInstructions: String? = null,
    @SerialName("answers_amount")
    val answersAmount: Int,
    @SerialName("answer")
    val answer: AnswerTemplateDto? = null
)

@Serializable
data class AnswerTemplateDto(
    @SerialName("id")
    val id: Int,
    @SerialName("answer")
    val answer: String,
    @SerialName("points")
    val points: Double
)