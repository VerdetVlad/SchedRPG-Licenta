package com.vladv.questsched.user

import com.example.schedrpg.R
import com.vladv.questsched.utilities.MyDate
import java.util.*


class Quest{

    var name: String?=null
    var description: String?=null
    var initialDate: MyDate?=null
    var repeat: Recurrence?=null
    var type: Int?=null// 0 - STR, 1 - DEX, 2 - CON, 3 - WIS, 4 - INT, 5 - CHA
    var difficulty: Int?=null// 0 - very easy, 1 - easy, 2 - medium, 3 - hard, 4 - very hard

    constructor()
    constructor(
        name: String?,
        description: String?,
        initialDate: String?,
        repeat: Recurrence?,
        type: Int?,
        difficulty: Int?
    ) {
        this.name = name
        this.description = description
        this.initialDate = MyDate(initialDate)
        this.repeat = repeat
        this.type = type
        this.difficulty = difficulty
    }


    override fun toString(): String {
        return "Quest{" +
                "type=" + type +
                "initialDate=" + initialDate +
                "repeat=" + repeat.toString() +
                ", difficulty=" + difficulty +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}'
    }

    fun difficultyStringValue(): String {
        val difficultyStrings = arrayOf("very easy", "easy", "medium", "hard", "very hard")
        return difficultyStrings[difficulty!!]
    }

    fun typeStringValue(): String {
        val stats = arrayOf(
            "Strength",
            "Dexterity",
            "Constitution",
            "Wisdom",
            "Intelligence",
            "Charisma"
        )
        return stats[type!!]
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
        return stats[type!!]
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quest

        if (name != other.name) return false
        if (description != other.description) return false
        if (initialDate != other.initialDate) return false
        if (repeat != other.repeat) return false
        if (type != other.type) return false
        if (difficulty != other.difficulty) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (initialDate?.hashCode() ?: 0)
        result = 31 * result + repeat.hashCode()
        result = 31 * result + type!!
        result = 31 * result + difficulty!!
        return result
    }

    private fun getQuestDay(): Int {
        return initialDate?.day!!
    }

    fun validDate(date: MyDate):Boolean
    {

        val currentDate:Calendar = Calendar.getInstance()

        if(!date.compareDates(MyDate(currentDate))) return false
        if(!date.compareDates(initialDate!!)) return false
        if(this.repeat?.untilDate?.compareDates(date) == false) return false
        return  this.initialDate == date ||
                this.repeat?.recurringFrequency == 1 ||
                (this.repeat?.recurringFrequency == 2 && this.repeat?.recurringDays?.get(date.weekDay!!-1) == true) ||
                (this.repeat?.recurringFrequency == 3 && this.getQuestDay() == date.day)
    }

    fun questXpReward():Int
    {
        return 10*(difficulty!!+1)
    }

    fun questXpLoss():Int
    {
        return -5*(difficulty!!+1)
    }

}
