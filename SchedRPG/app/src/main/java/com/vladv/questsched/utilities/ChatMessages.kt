package com.vladv.questsched.utilities

class ChatMessages {

    var message:String?=null
    var from:String?=null
    var to:String?=null

    constructor()
    constructor(message: String?, from: String?, to: String?) {
        this.message = message
        this.from = from
        this.to = to
    }
}