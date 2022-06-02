package com.vladv.questsched.utilities

class Avatar {
    var drawableFace:Int? = null
    var drawableBody:Int? = null
    var name:String? = null

    constructor()
    constructor(drawableFace: Int?, drawableBody: Int?, name: String?) {
        this.drawableFace = drawableFace
        this.drawableBody = drawableBody
        this.name = name
    }
}