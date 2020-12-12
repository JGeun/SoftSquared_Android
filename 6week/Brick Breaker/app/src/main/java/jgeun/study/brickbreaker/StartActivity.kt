package jgeun.study.brickbreaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btn_start.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btn_exit.setOnClickListener{
            finish()
        }
    }
}