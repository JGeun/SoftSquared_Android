package jgeun.study.brickbreaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private val pauseCode = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        gameView = GameView(this)
        setContentView(gameView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pauseCode){
            Log.d("MainActivityCheck", "code 확인")
            if(resultCode != Activity.RESULT_OK)
                return;

            if (data != null) {
                Log.d("MainActivityCheck", "data있음")
                val isFinish:Boolean = data.getBooleanExtra("isFinish", false)
                Log.d("MainActivityCheck", isFinish.toString())
                if(isFinish){
                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                }

                val isContinue:Boolean = data.getBooleanExtra("isContinue", false)
                if(isContinue){

                }
            }
        }
    }
}