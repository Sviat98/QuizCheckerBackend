package com.bashkevich.quizcheckerbackend.routes

import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.structure.executeStructured
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.AnswerTemplateRequest
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.BlankTemplateRequest
import io.ktor.http.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.blankTemplateRoutes() {
    route("/ai/blank") {
        post {
            val request = call.receive<AgentRequest>()

            val exampleTemplate = BlankTemplateRequest(
                title = "Math Quiz",
                answers = listOf(
                    AnswerTemplateRequest(questionNumber = 1, answer = "Paris"),
                    AnswerTemplateRequest(questionNumber = 2, answer = "Blue")
                )
            )

            llm().executeStructured<BlankTemplateRequest>(
                prompt("blank-template-parser") {
                    system("""
                        You are a helpful assistant that parses user prompts into structured quiz blank templates.
                        Extract the title and answers from the user's prompt.
                    """.trimIndent())
                    user(request.prompt)
                },
                OpenAIModels.Chat.GPT4oMini,
                examples = listOf(exampleTemplate)
            ).onSuccess { output ->
                call.respond(HttpStatusCode.OK, output.data)
            }.onFailure { error ->
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to process blank template: ${error.message}"))
            }
        }
    }
}
