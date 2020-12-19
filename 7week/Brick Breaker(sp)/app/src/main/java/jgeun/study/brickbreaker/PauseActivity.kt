package jgeun.study.brickbreaker

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import jgeun.study.brickbreaker.databinding.ActivityPauseBinding

class PauseActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPauseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityPauseBinding.inflate(layoutInflater)
        binding.btnGoMain.setOnClickListener{
            val sendContinueIntent = intent
            sendContinueIntent.putExtra("isFinish", true)
            setResult(RESULT_OK, sendContinueIntent)
            finish()
        }

        binding.btnContinue.setOnClickListener{
            val sendContinueIntent = intent
            sendContinueIntent.putExtra("isContinue", true)
            setResult(RESULT_OK, sendContinueIntent)
            finish()
        }

        setContentView(binding.root)
    }
}