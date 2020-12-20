package jgeun.study.brickbreaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jgeun.study.brickbreaker.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        binding.btnStart.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnExit.setOnClickListener{
            finish()
        }

        setContentView(binding.root)
    }
}