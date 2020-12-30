package jgeun.study.networkapiairport

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jgeun.study.networkapiairport.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySelectBinding.inflate(layoutInflater)
        binding.selectIvDepart.setOnClickListener{
            startActivity(Intent(this, DepartActivity::class.java))
        }
        binding.selectIvArrival.setOnClickListener{
            startActivity(Intent(this, ArrivalActivity::class.java))
        }
        setContentView(binding.root)
    }
}