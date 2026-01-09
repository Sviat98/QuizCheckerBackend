package com.bashkevich.quizcheckerbackend.plugins

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.ktor.Koog
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import io.ktor.server.application.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun Application.configureKoog() {
    install(Koog) {
        llm {
            openAI(apiKey = System.getenv("OPENAI_API_KEY") ?: "") {
                timeouts {
                    requestTimeout = 5.minutes
                    connectTimeout = 60.seconds
                    socketTimeout = 5.minutes
                }
            }

            fallback {
                provider = LLMProvider.OpenAI
                model = OpenAIModels.Chat.GPT4oMini
            }
        }
        agentConfig {
            prompt(name = "quiz-assistant") {
                system(
                    """
                    You are a helpful quiz assistant for the QuizChecker Backend.
                    You help users create, manage, and check quizzes.
                    You can answer questions about quiz topics and provide explanations.
                    Be concise and helpful in your responses.
                    """.trimIndent()
                )
            }
            registerTools {
                tools(QuizHelperTools())
            }
        }
    }
}

@LLMDescription("Tools for helping with quiz generation and management")
class QuizHelperTools : ToolSet {
    @Tool
    @LLMDescription("Get available quiz categories")
    fun getQuizCategories(): List<String> {
        return listOf(
            "Science",
            "History",
            "Geography",
            "Literature",
            "Mathematics",
            "Technology",
            "Sports",
            "Entertainment"
        )
    }

    @Tool
    @LLMDescription("Generate a sample quiz question for a given category")
    fun generateQuizQuestion(
        @LLMDescription("The category for the quiz question") category: String
    ): String {
        val questions = mapOf(
            "Science" to "What is the chemical symbol for water?",
            "History" to "In what year did World War II end?",
            "Geography" to "What is the capital of France?",
            "Literature" to "Who wrote 'Romeo and Juliet'?",
            "Mathematics" to "What is the value of Pi to two decimal places?",
            "Technology" to "What does CPU stand for?",
            "Sports" to "How many players are on a soccer team?",
            "Entertainment" to "What is the highest-grossing film of all time?"
        )
        return questions[category] ?: "What is your favorite subject?"
    }
}
