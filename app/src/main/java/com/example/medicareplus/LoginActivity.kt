package com.example.medicareplus

import android.animation.AnimatorSet
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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isKeepLoggedIn = prefs.getBoolean("keep_logged_in", false)

        if (isKeepLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Set the login dark theme
        setTheme(R.style.AppTheme_Login)
        setContentView(R.layout.activity_login)

        val logo = findViewById<ImageView>(R.id.ivLogo)
        val appName = findViewById<TextView>(R.id.tvAppName)
        val usernameInput = findViewById<EditText>(R.id.loginUsername)
        val passwordInput = findViewById<EditText>(R.id.loginPassword)
        val keepLoginCheckbox = findViewById<CheckBox>(R.id.keepLogin)
        val loginBtn = findViewById<com.google.android.material.button.MaterialButton>(R.id.loginBtn)
        val toSignUp = findViewById<TextView>(R.id.toSignUp)

        // Style the "Sign Up" link text
        val signUpText = SpannableString("Don't have an account? Sign Up")
        signUpText.setSpan(
            ForegroundColorSpan(Color.parseColor("#6C7293")),
            0, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        signUpText.setSpan(
            ForegroundColorSpan(Color.parseColor("#1A73E8")),
            23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        toSignUp.text = signUpText

        // ========== ANIMATIONS ==========
        // Logo bounce animation
        logo.scaleX = 0f
        logo.scaleY = 0f
        logo.alpha = 0f

        val logoScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0f, 1f).apply {
            duration = 800
            interpolator = OvershootInterpolator(2f)
        }
        val logoScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0f, 1f).apply {
            duration = 800
            interpolator = OvershootInterpolator(2f)
        }
        val logoAlpha = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f).apply {
            duration = 600
        }
        AnimatorSet().apply {
            playTogether(logoScaleX, logoScaleY, logoAlpha)
            start()
        }

        // App name slide in
        appName.translationY = 40f
        appName.alpha = 0f
        appName.animate().translationY(0f).alpha(1f).setDuration(600).setStartDelay(300)
            .setInterpolator(DecelerateInterpolator()).start()

        // Form elements fade in with stagger
        val views = listOf<View>(
            usernameInput.parent.parent as View,  // Input container
            passwordInput.parent.parent as View,
            keepLoginCheckbox,
            loginBtn,
            toSignUp
        )
        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 30f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(500L + (index * 100L))
                .setInterpolator(DecelerateInterpolator())
                .start()
        }

        // ========== LOGIN LOGIC ==========
        loginBtn.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedPassword = prefs.getString(username, null)

            if (password == savedPassword) {
                if (keepLoginCheckbox.isChecked) {
                    prefs.edit().putBoolean("keep_logged_in", true).apply()
                }
                // Save the username for welcome greeting
                prefs.edit().putString("logged_in_user", username).apply()

                Toast.makeText(this, "Welcome, $username! ✅", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                // Shake animation on error
                loginBtn.animate().translationX(-10f).setDuration(50).withEndAction {
                    loginBtn.animate().translationX(10f).setDuration(50).withEndAction {
                        loginBtn.animate().translationX(-5f).setDuration(50).withEndAction {
                            loginBtn.animate().translationX(0f).setDuration(50).start()
                        }.start()
                    }.start()
                }.start()
            }
        }

        toSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
