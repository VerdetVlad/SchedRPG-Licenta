package com.vladv.questsched.utilities

class ChatMessages {

    var seenStatus:String?=null
    var messageID:String?=null
    var message:String?=null
    var from:String?=null
    var to:String?=null

    constructor()
    constructor(
        seenStatus: String?,
        messageID: String?,
        message: String?,
        from: String?,
        to: String?
    ) {
        this.seenStatus = seenStatus
        this.messageID = messageID
        this.message = message
        this.from = from
        this.to = to
    }

}