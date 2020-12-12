package jgeun.study.brickbreaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game_clear.*

class GameClearActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_clear)

        btn_goMain.setOnClickListener{
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}