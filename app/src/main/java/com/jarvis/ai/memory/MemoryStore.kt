package com.jarvis.ai.memory

import android.content.Context
import com.jarvis.ai.ai.ChatMessage
import org.json.JSONArray
import org.json.JSONObject

/**
 * Lightweight local conversation history.
 * Stored only on this device and limited to the latest 50 messages.
 */
class MemoryStore(context: Context) {
    private val preferences = context.getSharedPreferences("jarvis_memory", Context.MODE_PRIVATE)

    fun add(message: ChatMessage) {
        val items = readJson()
        items.put(JSONObject().apply {
            put("role", message.role)
            put("content", message.content)
            put("timestamp", message.timestamp)
        })
        while (items.length() > MAX_MESSAGES) {
            items.remove(0)
        }
        preferences.edit().putString(KEY_MESSAGES, items.toString()).apply()
    }

    fun getRecent(): List<ChatMessage> {
        val items = readJson()
        return buildList {
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index) ?: continue
                add(
                    ChatMessage(
                        role = item.optString("role"),
                        content = item.optString("content"),
                        timestamp = item.optLong("timestamp")
                    )
                )
            }
        }
    }

    fun clear() {
        preferences.edit().remove(KEY_MESSAGES).apply()
    }

    private fun readJson(): JSONArray {
        val raw = preferences.getString(KEY_MESSAGES, null) ?: return JSONArray()
        return runCatching { JSONArray(raw) }.getOrElse { JSONArray() }
    }

    companion object {
        private const val KEY_MESSAGES = "messages"
        private const val MAX_MESSAGES = 50
    }
}
