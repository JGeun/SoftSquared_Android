package jgeun.study.brickbreaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import jgeun.study.brickbreaker.databinding.ActivityGameTimerBinding

class GameTimerActivity : AppCompatActivity() {
    private lateinit var tv_timer: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGameTimerBinding.inflate(layoutInflater)

        tv_timer = binding.tvTimer
        TimerThread(this).start()
        setContentView(binding.root)
    }

    inner class TimerThread : Thread {
        private lateinit var context: Context
        private var n = 3

        constructor(context: Context) {
            this.context = context
        }

        override fun run() {

            while (n > 0) {
                runOnUiThread {
                    tv_timer.setText(n.toString())
                }
                sleep(1000)
                n -= 1
            }
            val sendContinueIntent = intent
            sendContinueIntent.putExtra("isTimerFinish", true)
            setResult(RESULT_OK, sendContinueIntent)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}