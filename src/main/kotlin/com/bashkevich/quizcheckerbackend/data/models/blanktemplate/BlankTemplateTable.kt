package com.bashkevich.quizcheckerbackend.data.models.blanktemplate

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object BlankTemplateTable : IntIdTable("blank_template") {
    val roundNumber = integer("round_number")
    val title = varchar("title", 255)
    val slotsAmount = integer("slots_amount")
}

object AnswerTemplateTable : IntIdTable("answer_template") {
    val blankTemplate = reference("blank_template_id", BlankTemplateTable)
    val answer = text("answer")
    val points = double("points")
}

object SlotTemplateTable : IntIdTable("slot_template") {
    val blankTemplate = reference("blank_template_id", BlankTemplateTable)
    val slotNumber = integer("slot_number")
    val checkInstructions = text("check_instructions").nullable()
}

object SlotAnswerTemplateTable : Table("slot_answer_template") {
    val slot = reference("slot_id", SlotTemplateTable)
    val answer = reference("answer_id", AnswerTemplateTable)

    override val primaryKey = PrimaryKey(slot, answer)
}
