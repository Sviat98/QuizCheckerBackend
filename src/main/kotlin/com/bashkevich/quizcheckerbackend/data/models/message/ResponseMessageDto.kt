package com.bashkevich.quizcheckerbackend.data.models.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessageDto(
    @SerialName(value = "message")
    val message: String
)