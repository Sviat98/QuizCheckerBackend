plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.bashkevich.quizcheckerbackend"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.config.yaml)

    // Serialization
    implementation(libs.ktor.serialization.kotlinx.json)

    // Koin for Dependency Injection
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    // Database
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.hikaricp)
    implementation(libs.postgresql)

    // Logging
    implementation(libs.logback.classic)

    // Koog AI Agent
    implementation(libs.koog.ktor)

    // Testing
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test)
}
gradle.taskGraph.whenReady {
    val lastTask = allTasks.lastOrNull()
    lastTask?.doLast {
        if (this.state.failure != null) return@doLast
        println("✅ TASK SUCCESSFUL. Some messages suppressed by logging.level=warn")
    }
}
