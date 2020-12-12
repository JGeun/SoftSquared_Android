package jgeun.study.brickbreaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pause.*

class PauseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause)

        btn_restart.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btn_goStartMain.setOnClickListener{
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}