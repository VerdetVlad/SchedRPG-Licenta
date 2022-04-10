package com.vladv.questsched.user

import java.util.*

class Quest {
    // 0 - STR, 1 - DEX, 2 - CON, 3 - WIS, 4 - INT, 5 - CHA
    private var type = 0

    //minutes
    //private int duration;
    // 0 - very easy, 1 - easy, 2 - medium, 3 - hard, 4 - very hard
    private var difficulty = 0
    var name: String? = null
    var description: String? = null

    constructor()
    constructor(
        type: Int,  /*int duration,*/
        difficulty: Int, name: String?, description: String?
    ) {
        this.type = type
        //this.duration = duration;
        this.difficulty = difficulty
        this.name = name
        this.description = description
    }

    constructor(type: Int, difficulty: Int, name: String?) {
        this.type = type
        this.difficulty = difficulty
        this.name = name
        description = ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val task = other as Quest
        return type == task.type && difficulty == task.difficulty && name == task.name && description == task.description
    }

    override fun hashCode(): Int {
        return Objects.hash(type, difficulty, name, description)
    }

    override fun toString(): String {
        return "Task{" +
                "type=" + type +
                ", difficulty=" + difficulty +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}'
    }

    fun dificultyStringValue(): String {
        val difficultyStrings = arrayOf("very easy", "easy", "medium", "hard", "very hard")
        return difficultyStrings[difficulty]
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
        return stats[type]
    }
}