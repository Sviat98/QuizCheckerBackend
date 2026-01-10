package com.bashkevich.quizcheckerbackend.routes

import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.BlankTemplateRequest
import io.ktor.http.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.blankTemplateRoutes() {
    route("/ai/blank") {
        post {
            try {
                val request = call.receive<AgentRequest>()

                val messages = llm().execute(
                    prompt("blank-template-parser") {
                        system("""
                            You are a helpful assistant that parses user prompts into structured quiz blank templates.
                            The user will provide a prompt describing a blank template for quiz answers.
                            You must extract the title and answers from the prompt and return ONLY a valid JSON object.

                            The JSON structure should be:
                            {
                              "title": "string - the title of the blank template",
                              "answers": [
                                {
                                  "questionNumber": number - the question number,
                                  "answer": "string - the answer text"
                                }
                              ]
                            }

                            Return ONLY the JSON object, no additional text or explanation.
                        """.trimIndent())
                        user(request.prompt)
                    },
                    OpenAIModels.Chat.GPT4oMini
                )

                val jsonResponse = messages.joinToString(separator = "") { it.content }
                val output = Json.decodeFromString<BlankTemplateRequest>(jsonResponse)

                call.respond(HttpStatusCode.OK, output)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to process blank template: ${e.message}"))
            }
        }
    }
}
