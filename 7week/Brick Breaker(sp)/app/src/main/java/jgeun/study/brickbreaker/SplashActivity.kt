package jgeun.study.brickbreaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, StartActivity::class.java))
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }, 2000)

    }
}