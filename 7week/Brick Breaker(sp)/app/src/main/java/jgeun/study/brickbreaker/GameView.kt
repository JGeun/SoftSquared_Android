package jgeun.study.brickbreaker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates

class GameView : View {
    private var paint: Paint = Paint()
    private val displayMetrics: DisplayMetrics = DisplayMetrics()
    private var screenHeight by Delegates.notNull<Int>()
    private var screenWidth by Delegates.notNull<Int>()
    private var screenLineTop = 300F
    private var screenGameTop = 310F

    private var screenGameBottom: Float
    private var screenLineBottom: Float

    private var life = 3

    private var highScore: Int = 300
    private var score = 0
    private var isHighScore:Boolean = false
    private var pauseImage : Bitmap

    private var blockArray : ArrayList<Block> = ArrayList()
    private var blockCount = 40

    private var bar: Bar
    private var barImage: Bitmap

    private var ballArray : ArrayList<Ball> = ArrayList()
    private var ballImageArray : ArrayList<Bitmap> = ArrayList()

    var ballThreadArray: ArrayList<BallThread> = ArrayList()
    var blockThread:BlockThread = BlockThread()
    var effectThreadArray : ArrayList<EffectThread> = ArrayList()

    private var bubbleImageArray :ArrayList<Bitmap> = ArrayList()

    var isContinue = false
    private val pauseCode = 1000

    constructor(context: Context) : super(context){
        (context as Activity).startActivityForResult(Intent(context, GameTimerActivity::class.java), 1000)

        (context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels

        screenGameBottom = screenHeight.toFloat()
        screenLineBottom = screenGameBottom+10F

        pauseImage = BitmapFactory.decodeResource(resources, R.drawable.btn_stop)
        pauseImage = Bitmap.createScaledBitmap(pauseImage, 100, 100, true)

        val sharedPreferences = context.getSharedPreferences("score", AppCompatActivity.MODE_PRIVATE)
        if(sharedPreferences != null){
            highScore = sharedPreferences.getInt("userScore", 0)
        }

        var blockHeightNum = 0
        var count = -1
        for(i in 0 until 40){
            count += 1
            if(count == 8) {
                count = 0
                blockHeightNum += 1
            }
            var block: Block = Block(5F+screenWidth/8*count, screenGameTop+100F+(blockHeightNum*110F), 100, 50, true)
            blockArray.add(block)
        }

        bar = Bar((screenWidth/2).toFloat(), (screenHeight-screenHeight/32).toFloat(), screenWidth/4, screenHeight/32)
        barImage = BitmapFactory.decodeResource(resources, R.drawable.heart)
        barImage = Bitmap.createScaledBitmap(barImage, bar.width, bar.height, true)

        var ball1 = Ball(600F, 2000F, screenWidth/32, screenHeight/64, 5F, -5F)
        var ball2 = Ball(500F, 2000F, screenWidth/32, screenHeight/64, -6F, -6F)
        var ball3 = Ball(700F, 2000F, screenWidth/32, screenHeight/64, 7F, -7F)

        ballArray.add(ball1)
        ballArray.add(ball2)
        ballArray.add(ball3)

        for(ball in ballArray){
            var ballImage = BitmapFactory.decodeResource(resources, R.drawable.heart)
            ballImage = Bitmap.createScaledBitmap(ballImage, ball.width, ball.height, true)
            ballImageArray.add(ballImage)
        }

        ballThreadArray.add(BallThread(ball1))
        ballThreadArray.add(BallThread(ball2))
        ballThreadArray.add(BallThread(ball3))


        var bubbleGreenImage = BitmapFactory.decodeResource(resources, R.drawable.bubble_green)
        bubbleGreenImage = Bitmap.createScaledBitmap(bubbleGreenImage, 60, 60, true)
        bubbleImageArray.add(bubbleGreenImage)

        var bubbleRedImage = BitmapFactory.decodeResource(resources, R.drawable.bubble_red)
        bubbleRedImage = Bitmap.createScaledBitmap(bubbleRedImage, 60, 60, true)
        bubbleImageArray.add(bubbleRedImage)

        if((context as MainActivity).getIsContinue()){
            Log.d("MainActivityCheck", "continue")
            blockThread.pauseFlag = false
            for(effectThread in effectThreadArray)
                effectThread.pauseFlag = false
            for(ballThread in ballThreadArray)
                ballThread.pauseFlag = false
        }
        isFocusable = true
        mHandler.sendEmptyMessageDelayed(0, 10)
    }

    fun threadStart(){
        blockThread.start()
        for(i in 0 until 3)
            ballThreadArray.get(i).start()
    }

    override fun onDraw(canvas: Canvas) {
        paint.setColor(Color.RED)

        for(block in blockArray) {
            if(block.isExist)
                canvas.drawRect(block.x, block.y, block.x+block.width, block.y+block.height, paint)
        }
        paint.setColor(Color.BLACK)
        paint.textSize = 60F
        paint.style = Paint.Style.FILL
        canvas.drawText("최대 점수: ", 100F, 100F, paint)
        canvas.drawText(highScore.toString(), 400F, 100F, paint)
        canvas.drawText("현재 점수: ", 100F, 200F, paint)
        canvas.drawText(score.toString(), 400F, 200F, paint)

        canvas.drawRect(0F, screenLineTop, screenWidth.toFloat(), screenGameTop, paint)
        canvas.drawRect(0F, screenLineTop, screenWidth.toFloat(), screenGameTop, paint)
        canvas.drawBitmap(pauseImage, 950F, 40F, null)
        for(i in 0 until ballArray.size){
            if(ballArray.get(i).isExist)
                canvas.drawBitmap(ballImageArray.get(i), ballArray.get(i).x, ballArray.get(i).y, null)
        }

        for(effectThread in effectThreadArray){
            if(effectThread.isExist)
                canvas.drawBitmap(effectThread.image, effectThread.x, effectThread.y, null)
        }

        paint.setColor(Color.CYAN)
        val barRect = RectF(bar.x,bar.y,bar.x+bar.width, bar.y+bar.height)
        canvas.drawRoundRect(barRect, 14F, 14F, paint)
    }

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            invalidate()
            sendEmptyMessageDelayed(0, 10)
        }
    }

    inner class BallThread : Thread{
        lateinit var ball: Ball
        var stopFlag: Boolean = false
        var pauseFlag: Boolean = false

        constructor(ball:Ball){
            this.ball = ball
        }

        override fun run(){
            try{
                while(true) {
                    if (!stopFlag) {
                        if(!pauseFlag){
                            ball.x += ball.speedX // 가로로 이동
                            ball.y += ball.speedY // 세로로 이동

                            if (ball.x < ball.width / 2) {                           // 왼쪽 벽
                                ball.x = ball.width / 2.toFloat()
                                ball.speedX = -ball.speedX
                            } else if (ball.x > screenWidth - ball.width / 2) {         // 오른쪽 벽
                                ball.x = (screenWidth - ball.width / 2).toFloat()
                                ball.speedX = -ball.speedX
                            } else if (ball.y < screenGameTop ) {                    // 천정
                                ball.y = screenGameTop.toFloat()
                                ball.speedY = -ball.speedY
                            } else if (ball.y > screenHeight - ball.height/2){
                                this.interrupt()
                                ball.isExist = false
                                life -= 1
                                Log.d("Life", life.toString())
                                if(life == 0){
                                    blockThread.interrupt()
                                    val intent = Intent(context, RankActivity::class.java)
                                    intent.putExtra("score", score)
                                    intent.putExtra("isHighScore", isHighScore)
                                    (context as Activity).startActivity(intent)
                                    (context as Activity).finish()
                                }
                            } else if (ball.y > screenHeight - ball.height / 2 - bar.height) {        // 바닥
                                if (isBarHit()) {
                                    ball.y = (screenHeight - ball.height / 2 - bar.height).toFloat()
                                    ball.speedY = -ball.speedY
                                }
                            }
                            sleep(10)
                        }
                    } else {
                        this.interrupt()
                        break;
                    }
                }
            }catch(e : InterruptedException){
                e.printStackTrace()
            }
        }

        fun isBarHit(): Boolean {
            if( (ball.x+ball.width) >= bar.x && (ball.x <= bar.x+bar.width)) {
                Log.d("Hit", "bar랑 부딪힘")
                return true
            }
            else
                return false
        }
    }

    inner class BlockThread: Thread(){
        var pauseFlag = false
        override fun run(){
            while(true){
                if(!pauseFlag){
                    for(block in blockArray) {
                        for (ball in ballArray) {
                            if (block.isExist && block.isBallHit(ball)) {
                                block.isExist = false
                                blockCount -= 1
                                score += 100
                                if (score > highScore) {
                                    if (!isHighScore)
                                        isHighScore = true
                                    highScore = score
                                }
                                Log.d("BlockCount", blockCount.toString())
                                if (blockCount <= 0) {
                                    this.interrupt()
                                    for (ballThread in ballThreadArray)
                                        ballThread.stopFlag = true
                                    Log.d("isMaxScore", "GameView: " + isHighScore.toString())
                                    val intent = Intent(context, RankActivity::class.java)
                                    intent.putExtra("score", score)
                                    intent.putExtra("isMaxScore", isHighScore)
                                    (context as Activity).startActivity(intent)
                                    (context as Activity).finish()
                                }

                                val num = (Math.random() * 10 + 1).toInt()
                                when (num) {
                                    4 -> {
                                        var effectThread: EffectThread = EffectThread(block.x, block.y, bubbleImageArray.get(0), 1)
                                        effectThread.start()
                                        effectThreadArray.add(effectThread)
                                    }
                                    7 -> {
                                        var effectThread: EffectThread = EffectThread(block.x, block.y, bubbleImageArray.get(1), 2)
                                        effectThread.start()
                                        effectThreadArray.add(effectThread)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    inner class EffectThread: Thread{
        var x : Float
        var y : Float
        var image: Bitmap
        var effect: Int
        var isExist = true
        var pauseFlag = false
        constructor(x:Float, y:Float, image: Bitmap, effect: Int){
            this.x = x
            this.y = y
            this.image = image
            this.effect = effect
        }

        override fun run() {
            while(true){
                if(!pauseFlag){
                    y += 10F
                    if(isBarHit()){
                        when(effect){
                            1 -> {
                                if(bar.width <= 400)
                                    bar.width += 100
                                score -= 100
                            }
                            2 -> {
                                if(bar.width >= 200)
                                    bar.width -= 100
                                score += 200
                            }
                        }
                        isExist =false
                        break
                    }
                    if(y > screenHeight) {
                        isExist = false
                        break
                    }

                    sleep(10)
                }

            }
        }
        fun isBarHit(): Boolean {
            if( (this.x+60) >= bar.x && (this.x <= bar.x+bar.width) && (this.y+60 >= bar.y && this.y <= bar.y+bar.height)) {
                return true
            }
            else
                return false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var barX = 0F
        if(event!!.action == MotionEvent.ACTION_DOWN){
            if(event.x >= 950 && event.x <= 1050 && event.y >= 40 && event.y <=140) {
                Log.d("TouchEvent", "클릭")

                blockThread.pauseFlag = true
                for(ballThread in ballThreadArray)
                    ballThread.pauseFlag = true
                for(effectThread in effectThreadArray)
                    effectThread.pauseFlag = true
                (context as Activity).startActivityForResult(Intent(context, PauseActivity::class.java), pauseCode)
                (context as Activity).overridePendingTransition(0, 0)
            }
            barX = event.getX()
            Log.d("barPositionDown", "눌린 마우스 위치" + barX.toString())
            Log.d("barPositionDown", "현재 bar의 위치" + bar.x.toString())
        }else if(event.action == MotionEvent.ACTION_MOVE) {
            if(bar.x + bar.width > screenWidth) {
                Log.d("barPositionMove", "화면보다 커요")
                barX = (screenWidth - bar.width).toFloat()
            }
            else if(bar.x < 0)
                barX = 0F
            else {
                barX = (event.getX() - (bar.width / 2))
                Log.d("barPositionMove", "evet.getX(): " + event.getX().toString())
            }
            Log.d("barPositionMove", "움직이는 중: barX는 " + barX.toString())
            bar.x = barX
            Log.d("barPositionMove", "움직이는 중: barPosX는" + bar.x.toString())
        }

        else if(event.action == MotionEvent.ACTION_UP){
            if(barX < 0)
                bar.x = 0F
            else if(barX + bar.width > screenWidth)
                bar.x = (screenWidth - bar.width).toFloat()
        }
        return true;
    }
}
