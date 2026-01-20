package com.bashkevich.quizcheckerbackend.routes

import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.structure.executeStructured
import com.bashkevich.quizcheckerbackend.data.models.checkanswer.AnswerCheckDto
import com.bashkevich.quizcheckerbackend.data.models.checkanswer.CheckAnswerRequest
import io.ktor.http.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.checkAnswerRoutes() {
    route("/checkanswer") {
        post {
            val request = call.receive<CheckAnswerRequest>()

            val givenAnswer = request.givenAnswer
            val rightAnswer = request.rightAnswer

            val unorderedPrompt = "Ответ засчитывается, если написан в любом порядке."

            val orderedPrompt = "Ответ засчитывается, если написан в правильном порядке порядке."

            llm().executeStructured<AnswerCheckDto>(
                prompt("check-answer") {
                    system("""
                        You are an expert football quiz answer checker.
                        Your task is to evaluate how correct the given answer is compared to the right answer.
                        Consider partial correctness, synonyms, and minor variations in spelling or phrasing.
                        Return a probability as a number from 0 to 1, where 0 is completely incorrect and 1 is  absolutely right answer.
                    """.trimIndent())
                    user("How is $givenAnswer correct to a $rightAnswer ? $orderedPrompt")
                },
                OpenAIModels.Chat.GPT4oMini
            ).onSuccess { output ->
                call.respond(HttpStatusCode.OK, output.data)
            }.onFailure { error ->
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to check answer: ${error.message}"))
            }
        }
    }
}
