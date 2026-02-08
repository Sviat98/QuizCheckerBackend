package com.bashkevich.quizcheckerbackend.data.repositories.blanktemplate

import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.BlankTemplateDto

interface BlankTemplateRepository {
    fun insertBlankTemplate(roundNumber: Int, title: String, slotsAmount: Int): Int
    fun getBlankTemplateById(id: Int): BlankTemplateDto?
    fun getAllBlankTemplates(): List<BlankTemplateDto>

    fun insertAnswerTemplate(blankTemplateId: Int, answer: String, points: Double): Int
    fun getAnswerTemplatesByBlankTemplateId(blankTemplateId: Int): List<AnswerTemplateData>

    fun insertSlotTemplate(blankTemplateId: Int, slotNumber: Int, checkInstructions: String?): Int
    fun getSlotTemplatesByBlankTemplateId(blankTemplateId: Int): List<SlotTemplateData>

    fun insertSlotAnswerMapping(slotId: Int, answerId: Int)
    fun getAnswerIdsBySlotId(slotId: Int): List<Int>
}

data class AnswerTemplateData(
    val id: Int,
    val answer: String,
    val points: Double
)

data class SlotTemplateData(
    val id: Int,
    val slotNumber: Int,
    val checkInstructions: String?
)