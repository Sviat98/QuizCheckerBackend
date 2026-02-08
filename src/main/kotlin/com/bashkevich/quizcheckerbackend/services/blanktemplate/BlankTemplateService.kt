package com.bashkevich.quizcheckerbackend.services.blanktemplate

import com.bashkevich.quizcheckerbackend.data.DatabaseFactory.dbQuery
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.*
import com.bashkevich.quizcheckerbackend.data.repositories.blanktemplate.BlankTemplateRepository

class BlankTemplateService(private val repository: BlankTemplateRepository) {

    suspend fun insertBlankTemplate(request: BlankTemplateRequest): BlankTemplateDto = dbQuery {
        val blankTemplateId = repository.insertBlankTemplate(
            roundNumber = request.roundNumber,
            title = request.title,
            slotsAmount = request.slotsAmount
        )

        val answerIdMapping = mutableMapOf<Int, Int>()

        val answerDtos = request.answers.map { answerRequest ->
            val newAnswerId = repository.insertAnswerTemplate(
                blankTemplateId = blankTemplateId,
                answer = answerRequest.answer,
                points = answerRequest.points
            )
            answerIdMapping[answerRequest.id] = newAnswerId
            AnswerTemplateDto(
                id = newAnswerId,
                answer = answerRequest.answer,
                points = answerRequest.points
            )
        }

        val slotDtos = request.slots.map { slotRequest ->
            val slotId = repository.insertSlotTemplate(
                blankTemplateId = blankTemplateId,
                slotNumber = slotRequest.slotNumber,
                checkInstructions = slotRequest.checkInstructions
            )
            val newAnswerIds = slotRequest.answerOptions.map { oldAnswerId ->
                val newAnswerId = answerIdMapping[oldAnswerId]
                    ?: throw IllegalStateException("Answer with id $oldAnswerId not found in mapping")
                repository.insertSlotAnswerMapping(slotId, newAnswerId)
                newAnswerId
            }
            val answersAmount = newAnswerIds.size
            val answer = if (answersAmount == 1) {
                answerDtos.first { it.id == newAnswerIds.first() }
            } else {
                null
            }
            SlotTemplateDto(
                id = slotId,
                slotNumber = slotRequest.slotNumber,
                checkInstructions = slotRequest.checkInstructions,
                answersAmount = answersAmount,
                answer = answer
            )
        }

        BlankTemplateDto(
            id = blankTemplateId,
            roundNumber = request.roundNumber,
            title = request.title,
            slotsAmount = request.slotsAmount,
            slots = slotDtos
        )
    }

    suspend fun getBlankTemplateById(id: Int): BlankTemplateRequest? = dbQuery {
        repository.getBlankTemplateById(id)
    }

    suspend fun getAllBlankTemplates(): List<BlankTemplateRequest> = dbQuery {
        repository.getAllBlankTemplates()
    }
}
