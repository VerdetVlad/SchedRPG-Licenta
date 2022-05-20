package com.vladv.questsched.user

import com.vladv.questsched.utilities.Quest

class User {

    companion object {
        private var fullName: String? = null
        private var email: String? = null
        private var quests: ArrayList<Quest>? = null
    }

    val fullName: String?
        get() = Companion.fullName
    val email: String?
        get() = Companion.email
    val quests: ArrayList<Quest>?
        get() = Companion.quests

    constructor()
    constructor(fullName: String?, email: String?) {
        Companion.fullName = fullName
        Companion.email = email
        Companion.quests = ArrayList()
    }

    constructor(fullName: String?, email: String?, quests: ArrayList<Quest>?) {
        Companion.fullName = fullName
        Companion.email = email
        Companion.quests = quests
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
                ", quests=" + Companion.quests +
                '}'
    }





}