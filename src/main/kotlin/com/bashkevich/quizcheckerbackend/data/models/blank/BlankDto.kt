package com.bashkevich.quizcheckerbackend.data.models.blank

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlankDto(
    @SerialName("id")
    val id: Int,
    @SerialName("blank_template_id")
    val blankTemplateId: Int,
    @SerialName("round_number")
    val roundNumber: Int,
    @SerialName("title")
    val title: String,
    @SerialName("slots")
    val slots: List<SlotDto>
)

@Serializable
data class SlotDto(
    @SerialName("id")
    val id: Int,
    @SerialName("slot_template_id")
    val slotTemplateId: Int? = null,
    @SerialName("slot_number")
    val slotNumber: Int,
    @SerialName("answer")
    val answer: String? = null,
    @SerialName("points")
    val points: Double? = null
)
