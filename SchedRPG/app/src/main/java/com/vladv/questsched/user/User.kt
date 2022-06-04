package com.vladv.questsched.user

import com.vladv.questsched.utilities.AvatarList

class User {

    companion object {
        private var username: String? = null
        private var email: String? = null
        private var stats: CharacterStats? = null
        private var quests: ArrayList<Quest>? = null
        private var avatar: Avatar? = null

        fun setQuestsAtIndex(q: Quest, i: Int)
        {
            quests?.set(i, q)
        }
        fun setAvatar(a: Avatar){
            avatar = a
        }
    }

    val username: String?
        get() = Companion.username
    val email: String?
        get() = Companion.email
    val stats: CharacterStats?
        get() = Companion.stats
    val quests: ArrayList<Quest>?
        get() = Companion.quests
    val avatar: Avatar?
        get() = Companion.avatar

    constructor()
    constructor(username: String, email: String) {
        Companion.username = username
        Companion.email = email
        Companion.quests = ArrayList()
        val a:ArrayList<Int> = arrayListOf(1,2,3,4,5,6)
        Companion.stats = CharacterStats(13,455,2000,a)
        Companion.avatar = AvatarList.avatarList[2]
    }


    constructor(username: String, email: String, stats: CharacterStats, quests: ArrayList<Quest>, avatar: Avatar) {
        Companion.username = username
        Companion.email = email
        Companion.quests = quests
        Companion.stats = stats
        Companion.avatar = avatar
    }



    fun addQuest(quest: Quest) {
        if(quests==null) Companion.quests = ArrayList()
        Companion.quests!!.add(quest)
    }

    fun removeQuest(quest: Quest) {
        Companion.quests!!.remove(quest)
    }

    override fun toString(): String {
        return "User{" +
                "username='" + Companion.username + '\'' +
                ", email='" + Companion.email + '\'' +
                ", stats=" +Companion.stats + '\'' +
                ", quests=" + Companion.quests +
                '}'
    }


    fun completeQuest(q: Quest)
    {
        val find = Companion.quests?.firstOrNull { it == q }
        val changeXP = Companion.stats?.changeXP(find!!.questXpReward())
    }

    fun abandonQuest(q: Quest)
    {
        val find = Companion.quests?.firstOrNull { it == q }
        val changeXP = Companion.stats?.changeXP(find!!.questXpLoss())
    }


}