package com.vladv.questsched.user

import com.vladv.questsched.utilities.MyDate
import kotlin.collections.ArrayList

class LastLogIn {
    var logInDate:MyDate? =null
    var unfinishedQuests:MutableList<Quest> = ArrayList()

    constructor()
    constructor(logInData: MyDate?, unfinishedQuests: ArrayList<Quest>) {
        this.logInDate = logInData
        this.unfinishedQuests = unfinishedQuests
    }

    constructor(logInData: MyDate?) {
        this.logInDate = logInData
    }

    fun addUnfinishedQuest(quest: Quest)
    {
        unfinishedQuests.add(quest)
    }

    fun removeFinishedQuest(quest: Quest)
    {
        unfinishedQuests.remove(quest)
    }

    override fun toString(): String {
        return "LastLogIn(logInData=$logInDate, unfinishedQuests=$unfinishedQuests)"
    }


}