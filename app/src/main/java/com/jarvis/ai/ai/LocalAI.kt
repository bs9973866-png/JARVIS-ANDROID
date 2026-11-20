package com.jarvis.ai.ai

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Safe offline fallback.
 *
 * This is a deterministic command assistant, not an LLM.
 * A real on-device model adapter can implement AIProvider later.
 */
class LocalAI : AIProvider {
    override val name: String = "Local command engine"

    override suspend fun isAvailable(): Boolean = true

    override suspend fun generateReply(
        message: String,
        conversation: List<ChatMessage>
    ): String {
        val command = message.trim().lowercase(Locale.ROOT)
        return when {
            command.isBlank() ->
                "Я не расслышал команду. Повторите, пожалуйста."
            "привет" in command || "здравствуй" in command ->
                "Здравствуйте. Я JARVIS. Чем могу помочь?"
            "время" in command ->
                "Сейчас " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) + "."
            "дата" in command || "число" in command ->
                "Сегодня " + SimpleDateFormat("d MMMM yyyy", Locale("ru")).format(Date()) + "."
            "как дела" in command ->
                "Основные модули прототипа готовы к работе. Облачная модель пока не подключена."
            "кто ты" in command ->
                "Я JARVIS — ваш персональный Android-помощник. Сейчас работаю в локальном демонстрационном режиме."
            "что ты умеешь" in command ->
                "Пока я умею распознавать речь, отвечать голосом, сообщать время и дату. Подключение языковой модели и управление устройством находятся в разработке."
            else ->
                "Я сохранил ваш запрос, но полноценная языковая модель ещё не подключена. Сейчас доступен локальный режим команд."
        }
    }
}
