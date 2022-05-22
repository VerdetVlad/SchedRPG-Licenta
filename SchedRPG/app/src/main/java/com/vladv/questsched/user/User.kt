package com.vladv.questsched.user

import com.vladv.questsched.utilities.CharacterStats
import com.vladv.questsched.utilities.Quest

class User {

    companion object {
        private var fullName: String? = null
        private var email: String? = null
        private var stats: CharacterStats? = null
        private var quests: ArrayList<Quest>? = null
    }

    val fullName: String?
        get() = Companion.fullName
    val email: String?
        get() = Companion.email
    val stats: CharacterStats?
        get() = Companion.stats
    val quests: ArrayList<Quest>?
        get() = Companion.quests

    constructor()
    constructor(fullName: String?, email: String?) {
        Companion.fullName = fullName
        Companion.email = email
        Companion.quests = ArrayList()
        val a:ArrayList<Int> = arrayListOf(1,2,3,4,5,6)
        Companion.stats = CharacterStats(13,455,2000,a)
    }

    constructor(fullName: String?, email: String?, quests: ArrayList<Quest>?) {
        Companion.fullName = fullName
        Companion.email = email
        Companion.quests = quests
        val a:ArrayList<Int> = arrayListOf(1,2,3,4,5,6)
        Companion.stats = CharacterStats(13,455,2000,a)
    }

    constructor(fullName: String?, email: String?, stats: CharacterStats?, quests: ArrayList<Quest>?) {
        Companion.fullName = fullName
        Companion.email = email
        Companion.quests = quests
        Companion.stats = stats
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
                "fullName='" + Companion.fullName + '\'' +
                ", email='" + Companion.email + '\'' +
                ", stats=" +Companion.stats + '\'' +
                ", quests=" + Companion.quests +
                '}'
    }


    fun completeQuest(q:Quest)
    {
        val find = Companion.quests?.firstOrNull { it == q }
        val changeXP = Companion.stats?.changeXP(find!!.questXpReward())
    }

    fun abandonQuest(q:Quest)
    {
        val find = Companion.quests?.firstOrNull { it == q }
        val changeXP = Companion.stats?.changeXP(find!!.questXpLoss())
    }

}