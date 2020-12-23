package jgeun.study.brickbreaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jgeun.study.brickbreaker.databinding.ActivityExplainBinding

class ExplainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExplainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExplainBinding.inflate(layoutInflater)

        binding.btnBack.setOnClickListener{

           // startActivity()
            finish()
            overridePendingTransition(0, 0)
        }
        setContentView(binding.root)
    }
}