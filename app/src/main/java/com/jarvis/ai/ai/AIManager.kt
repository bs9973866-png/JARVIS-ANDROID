package com.jarvis.ai.ai

/**
 * Selects an available provider. Cloud provider is injected later;
 * no network calls or secret keys are embedded in this Android app yet.
 */
class AIManager(
    private val localProvider: AIProvider = LocalAI(),
    private val cloudProvider: AIProvider? = null
) {
    var mode: AIMode = AIMode.HYBRID

    suspend fun reply(message: String, history: List<ChatMessage>): String {
        val cloud = cloudProvider
        if (mode != AIMode.LOCAL_ONLY && cloud != null && cloud.isAvailable()) {
            return runCatching { cloud.generateReply(message, history) }
                .getOrElse { localProvider.generateReply(message, history) }
        }
        if (mode == AIMode.CLOUD_ONLY) {
            return "Облачный ИИ сейчас недоступен. Проверьте подключение или выберите гибридный режим."
        }
        return localProvider.generateReply(message, history)
    }

    suspend fun activeProviderName(): String {
        val cloud = cloudProvider
        return if (mode != AIMode.LOCAL_ONLY && cloud != null && cloud.isAvailable()) {
            cloud.name
        } else {
            localProvider.name
        }
    }
}
