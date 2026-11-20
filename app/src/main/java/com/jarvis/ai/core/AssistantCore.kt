package com.jarvis.ai.core

import com.jarvis.ai.ai.AIManager
import com.jarvis.ai.ai.ChatMessage
import com.jarvis.ai.memory.MemoryStore

/**
 * Application-level coordinator between AI and local conversation memory.
 */
class AssistantCore(
    private val aiManager: AIManager,
    private val memoryStore: MemoryStore
) {
    suspend fun ask(userMessage: String): String {
        val history = memoryStore.getRecent()
        val reply = aiManager.reply(userMessage, history)
        memoryStore.add(ChatMessage(role = "user", content = userMessage))
        memoryStore.add(ChatMessage(role = "assistant", content = reply))
        return reply
    }

    fun recentConversation(): List<ChatMessage> = memoryStore.getRecent()

    fun clearConversation() = memoryStore.clear()
}
