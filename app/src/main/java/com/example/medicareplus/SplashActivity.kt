package com.example.medicareplus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Use a solid color or gradient for the splash screen
        setTheme(R.style.AppTheme_Login) 
        setContentView(R.layout.activity_splash)

        val splashLogo = findViewById<ImageView>(R.id.splashLogo)
        val splashText = findViewById<TextView>(R.id.splashText)

        // Initial state
        splashLogo.scaleX = 0f
        splashLogo.scaleY = 0f
        splashLogo.alpha = 0f
        
        splashText.translationY = 50f
        splashText.alpha = 0f

        // Animate Logo pop-in
        splashLogo.animate()
            .scaleX(1.2f).scaleY(1.2f).alpha(1f)
            .setDuration(800)
            .setInterpolator(OvershootInterpolator(1.5f))
            .withEndAction {
                // Subtle pulse effect
                splashLogo.animate().scaleX(1f).scaleY(1f).setDuration(400).start()
            }.start()

        // Animate text slide-up fade-in
        splashText.animate()
            .translationY(0f).alpha(1f)
            .setDuration(700)
            .setStartDelay(300)
            .setInterpolator(DecelerateInterpolator())
            .start()

        // Route to LoginActivity after 2.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2500)
    }
}
