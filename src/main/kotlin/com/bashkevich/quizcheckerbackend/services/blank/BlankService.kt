package com.bashkevich.quizcheckerbackend.services.blank

import com.bashkevich.quizcheckerbackend.data.DatabaseFactory.dbQuery
import com.bashkevich.quizcheckerbackend.data.models.blank.BlankDto
import com.bashkevich.quizcheckerbackend.data.repositories.blank.BlankRepository
import com.bashkevich.quizcheckerbackend.data.repositories.blanktemplate.BlankTemplateRepository
import io.ktor.server.plugins.*

class BlankService(
    private val blankRepository: BlankRepository,
    private val blankTemplateRepository: BlankTemplateRepository
) {

    suspend fun startQuiz(): List<BlankDto> = dbQuery {
        val blankTemplates = blankTemplateRepository.getAllBlankTemplates()

        blankTemplates.map { blankTemplate ->
            val blankId = blankRepository.insertBlank(
                blankTemplateId = blankTemplate.id,
                roundNumber = blankTemplate.roundNumber,
                title = blankTemplate.title
            )

            val slotTemplates = blankTemplateRepository.getSlotTemplatesByBlankTemplateId(blankTemplate.id)

            slotTemplates.forEach { slotTemplate ->
                blankRepository.insertSlot(
                    blankId = blankId,
                    slotTemplateId = slotTemplate.id,
                    slotNumber = slotTemplate.slotNumber
                )
            }

            blankRepository.getBlankById(blankId)
                ?: throw IllegalStateException("Failed to retrieve created blank with id $blankId")
        }
    }

    suspend fun getBlankById(id: Int): BlankDto = dbQuery {
        blankRepository.getBlankById(id)
            ?: throw NotFoundException("Blank with id $id not found")
    }

    suspend fun getAllBlanks(): List<BlankDto> = dbQuery {
        blankRepository.getAllBlanks()
    }

    suspend fun getBlanksByBlankTemplateId(blankTemplateId: Int): List<BlankDto> = dbQuery {
        blankRepository.getBlanksByBlankTemplateId(blankTemplateId)
    }
}
