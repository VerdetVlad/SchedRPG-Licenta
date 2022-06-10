package com.vladv.questsched.utilities

import com.vladv.questsched.user.Avatar
import com.vladv.questsched.user.Quest

class UserSocialProfile{

    var username:String? =null
    var profileDescription:String? =null
    var avatar: Avatar? =null
    var quests:ArrayList<Quest>? = null

    constructor()


    constructor(
        username: String?,
        description: String?,
        avatar: Avatar?,
        quests: ArrayList<Quest>?
    ) {
        this.username = username
        this.profileDescription = description
        this.avatar = avatar
        this.quests = quests
    }


}