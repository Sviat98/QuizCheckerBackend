package com.bashkevich.quizcheckerbackend.data.repositories.blank

import com.bashkevich.quizcheckerbackend.data.models.blank.BlankDto
import com.bashkevich.quizcheckerbackend.data.models.blank.BlankTable
import com.bashkevich.quizcheckerbackend.data.models.blank.SlotDto
import com.bashkevich.quizcheckerbackend.data.models.blank.SlotTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update

class BlankRepositoryImpl : BlankRepository {

    override fun insertBlank(blankTemplateId: Int, roundNumber: Int, title: String): Int {
        return BlankTable.insertAndGetId {
            it[BlankTable.blankTemplate] = blankTemplateId
            it[BlankTable.roundNumber] = roundNumber
            it[BlankTable.title] = title
        }.value
    }

    override fun getBlankById(id: Int): BlankDto? {
        val row = BlankTable.selectAll()
            .where { BlankTable.id eq id }
            .singleOrNull() ?: return null

        val slots = getSlotsByBlankId(id)

        return BlankDto(
            id = row[BlankTable.id].value,
            blankTemplateId = row[BlankTable.blankTemplate].value,
            roundNumber = row[BlankTable.roundNumber],
            title = row[BlankTable.title],
            slots = slots.map { it.toSlotDto() }
        )
    }

    override fun getAllBlanks(): List<BlankDto> {
        return BlankTable.selectAll().map { row ->
            val id = row[BlankTable.id].value
            val slots = getSlotsByBlankId(id)

            BlankDto(
                id = id,
                blankTemplateId = row[BlankTable.blankTemplate].value,
                roundNumber = row[BlankTable.roundNumber],
                title = row[BlankTable.title],
                slots = slots.map { it.toSlotDto() }
            )
        }
    }

    override fun getBlanksByBlankTemplateId(blankTemplateId: Int): List<BlankDto> {
        return BlankTable.selectAll()
            .where { BlankTable.blankTemplate eq blankTemplateId }
            .map { row ->
                val id = row[BlankTable.id].value
                val slots = getSlotsByBlankId(id)

                BlankDto(
                    id = id,
                    blankTemplateId = row[BlankTable.blankTemplate].value,
                    roundNumber = row[BlankTable.roundNumber],
                    title = row[BlankTable.title],
                    slots = slots.map { it.toSlotDto() }
                )
            }
    }

    override fun insertSlot(blankId: Int, slotTemplateId: Int?, slotNumber: Int): Int {
        return SlotTable.insertAndGetId {
            it[SlotTable.blank] = blankId
            it[SlotTable.slotTemplate] = slotTemplateId
            it[SlotTable.slotNumber] = slotNumber
        }.value
    }

    override fun getSlotsByBlankId(blankId: Int): List<SlotData> {
        return SlotTable.selectAll()
            .where { SlotTable.blank eq blankId }
            .map { it.toSlotData() }
    }

    override fun updateSlotAnswer(slotId: Int, answer: String?, points: Double?) {
        SlotTable.update({ SlotTable.id eq slotId }) {
            it[SlotTable.answer] = answer
            it[SlotTable.points] = points
        }
    }

    override fun getSlotById(slotId: Int): SlotData? {
        return SlotTable.selectAll()
            .where { SlotTable.id eq slotId }
            .singleOrNull()
            ?.toSlotData()
    }

    private fun ResultRow.toSlotData(): SlotData {
        return SlotData(
            id = this[SlotTable.id].value,
            slotTemplateId = this[SlotTable.slotTemplate]?.value,
            slotNumber = this[SlotTable.slotNumber],
            answer = this[SlotTable.answer],
            points = this[SlotTable.points]
        )
    }

    private fun SlotData.toSlotDto(): SlotDto {
        return SlotDto(
            id = this.id,
            slotTemplateId = this.slotTemplateId,
            slotNumber = this.slotNumber,
            answer = this.answer,
            points = this.points
        )
    }
}
