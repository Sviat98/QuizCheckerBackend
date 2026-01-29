package com.bashkevich.quizcheckerbackend.routes

import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.structure.executeStructured
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.AnswerTemplateRequest
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.BlankTemplateRequest
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.SlotTemplateRequest
import io.ktor.http.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.bashkevich.quizcheckerbackend.services.blanktemplate.BlankTemplateService

fun Route.blankTemplateRoutes() {
    val blankTemplateService by inject<BlankTemplateService>()

    route("/ai/blank") {

        post {
            val request = call.receive<AgentRequest>()

            val exampleTemplate = BlankTemplateRequest(
                roundNumber = 1,
                title = "Math Quiz",
                slotsAmount = 2,
                answers = listOf(
                    AnswerTemplateRequest(id = -1, answer = "Paris", points = 1.0),
                    AnswerTemplateRequest(id = -2, answer = "Blue", points = 1.0)
                ),
                slots = listOf(
                    SlotTemplateRequest(
                        slotNumber = 1,
                        checkInstructions = null,
                        answerOptions = listOf(-1)
                    ),
                    SlotTemplateRequest(
                        slotNumber = 2,
                        checkInstructions = null,
                        answerOptions = listOf(-2)
                    )
                )
            )

            llm().executeStructured<BlankTemplateRequest>(
                prompt("blank-template-parser") {
                    system("""
                        You are a helpful assistant that parses user prompts into structured quiz blank templates.
                        Define check instructions only if it is provided.
                    """.trimIndent())
                    user(request.prompt)
                },
                OpenAIModels.Chat.GPT4oMini,
                examples = listOf(exampleTemplate)
            ).onSuccess { output ->
                val blankTemplateDto = blankTemplateService.insertBlankTemplate(output.data)
                call.respond(HttpStatusCode.OK, blankTemplateDto)
            }.onFailure { error ->
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to process blank template: ${error.message}"))
            }
        }
    }
}
