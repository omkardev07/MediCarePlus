package com.example.medicareplus

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Login)
        setContentView(R.layout.activity_signup)

        val logo = findViewById<ImageView>(R.id.ivLogo)
        val title = findViewById<TextView>(R.id.tvTitle)
        val fullNameInput = findViewById<EditText>(R.id.signUpFullName)
        val usernameInput = findViewById<EditText>(R.id.signUpUsername)
        val passwordInput = findViewById<EditText>(R.id.signUpPassword)
        val confirmPasswordInput = findViewById<EditText>(R.id.signUpConfirmPassword)
        val signUpBtn = findViewById<com.google.android.material.button.MaterialButton>(R.id.signUpBtn)
        val toLogin = findViewById<TextView>(R.id.toLogin)

        // Style the login link text
        val loginText = SpannableString("Already have an account? Login")
        loginText.setSpan(
            ForegroundColorSpan(Color.parseColor("#6C7293")),
            0, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        loginText.setSpan(
            ForegroundColorSpan(Color.parseColor("#1A73E8")),
            25, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        toLogin.text = loginText

        // ========== ANIMATIONS ==========
        logo.scaleX = 0f
        logo.scaleY = 0f
        logo.animate().scaleX(1f).scaleY(1f).setDuration(700)
            .setInterpolator(OvershootInterpolator(1.5f)).start()

        title.translationY = 30f
        title.alpha = 0f
        title.animate().translationY(0f).alpha(1f).setDuration(500).setStartDelay(200)
            .setInterpolator(DecelerateInterpolator()).start()

        val formViews = listOf<View>(
            fullNameInput.parent.parent as View,
            usernameInput.parent.parent as View,
            passwordInput.parent.parent as View,
            confirmPasswordInput.parent.parent as View,
            signUpBtn,
            toLogin
        )
        formViews.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 25f
            view.animate().alpha(1f).translationY(0f)
                .setDuration(400).setStartDelay(400L + (index * 80L))
                .setInterpolator(DecelerateInterpolator()).start()
        }

        // ========== SIGN UP LOGIC ==========
        signUpBtn.setOnClickListener {
            val fullName = fullNameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 4) {
                Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

            if (prefs.contains(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save user credentials and full name
            prefs.edit()
                .putString(username, password)
                .putString("${username}_fullname", fullName)
                .apply()

            Toast.makeText(this, "Account created! Welcome, $fullName ✅", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        toLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}
