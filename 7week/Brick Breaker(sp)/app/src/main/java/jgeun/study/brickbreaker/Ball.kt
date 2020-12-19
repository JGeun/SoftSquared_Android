package jgeun.study.brickbreaker

class Ball {
    var x:Float = 0F
    var y:Float = 0F
    var width:Int = 60
    var height:Int = 60
    var speedX: Float = 3F
    var speedY: Float = -3F
    var isExist:Boolean = true
    constructor(x:Float, y:Float, width:Int, height:Int, speedX:Float, speedY:Float){
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.speedX = speedX
        this.speedY = speedY
    }

}