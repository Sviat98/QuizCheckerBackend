package com.bashkevich.quizcheckerbackend.services.blanktemplate

import com.bashkevich.quizcheckerbackend.data.DatabaseFactory.dbQuery
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.BlankTemplateRequest
import com.bashkevich.quizcheckerbackend.data.repositories.blanktemplate.BlankTemplateRepository

class BlankTemplateService(private val repository: BlankTemplateRepository) {

    suspend fun insertBlankTemplate(request: BlankTemplateRequest): Int = dbQuery {
        val blankTemplateId = repository.insertBlankTemplate(
            roundNumber = request.roundNumber,
            title = request.title,
            slotsAmount = request.slotsAmount
        )

        val answerIdMapping = mutableMapOf<Int, Int>()

        request.answers.forEach { answerRequest ->
            val newAnswerId = repository.insertAnswerTemplate(
                blankTemplateId = blankTemplateId,
                answer = answerRequest.answer,
                points = answerRequest.points
            )
            answerIdMapping[answerRequest.id] = newAnswerId
        }

        request.slots.forEach { slotRequest ->
            val slotId = repository.insertSlotTemplate(
                blankTemplateId = blankTemplateId,
                slotNumber = slotRequest.slotNumber,
                checkInstructions = slotRequest.checkInstructions
            )
            slotRequest.answerOptions.forEach { oldAnswerId ->
                val newAnswerId = answerIdMapping[oldAnswerId]
                    ?: throw IllegalStateException("Answer with id $oldAnswerId not found in mapping")
                repository.insertSlotAnswerMapping(slotId, newAnswerId)
            }
        }

        blankTemplateId
    }

    suspend fun getBlankTemplateById(id: Int): BlankTemplateRequest? = dbQuery {
        repository.getBlankTemplateById(id)
    }

    suspend fun getAllBlankTemplates(): List<BlankTemplateRequest> = dbQuery {
        repository.getAllBlankTemplates()
    }
}
