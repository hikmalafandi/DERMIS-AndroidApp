package com.capstone.dermis.view.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.capstone.dermis.databinding.ActivitySplashScreenBinding
import com.capstone.dermis.view.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private val splash_time: Long = 750
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(i)

            finish()
        }, splash_time)
    }
}