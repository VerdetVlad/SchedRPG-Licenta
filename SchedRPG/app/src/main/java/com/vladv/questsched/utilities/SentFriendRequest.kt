package com.vladv.questsched.utilities

import com.vladv.questsched.user.User

class SentFriendRequest()
{
    val type = "received"
    val username = User().username
    val avatarFace = User().avatar?.drawableFace

}