package com.jarvis.ai.ai

/**
 * Common contract for every JARVIS language model.
 * Implementations must never expose API secrets in client-side source code.
 */
interface AIProvider {
    val name: String
    suspend fun isAvailable(): Boolean
    suspend fun generateReply(message: String, conversation: List<ChatMessage>): String
}

data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class AIMode {
    HYBRID,
    CLOUD_ONLY,
    LOCAL_ONLY
}
