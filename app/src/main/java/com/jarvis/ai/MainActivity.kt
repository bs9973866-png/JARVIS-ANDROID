package com.jarvis.ai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private lateinit var status: TextView
    private lateinit var transcript: TextView
    private lateinit var tts: TextToSpeech
    private val cyan = Color.rgb(82, 224, 245)
    private val background = Color.rgb(7, 10, 18)
    private val panel = Color.rgb(17, 25, 39)

    companion object {
        private const val REQUEST_AUDIO = 41
        private const val REQUEST_SPEECH = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = background
        window.navigationBarColor = background
        tts = TextToSpeech(this, this)
        buildScreen()
    }

    private fun buildScreen() {
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(background)
        }
        val brand = TextView(this).apply {
            text = "J.A.R.V.I.S."
            textSize = 27f
            letterSpacing = 0.22f
            setTextColor(cyan)
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            gravity = Gravity.CENTER
        }
        page.addView(brand, matchWrap())

        val subtitle = TextView(this).apply {
            text = "PERSONAL AI SYSTEM  •  PROTOTYPE 0.1"
            textSize = 10f
            letterSpacing = 0.12f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 34)
        }
        page.addView(subtitle, matchWrap())

        val core = TextView(this).apply {
            text = "◉"
            textSize = 94f
            setTextColor(cyan)
            gravity = Gravity.CENTER
        }
        page.addView(core, LinearLayout.LayoutParams(-1, 190))

        status = TextView(this).apply {
            text = "SYSTEM STANDBY"
            textSize = 13f
            letterSpacing = 0.15f
            setTextColor(cyan)
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 22)
        }
        page.addView(status, matchWrap())

        transcript = TextView(this).apply {
            text = "Здравствуйте. Я JARVIS.\nНажмите кнопку и произнесите команду."
            textSize = 15f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(18, 22, 18, 22)
            setBackgroundColor(panel)
        }
        page.addView(transcript, LinearLayout.LayoutParams(-1, 0, 1f))

        val listen = Button(this).apply {
            text = "🎙  ПОЗВАТЬ ДЖАРВИСА"
            textSize = 14f
            isAllCaps = false
            setTextColor(Color.BLACK)
            setBackgroundColor(cyan)
            setOnClickListener { requestVoiceInput() }
        }
        page.addView(listen, LinearLayout.LayoutParams(-1, 58).apply {
            topMargin = 22
        })

        val note = TextView(this).apply {
            text = "Голосовой прототип • Для распознавания может понадобиться интернет"
            textSize = 10f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, 14, 0, 0)
        }
        page.addView(note, matchWrap())
        setContentView(page)
    }

    private fun matchWrap() = LinearLayout.LayoutParams(-1, -2)

    private fun requestVoiceInput() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO)
            return
        }
        launchRecognizer()
    }

    private fun launchRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            status.text = "SPEECH SERVICE UNAVAILABLE"
            transcript.text = "На устройстве не найден сервис распознавания речи."
            return
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Слушаю команду JARVIS")
        }
        status.text = "LISTENING"
        try {
            startActivityForResult(intent, REQUEST_SPEECH)
        } catch (_: Exception) {
            status.text = "VOICE INPUT ERROR"
            transcript.text = "Не удалось открыть распознавание речи."
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_AUDIO &&
            grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
        ) {
            launchRecognizer()
        } else if (requestCode == REQUEST_AUDIO) {
            status.text = "MICROPHONE PERMISSION DENIED"
        }
    }

    @Deprecated("Uses activity result API for broad Android compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_SPEECH) return
        if (resultCode == RESULT_OK) {
            val spoken = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull().orEmpty()
            transcript.text = "ВЫ: " + spoken
            processCommand(spoken)
        } else {
            status.text = "SYSTEM STANDBY"
            transcript.text = "Не удалось распознать команду. Попробуйте ещё раз."
        }
    }

    private fun processCommand(raw: String) {
        val command = raw.lowercase(Locale.ROOT)
        val answer = when {
            "привет" in command || "здравствуй" in command ->
                "Здравствуйте. Рад вас слышать. Чем могу помочь?"
            "время" in command ->
                "Сейчас " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) + "."
            "дата" in command || "число" in command ->
                "Сегодня " + SimpleDateFormat("d MMMM yyyy", Locale("ru")).format(Date()) + "."
            "как дела" in command ->
                "Все системы работают в режиме прототипа. Готов к вашим командам."
            "кто ты" in command ->
                "Я JARVIS, ваш персональный голосовой помощник. Пока я нахожусь на ранней стадии разработки."
            else ->
                "Команду получил. Интеллектуальный модуль ещё подключается. В следующей версии я смогу отвечать на более сложные вопросы."
        }
        transcript.text = "ВЫ: " + raw + "\n\nJARVIS: " + answer
        status.text = "SPEAKING"
        if (::tts.isInitialized) tts.speak(answer, TextToSpeech.QUEUE_FLUSH, null, "jarvis_reply")
        status.postDelayed({
            if (!isFinishing) status.text = "SYSTEM STANDBY"
        }, 2500)
    }

    override fun onInit(result: Int) {
        if (result == TextToSpeech.SUCCESS) tts.language = Locale("ru", "RU")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}