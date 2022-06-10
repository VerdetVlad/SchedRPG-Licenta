package com.vladv.questsched.user

import com.vladv.questsched.utilities.FinishedQuestData
import com.vladv.questsched.utilities.MyDate
import java.util.*
import kotlin.collections.ArrayList

class QuestHistory {

    var questHistoryMap :MutableMap<String,MutableList<FinishedQuestData>> = mutableMapOf()


    constructor()

    constructor(questHistoryMap: MutableMap<String, MutableList<FinishedQuestData>>) {
        this.questHistoryMap = questHistoryMap
    }


    fun addToQuestHistory(date:MyDate, quest:Quest, completed:Boolean) {
        if(questHistoryMap[date.toStringDateKey()].isNullOrEmpty())
            questHistoryMap[date.toStringDateKey()] = ArrayList()

        questHistoryMap[date.toStringDateKey()]?.add(FinishedQuestData(quest,completed))
    }

    fun addToQuestHistory(date:String, quest:Quest, completed:Boolean) {
        if(questHistoryMap[date].isNullOrEmpty())
            questHistoryMap[date] = ArrayList()

        questHistoryMap[date]?.add(FinishedQuestData(quest,completed))
    }

}