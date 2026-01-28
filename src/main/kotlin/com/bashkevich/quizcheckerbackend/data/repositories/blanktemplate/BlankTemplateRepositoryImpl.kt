package com.bashkevich.quizcheckerbackend.data.repositories.blanktemplate

import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.*
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll

class BlankTemplateRepositoryImpl : BlankTemplateRepository {

    override fun insertBlankTemplate(roundNumber: Int, title: String, slotsAmount: Int): Int {
        return BlankTemplateTable.insertAndGetId {
            it[BlankTemplateTable.roundNumber] = roundNumber
            it[BlankTemplateTable.title] = title
            it[BlankTemplateTable.slotsAmount] = slotsAmount
        }.value
    }

    override fun getBlankTemplateById(id: Int): BlankTemplateRequest? {
        val row = BlankTemplateTable.selectAll()
            .where { BlankTemplateTable.id eq id }
            .singleOrNull() ?: return null

        val answers = getAnswerTemplatesByBlankTemplateId(id)
        val slots = getSlotTemplatesByBlankTemplateId(id)

        return BlankTemplateRequest(
            roundNumber = row[BlankTemplateTable.roundNumber],
            title = row[BlankTemplateTable.title],
            slotsAmount = row[BlankTemplateTable.slotsAmount],
            answers = answers.map { it.toAnswerTemplateRequest() },
            slots = slots.map { slot ->
                val answerOptions = getAnswerIdsBySlotId(slot.id)
                SlotTemplateRequest(
                    slotNumber = slot.slotNumber,
                    checkInstructions = slot.checkInstructions,
                    answerOptions = answerOptions
                )
            }
        )
    }

    override fun getAllBlankTemplates(): List<BlankTemplateRequest> {
        return BlankTemplateTable.selectAll().map { row ->
            val id = row[BlankTemplateTable.id].value
            val answers = getAnswerTemplatesByBlankTemplateId(id)
            val slots = getSlotTemplatesByBlankTemplateId(id)

            BlankTemplateRequest(
                roundNumber = row[BlankTemplateTable.roundNumber],
                title = row[BlankTemplateTable.title],
                slotsAmount = row[BlankTemplateTable.slotsAmount],
                answers = answers.map { it.toAnswerTemplateRequest() },
                slots = slots.map { slot ->
                    val answerOptions = getAnswerIdsBySlotId(slot.id)
                    SlotTemplateRequest(
                        slotNumber = slot.slotNumber,
                        checkInstructions = slot.checkInstructions,
                        answerOptions = answerOptions
                    )
                }
            )
        }
    }

    override fun insertAnswerTemplate(blankTemplateId: Int, answer: String, points: Double): Int {
        return AnswerTemplateTable.insertAndGetId {
            it[blankTemplate] = blankTemplateId
            it[AnswerTemplateTable.answer] = answer
            it[AnswerTemplateTable.points] = points
        }.value
    }

    override fun getAnswerTemplatesByBlankTemplateId(blankTemplateId: Int): List<AnswerTemplateData> {
        return AnswerTemplateTable.selectAll()
            .where { AnswerTemplateTable.blankTemplate eq blankTemplateId }
            .map { it.toAnswerTemplateData() }
    }

    override fun insertSlotTemplate(blankTemplateId: Int, slotNumber: Int, checkInstructions: String?): Int {
        return SlotTemplateTable.insertAndGetId {
            it[blankTemplate] = blankTemplateId
            it[SlotTemplateTable.slotNumber] = slotNumber
            it[SlotTemplateTable.checkInstructions] = checkInstructions
        }.value
    }

    override fun getSlotTemplatesByBlankTemplateId(blankTemplateId: Int): List<SlotTemplateData> {
        return SlotTemplateTable.selectAll()
            .where { SlotTemplateTable.blankTemplate eq blankTemplateId }
            .map { it.toSlotTemplateData() }
    }

    override fun insertSlotAnswerMapping(slotId: Int, answerId: Int) {
        SlotAnswerTemplateTable.insert {
            it[slot] = slotId
            it[answer] = answerId
        }
    }

    override fun getAnswerIdsBySlotId(slotId: Int): List<Int> {
        return SlotAnswerTemplateTable.selectAll()
            .where { SlotAnswerTemplateTable.slot eq slotId }
            .map { it[SlotAnswerTemplateTable.answer].value }
    }

    private fun ResultRow.toAnswerTemplateData(): AnswerTemplateData {
        return AnswerTemplateData(
            id = this[AnswerTemplateTable.id].value,
            answer = this[AnswerTemplateTable.answer],
            points = this[AnswerTemplateTable.points]
        )
    }

    private fun ResultRow.toSlotTemplateData(): SlotTemplateData {
        return SlotTemplateData(
            id = this[SlotTemplateTable.id].value,
            slotNumber = this[SlotTemplateTable.slotNumber],
            checkInstructions = this[SlotTemplateTable.checkInstructions]
        )
    }

    private fun AnswerTemplateData.toAnswerTemplateRequest(): AnswerTemplateRequest {
        return AnswerTemplateRequest(
            id = this.id,
            answer = this.answer,
            points = this.points
        )
    }
}