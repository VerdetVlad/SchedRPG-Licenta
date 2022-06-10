package com.vladv.questsched.utilities

import com.example.schedrpg.R
import com.vladv.questsched.user.Quest

class FinishedQuestData {
    var name:String = ""
    var description:String = ""
    var typeInt = 0
    var type:String= ""
    var difficulty:String= ""
    var completed:Boolean = false

    constructor()


    constructor(quest:Quest,completed: Boolean)
    {
        this.name = quest.name!!
        this.description = quest.description!!
        this.type = quest.typeStringValue()
        this.difficulty = quest.difficultyStringValue()
        this.completed = completed
        typeInt = quest.type!!
    }

    constructor(
        name: String,
        description: String,
        typeInt: Int,
        type: String,
        difficulty: String,
        completed: Boolean
    ) {
        this.name = name
        this.description = description
        this.typeInt = typeInt
        this.type = type
        this.difficulty = difficulty
        this.completed = completed
    }

    fun typeImageValue(): Int {
        val stats = arrayOf(
            R.drawable.quest_type_strenght,
            R.drawable.quest_type_dexterity,
            R.drawable.quest_type_constitution,
            R.drawable.quest_type_wisdom,
            R.drawable.quest_type_intelligence,
            R.drawable.quest_type_charisma
        )
        return stats[typeInt]
    }



}