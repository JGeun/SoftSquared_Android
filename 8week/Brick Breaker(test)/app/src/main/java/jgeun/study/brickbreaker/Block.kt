package jgeun.study.brickbreaker

import android.util.Log

class Block{
    var x = 0F
    var y = 0F
    var width = 100
    var height = 100
    var isExist : Boolean = true
    var range : Float = 10F
    var life: Int

    constructor(x:Float, y: Float, width: Int, height: Int, isExist:Boolean){
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.isExist = isExist
        life = 1
    }

    fun setBlockLife(life: Int){
        this.life = life
    }

    fun isBallHit(ball: Ball) : Boolean{
        if( ( ( ((ball.y+ball.height) >= this.y-range) && (ball.y+ball.height) <= this.y+range) )
            && ( (((ball.x+ball.width) >= this.x-range) && (ball.x <= (this.x+this.width)+range)))
        ){//위쪽 벽면
            Log.d("ballCrash", "위쪽 벽면 부딪힘")
            //ball.speedX = -ball.speedX
            ball.speedY = -ball.speedY
            life -= 1
            return true

        }else if( ( (ball.y >= (this.y+this.height-range)) && (ball.y <= (this.y+this.height)+range))
            && (  ((ball.x+ball.width) >= (this.x-range)) && (ball.x <= ((this.x+this.width)+range)))
        ){ //아래쪽 벽면면
            Log.d("ballCrash", "아래쪽 벽면 부딪힘")
            //ball.speedX = -ball.speedX
            ball.speedY = -ball.speedY
            life -= 1
            return true
        }

        else if(((ball.x <= (this.x+this.width).toFloat()) && (ball.x >= (this.x+this.width).toFloat()-range))
            && ((ball.y+ball.height >= this.y) && (ball.y <= this.y+this.height))) {//오른쪽 벽면
            Log.d("ballCrash", "오른쪽 벽면 부딪힘")
            ball.speedX = -ball.speedX
            //ball.speedY = -ball.speedY'
            life -= 1
            return true
        }
        else if((((ball.x+ball.width) >= this.x.toFloat()) && (ball.x+ball.width) <= this.x.toFloat()+range)
            && ((ball.y+ball.height >= this.y) && (ball.y <= this.y+this.height))){ //왼쪽 벽면
            Log.d("ballCrash", "왼쪽 벽면 부딪힘")
            ball.speedX = -ball.speedX
            life -= 1
            // ball.speedY = -ball.speedY
            return true
        }
        else
            return false
    }
}