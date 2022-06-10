package com.vladv.questsched.user

import com.vladv.questsched.utilities.AvatarList
import com.vladv.questsched.utilities.MyDate
import java.util.*

class User {

    companion object {
        private var username: String? = null
        private var email: String? = null
        private var profileDescription: String? = null
        private var stats: CharacterStats? = null
        private var quests: ArrayList<Quest>? = null
        private var avatar: Avatar? = null
        private var lastLogIn:LastLogIn?=null
        private var questHistory:QuestHistory?=null
        private var themeNightMode:Boolean?=null

        fun setDescription(profileDescription: String){
            this.profileDescription = profileDescription
        }

        fun setNighMode(mode: Boolean){
            this.themeNightMode = mode
        }

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
    val profileDescription: String?
        get() = Companion.profileDescription
    val stats: CharacterStats?
        get() = Companion.stats
    val quests: ArrayList<Quest>?
        get() = Companion.quests
    val avatar: Avatar?
        get() = Companion.avatar
    val lastLogIn: LastLogIn?
        get() = Companion.lastLogIn
    val questHistory: QuestHistory?
        get() = Companion.questHistory
    val themeNightMode: Boolean?
        get() = Companion.themeNightMode

    constructor()
    constructor(username: String, email: String) {
        Companion.username = username
        Companion.email = email
        Companion.quests = ArrayList()
        Companion.profileDescription = "Hello world!"
        val sLVL:ArrayList<Int> = arrayListOf(1,1,1,1,1,1)
        val sCurrXP:ArrayList<Int> = arrayListOf(0,0,0,0,0,0)
        val sMaxXP:ArrayList<Int> = arrayListOf(100,100,100,100,100,100)
        Companion.stats = CharacterStats(1,sLVL,sCurrXP,sMaxXP)
        Companion.avatar = AvatarList.avatarList[2]
        val calendar = Calendar.getInstance()
        val date = MyDate(calendar)
        Companion.lastLogIn = LastLogIn(date)
        Companion.themeNightMode = false

    }


    constructor(username: String, email: String, profileDescription:String,
                stats: CharacterStats, quests: ArrayList<Quest>, avatar: Avatar,
                lastLogIn: LastLogIn, questHistory: QuestHistory, themeNightMode:Boolean) {
        Companion.username = username
        Companion.email = email
        Companion.profileDescription = profileDescription
        Companion.quests = quests
        Companion.stats = stats
        Companion.avatar = avatar
        Companion.lastLogIn = lastLogIn
        Companion.questHistory = questHistory
        Companion.themeNightMode = themeNightMode
    }



    fun addQuest(quest: Quest) {
        if(quests==null) Companion.quests = ArrayList()
        Companion.quests!!.add(0,quest)
    }

    fun removeQuest(quest: Quest) {
        Companion.quests!!.remove(quest)
    }

    fun addToQuestHistory(date: MyDate, quest: Quest, completed:Boolean)
    {
        if(questHistory==null) Companion.questHistory = QuestHistory()
        Companion.questHistory?.addToQuestHistory(date, quest,completed)
    }

    fun addUnfinishedQuest(q: Quest)
    {
        lastLogIn!!.addUnfinishedQuest(q)
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
        val xpReward = q.difficulty!!.plus(1).times(stats!!.statsLvl[q.type!!])
            .times(stats!!.level!!).times(5)

        Companion.stats?.changeXP(q.type!!,xpReward)
        lastLogIn!!.removeFinishedQuest(q)
    }

    fun abandonQuest(q: Quest)
    {
        val xpLoss = q.difficulty!!.plus(1).times(stats!!.statsLvl[q.type!!])
            .times(stats!!.level!!).times(5).div(2)

        Companion.stats?.changeXP(q.type!!,xpLoss*(-1))
        lastLogIn!!.removeFinishedQuest(q)
    }


}