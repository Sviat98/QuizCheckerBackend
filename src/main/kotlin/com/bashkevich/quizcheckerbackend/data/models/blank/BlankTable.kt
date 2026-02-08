package com.bashkevich.quizcheckerbackend.data.models.blank

import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.BlankTemplateTable
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.SlotTemplateTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object BlankTable : IntIdTable("blanks") {
    val blankTemplate = reference("blank_template_id", BlankTemplateTable)
    val roundNumber = integer("round_number")
    val title = varchar("title", 255)
}

object SlotTable : IntIdTable("slots") {
    val blank = reference("blank_id", BlankTable)
    val slotTemplate = reference("slot_template_id", SlotTemplateTable).nullable()
    val slotNumber = integer("slot_number")
    val answer = varchar("answer", 255).nullable()
    val points = double("points").nullable()
}
