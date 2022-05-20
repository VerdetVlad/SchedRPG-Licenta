package com.vladv.questsched.user

import java.util.*


class Quest{

    var name: String?=null
    var description: String?=null
    var initialDate: String?=null
    var repeat: RecurringQuest?=null
    var type: Int?=null// 0 - STR, 1 - DEX, 2 - CON, 3 - WIS, 4 - INT, 5 - CHA
    var difficulty: Int?=null// 0 - very easy, 1 - easy, 2 - medium, 3 - hard, 4 - very hard

    constructor()
    constructor(
        name: String?,
        description: String?,
        initialDate: String?,
        repeat: RecurringQuest?,
        type: Int?,
        difficulty: Int?
    ) {
        this.name = name
        this.description = description
        this.initialDate = initialDate
        this.repeat = repeat
        this.type = type
        this.difficulty = difficulty
    }


    override fun toString(): String {
        return "Quest{" +
                "type=" + type +
                "initialDate=" + initialDate +
                "repate=" + repeat.toString() +
                ", difficulty=" + difficulty +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}'
    }

    fun dificultyStringValue(): String {
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
}
