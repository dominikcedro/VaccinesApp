package com.example.vaccineapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupRegisterLink()
        val loginButton = findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * This function sets up the clickable link that redirects to the Register screen.
     * It finds the TextView for the Register link, creates a SpannableString with a clickable link,
     * and sets the text and movement method of the TextView.
     */
    private fun setupRegisterLink() {
        val registerLinkTextView = findViewById<TextView>(R.id.tvGoRegister)
        val spannableRegisterLink = createSpannableRegisterLink()

        registerLinkTextView.text = spannableRegisterLink
        registerLinkTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    /**
     * This function creates a SpannableString with a clickable link that redirects to the Register screen.
     * It creates a SpannableString from a full text, finds the start index of a clickable word,
     * creates a ClickableSpan that starts an intent to RegisterActivity when clicked,
     * and sets the ClickableSpan to the SpannableString.
     * @return The created SpannableString with a clickable link.
     */
    private fun createSpannableRegisterLink(): SpannableString {
        val fullText = "Need help? Register here."
        val clickableWord = "Register here."
        val spannableRegisterLink = SpannableString(fullText)
        val startIndex = fullText.indexOf(clickableWord)

        val registerLinkSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }

        spannableRegisterLink.setSpan(registerLinkSpan, startIndex, startIndex + clickableWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableRegisterLink
    }
}