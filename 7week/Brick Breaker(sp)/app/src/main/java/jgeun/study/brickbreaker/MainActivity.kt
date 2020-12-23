package jgeun.study.brickbreaker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    public lateinit var mContext:Context
    private lateinit var gameView: GameView
    private val pauseCode = 1000
    private var isContinue = false
    private var isTimerFinish = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this
        Log.d("MainActivityCheck", "MainActivity들어옴")
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
                    overridePendingTransition(0, 0)
                    finish()
                }

                isContinue = data.getBooleanExtra("isContinue", false)
                if(isContinue) {
                    val ballThreadArray = gameView.ballThreadArray
                    for(ballThread in ballThreadArray)
                        ballThread.pauseFlag = false
                    val effectThreadArray = gameView.effectThreadArray
                    for(effectThread in effectThreadArray)
                        effectThread.pauseFlag = false
                    gameView.blockThread.pauseFlag = false
                }

                isTimerFinish = data.getBooleanExtra("isTimerFinish", false)
                if(isTimerFinish){
                    Log.d("isTimerFinish", isTimerFinish.toString())
                    gameView.threadStart()
                }
                isTimerFinish = false
            }
        }
    }

    fun getGameView(): View {
        return gameView
    }
    fun getIsContinue(): Boolean{
        return isContinue
    }
}