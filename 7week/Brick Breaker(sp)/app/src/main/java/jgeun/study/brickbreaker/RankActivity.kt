package jgeun.study.brickbreaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jgeun.study.brickbreaker.databinding.ActivityRankBinding
import java.io.Serializable

data class UserRecordKey(val nameKey: String?, val scoreKey: String?)

class RankActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRankBinding.inflate(layoutInflater)
        val score = intent.getIntExtra("score", -1)
        val isHighScore:Boolean = intent.getBooleanExtra("isHighScore", false)

        Log.d("MainACtivityCheck", "Score: " + score.toString())
        binding.tvScore.setText(score.toString())
        if(isHighScore){
            binding.tvRenewScore.visibility = View.VISIBLE
            val sharedPreferences = getSharedPreferences("score", MODE_PRIVATE)
            val editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("userScore", score)
            editor.apply()
        }
        binding.btnHome.setOnClickListener{
            startActivity(Intent(this, StartActivity::class.java))
            binding.tvRenewScore.visibility = View.INVISIBLE
            finish()
        }

        binding.btnStart.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            binding.tvRenewScore.visibility = View.INVISIBLE
            finish()
        }
        setContentView(binding.root)
    }
}