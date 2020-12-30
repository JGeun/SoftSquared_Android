package jgeun.study.networkapiairport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jgeun.study.networkapiairport.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this, SelectActivity::class.java))
        }
        setContentView(binding.root)
    }
}