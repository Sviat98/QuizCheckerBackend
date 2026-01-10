package com.bashkevich.quizcheckerbackend.routes

import ai.koog.agents.ext.agent.reActStrategy
import ai.koog.agents.ext.agent.structuredOutputWithToolsStrategy
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.ktor.aiAgent
import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.structure.StructuredRequestConfig
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(val message: String)

@Serializable
data class ChatResponse(val response: String)

@Serializable
data class AgentRequest(val prompt: String)

@Serializable
data class AgentResponse(val output: String)

@Serializable
data class CategoryRequest(val category: String)

@Serializable
data class QuestionResponse(
    @property:LLMDescription("The generated quiz question text")
    val question: String
)

fun Route.koogRoutes() {
    route("/api/ai") {
        // Simple LLM chat endpoint
        post("/chat") {
            val request = call.receive<ChatRequest>()

            val messages = llm().execute(
                prompt("quiz-chat") {
                    system("You are a helpful quiz assistant. Answer questions concisely.")
                    user(request.message)
                },
                OpenAIModels.Chat.GPT4oMini
            )

            val responseText = messages.joinToString(separator = "") { it.content }
            call.respond(HttpStatusCode.OK, ChatResponse(response = responseText))
        }

        // Agent endpoint with ReAct strategy
        post("/agent") {
            val request = call.receive<AgentRequest>()

            val output = aiAgent<String, String>(
                strategy = reActStrategy(),
                model = OpenAIModels.Chat.GPT4oMini,
                input = request.prompt
            )

            call.respond(HttpStatusCode.OK, AgentResponse(output = output))
        }

        // Streaming chat endpoint
        post("/stream") {
            val request = call.receive<ChatRequest>()

            val flow = llm().executeStreaming(
                prompt("streaming-chat") {
                    system("You are a helpful quiz assistant. Provide detailed explanations.")
                    user(request.message)
                },
                OpenAIModels.Chat.GPT4oMini
            )

            val sb = StringBuilder()
            flow.collect { chunk -> sb.append(chunk) }
            call.respondText(sb.toString(), ContentType.Text.Plain)
        }

        // Get quiz categories using the agent
        get("/categories") {
            val output = aiAgent<String, String>(
                strategy = reActStrategy(),
                model = OpenAIModels.Chat.GPT4oMini,
                input = "List all available quiz categories"
            )

            call.respond(HttpStatusCode.OK, AgentResponse(output = output))
        }

        // Generate a quiz question for a category using the generateQuizQuestion tool
        post("/generate-question") {
            val request = call.receive<CategoryRequest>()

            val config = StructuredRequestConfig<QuestionResponse>()

            val output = aiAgent<CategoryRequest, QuestionResponse>(
                strategy = structuredOutputWithToolsStrategy(
                    config = config,
                    parallelTools = false
                ) { categoryRequest ->
                    "Use the generateQuizQuestion tool to generate a quiz question for the '${categoryRequest.category}' category."
                },
                model = OpenAIModels.Chat.GPT4oMini,
                input = request
            )

            call.respond(HttpStatusCode.OK, output)
        }
    }
}
