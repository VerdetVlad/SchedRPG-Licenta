package com.vladv.questsched.tabs.fragments.social

import com.vladv.questsched.user.User

class SentFriendRequest()
{
    val type = "received"
    val username = User().username
    val avatarFace = User().avatar?.drawableFace

}