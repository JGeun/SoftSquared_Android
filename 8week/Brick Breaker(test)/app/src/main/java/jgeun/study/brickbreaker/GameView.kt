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
    private var screenLineTop = 400F
    private var screenGameTop = 410F

    private var screenGameBottom: Float
    private var screenLineBottom: Float

    private var highScore: Int = 300
    private var score = 0
    private var isHighScore:Boolean = false
    private var pauseImage : Bitmap

    private var blockWidth: Float
    private var blockHeight: Float
    private var blockPosXArray: ArrayList<Float> = ArrayList()
    private var blockPosYArray : ArrayList<Float> = ArrayList()
    private var blockArray : ArrayList<Block> = ArrayList()
    private var blockCount = 40
    private var blockLife = 1

    private lateinit var blockThread:BlockThread

    private var ball: Ball
    private var ballCount: Int = 1
    private var ballArray : ArrayList<Ball> = ArrayList()
    private var ballImageArray : ArrayList<Bitmap> = ArrayList()
    private lateinit var ballThread: BallThread
    private var ballThreadArray: ArrayList<BallThread> = ArrayList()
    private var ballPosX:Float
    private var ballPosY:Float

    private var bubbleImageArray :ArrayList<Bitmap> = ArrayList()

    private var isBallMove: Boolean = false
    private var isUserMove: Boolean = false
    private var userClickX: Float = -1F
    private var userClickY: Float = -1F

    private var crossValue: Double
    private var stdCrossCosinValue: Double
    private var stdCrossSinValue: Double

    private val pauseCode = 1000

    constructor(context: Context) : super(context){
        (context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels

        screenGameBottom = screenHeight.toFloat()-500F
        screenLineBottom = screenGameBottom+10F

        val gameScreenHeight = (screenGameBottom - screenGameTop)
        crossValue = Math.sqrt((gameScreenHeight*gameScreenHeight + screenWidth*screenWidth).toDouble())
        stdCrossCosinValue = screenWidth / crossValue
        stdCrossSinValue =  gameScreenHeight /crossValue

        pauseImage = BitmapFactory.decodeResource(resources, R.drawable.btn_stop)
        pauseImage = Bitmap.createScaledBitmap(pauseImage, 100, 100, true)

        val sharedPreferences = context.getSharedPreferences("score", AppCompatActivity.MODE_PRIVATE)
        if(sharedPreferences != null){
            highScore = sharedPreferences.getInt("userScore", 0)
        }

        blockWidth = screenWidth.toFloat()/6
        blockHeight = (screenGameBottom-screenGameTop)/9

        for(i in 0 until 6){
            blockPosXArray.add(blockWidth*i+10F)
        }

        blockDown()
        blockAdd()


        ball = Ball(screenWidth.toFloat()/2 - screenWidth.toFloat()/64, screenGameBottom-screenHeight.toFloat()/128-10F, screenWidth/32, screenHeight/64)
        ballPosX = screenWidth.toFloat()/2
        ballPosY = screenGameBottom-screenHeight/128-10F
        ballArray.add(ball)


        for(ball in ballArray){
            var ballImage = BitmapFactory.decodeResource(resources, R.drawable.heart)
            ballImage = Bitmap.createScaledBitmap(ballImage, ball.width, ball.height, true)
            ballImageArray.add(ballImage)
        }

        var bubbleGreenImage = BitmapFactory.decodeResource(resources, R.drawable.bubble_green)
        bubbleGreenImage = Bitmap.createScaledBitmap(bubbleGreenImage, 60, 60, true)
        bubbleImageArray.add(bubbleGreenImage)

        isFocusable = true
        mHandler.sendEmptyMessageDelayed(0, 10)
    }

    override fun onDraw(canvas: Canvas){
        paint.setTypeface(Typeface.create("Bold", Typeface.BOLD))
        for (block in blockArray) {
            if (block.isExist) {
                paint.setColor(Color.RED)
                canvas.drawRect(block.x, block.y, block.x + block.width, block.y + block.height, paint)
                paint.setColor(Color.WHITE)
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText(block.life.toString(), block.x + block.width/2, block.y + block.height/2+17F, paint)
                paint.textAlign = Paint.Align.LEFT
            }
        }

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        if (!isBallMove) {
            paint.setColor(context.resources.getColor(R.color.ballColor))

            canvas.drawText("x" + ballCount.toString(), ball.x - ball.width / 2, screenLineBottom + 60F, paint)
        }

        paint.setColor(Color.BLACK)
        paint.textSize = 60F
        paint.style = Paint.Style.FILL
        canvas.drawText("최대 점수: ", 100F, 100F, paint)
        canvas.drawText(highScore.toString(), 400F, 100F, paint)
        canvas.drawText("현재 점수: ", 100F, 200F, paint)
        canvas.drawText(score.toString(), 400F, 200F, paint)

        if (isUserMove && !isBallMove) {
            paint.strokeWidth = 10F
            paint.setColor(Color.GREEN)
            canvas.drawLine(ball.x + ball.width / 2, ball.y, userClickX, userClickY, paint)
        }

        paint.setColor(Color.BLACK)
        canvas.drawRect(0F, screenLineTop, screenWidth.toFloat(), screenGameTop, paint)
        canvas.drawRect(0F, screenGameBottom, screenWidth.toFloat(), screenLineBottom, paint)
        canvas.drawBitmap(pauseImage, 950F, 40F, null)
        for (i in 0 until ballArray.size) {
            if (ballArray.get(i).isExist) {
                canvas.drawBitmap(ballImageArray.get(i), ballArray.get(i).x, ballArray.get(i).y, null)
            }
        }
    }

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            invalidate()
            sendEmptyMessageDelayed(0, 10)
        }
    }

    inner class BallThread : Thread{
        var ball: Ball
        var stopFlag: Boolean = false

        constructor(ball:Ball){
            this.ball = ball
        }

        override fun run(){
            try{
                while(true) {
                    if (!stopFlag) {
                        ball.x += ball.speedX // 가로로 이동
                        ball.y += ball.speedY // 세로로 이동

                        if (ball.x < ball.width / 2) {                           // 왼쪽 벽
                            ball.x = ball.width / 2.toFloat()
                            ball.speedX = -ball.speedX
                        } else if (ball.x > screenWidth - ball.width / 2) {         // 오른쪽 벽
                            ball.x = (screenWidth - ball.width / 2).toFloat()
                            ball.speedX = -ball.speedX
                        } else if (ball.y < screenGameTop){                    // 천정
                            ball.y = screenGameTop.toFloat()
                            ball.speedY = -ball.speedY
                        } else if (ball.y > screenGameBottom - ball.height/2){
                            ball.y = screenGameBottom-ball.height/2
                            isBallMove = false
                            ballCount+=1
                            blockThread.stopFlag = true
                            stopFlag = true
                        }
                        sleep(10)
                    } else {
                        break;
                    }
                }
                Log.d("MainActivityCheck","ball 끝남")
                blockLife+=1
                blockDown()
                blockAdd()
            }catch(e : InterruptedException){
                Log.d("MainActivityCheck","볼 멈춤")
            }
        }
    }

    inner class BlockThread: Thread(){
        var stopFlag = false
        var isCrash = false
        override fun run(){
            while(true){
                if(!stopFlag){
                    for(block in blockArray){
                        for(ball in ballArray) {
                            Log.d("MainActivityCheck","반복문 시작")
                            if (block.isExist && block.isBallHit(ball)){
                                isCrash = true
                                Log.d("MainActivityCheck","부딪힘")
                                Log.d("MainActivityCheck", "blockLife: " + block.life.toString())
                                if(block.life == 0)
                                    block.isExist = false
                                score += 100
                                if (score > highScore){
                                    if(!isHighScore)
                                        isHighScore = true
                                    highScore = score
                                }
                            }
                        }
                    }
                    if(isCrash){
                        sleep(100)
                        isCrash = false
                    }

                }else {
                    this.interrupt()
                    break;
                }
            }
        }
    }

    fun blockAdd(){
        val randNum = (1..100).random()
        var num: Int
        if(randNum>=1 && randNum<=40){ //40퍼 확률
            num = 1
        }else if(randNum>=41 && randNum <= 70){ //30퍼확률
            num = 2
        }else if(randNum>=71 && randNum <= 90){ //20퍼확률
            num = 3
        }else{ //10퍼 확률
            num = 4
        }

        var storePosArray:ArrayList<Int> = ArrayList()
        while(num+1 != storePosArray.size){
            val pos = (0..5).random()
            if(!storePosArray.contains(pos))
                storePosArray.add(pos)
        }
        Log.d("MainActivityCheck", storePosArray.size.toString())
        for(i in 1..num){
            var block: Block = Block( blockPosXArray.get(storePosArray.get(i-1)), screenGameTop+blockHeight+10F, blockWidth.toInt()-15, blockHeight.toInt()-10, true)
            block.setBlockLife(blockLife)
            blockArray.add(block)
        }
    }

    fun blockDown(){
        for(block in blockArray){
            block.y += blockHeight
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var barX = 0F
        if(event!!.action == MotionEvent.ACTION_DOWN){
            if(event.x >= 950 && event.x <= 1050 && event.y >= 40 && event.y <=140) {
                Log.d("TouchEvent", "클릭")
                blockThread.interrupt()
                for(ballThread in ballThreadArray)
                    ballThread.stopFlag = true
                (context as Activity).startActivityForResult(Intent(context, PauseActivity::class.java), pauseCode)
            }
            if(!isBallMove){
                if(event.getY() >= screenGameTop && event.getY() <= screenGameBottom-blockHeight+20F){
                    isUserMove = true
                    userClickX = event.getX()
                    userClickY = event.getY()
                }else{
                    isUserMove = false
                }
            }

        }else if(event.action == MotionEvent.ACTION_MOVE){
            if(!isBallMove){
                if(event.getY() >= screenGameTop && event.getY() <= screenGameBottom-blockHeight+20F){
                    isUserMove = true
                    userClickX = event.getX()
                    userClickY = event.getY()
                }else{
                    isUserMove = false
                }
            }
        }

        else if(event.action == MotionEvent.ACTION_UP){
            if(!isBallMove){
                if(event.getY() >= screenGameTop && event.getY() <= screenGameBottom-blockHeight+20F) {
                    blockThread = BlockThread()
                    blockThread.start()
                    isUserMove = false
                    isBallMove = true
                    ball.speedX = (event.getX()-(ball.x+ball.width/2))/20
                    ball.speedY = (event.getY() -ball.y)/20
                    ballThread = BallThread(ball)
                    ballThread.start()
                }
            }
        }
        return true;
    }
}