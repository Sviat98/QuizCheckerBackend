package com.bashkevich.quizcheckerbackend.data.repositories.blank

import com.bashkevich.quizcheckerbackend.data.models.blank.BlankDto
import com.bashkevich.quizcheckerbackend.data.models.blank.SlotDto

interface BlankRepository {
    fun insertBlank(blankTemplateId: Int, roundNumber: Int, title: String): Int

    fun getBlankById(id: Int): BlankDto?

    fun getAllBlanks(): List<BlankDto>

    fun getBlanksByBlankTemplateId(blankTemplateId: Int): List<BlankDto>

    fun insertSlot(blankId: Int, slotTemplateId: Int?, slotNumber: Int): Int

    fun getSlotsByBlankId(blankId: Int): List<SlotData>

    fun updateSlotAnswer(slotId: Int, answer: String?, points: Double?)

    fun getSlotById(slotId: Int): SlotData?
}

data class SlotData(
    val id: Int,
    val slotTemplateId: Int?,
    val slotNumber: Int,
    val answer: String?,
    val points: Double?
)
