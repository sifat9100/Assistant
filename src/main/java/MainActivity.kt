package com.personal.assistant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var keyBox: EditText
    private lateinit var cmdBox: EditText
    private lateinit var logView: TextView
    private var recognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (16 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(pad, pad * 2, pad, pad)

        keyBox = EditText(this)
        keyBox.hint = "Anthropic API key (sk-ant-...)"
        keyBox.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        keyBox.setText(getSharedPreferences("p", MODE_PRIVATE).getString("key", ""))

        cmdBox = EditText(this)
        cmdBox.hint = "কমান্ড লিখুন অথবা 🎤 চাপুন"

        logView = TextView(this)
        logView.textSize = 14f

        root.addView(button("১. অ্যাক্সেসিবিলিটি চালু করুন") {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        })
        root.addView(keyBox)
        root.addView(cmdBox)
        root.addView(button("🎤 বলুন") { listen() })
        root.addView(button("▶ লিখিত কমান্ড চালান") { runTask(cmdBox.text.toString()) })
        root.addView(button("■ থামান") { AgentService.instance?.stop() })
        root.addView(logView)

        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)
