package com.vladv.questsched.user

class User {
    constructor()
    constructor(fullName: String?, email: String?) {
        Companion.fullName = fullName
        Companion.email = email
        Companion.tasks = ArrayList()
    }

    constructor(fullName: String?, email: String?, tasks: ArrayList<UserTask>?) {
        Companion.fullName = fullName
        Companion.email = email
        Companion.tasks = tasks
    }

    fun addTask(task: UserTask) {
        Companion.tasks!!.add(task)
    }

    override fun toString(): String {
        return "User{" +
                "fullName='" + Companion.fullName + '\'' +
                ", email='" + Companion.email + '\'' +
                ", tasks=" + Companion.tasks +
                '}'
    }

    fun removeTask(task: UserTask) {
        Companion.tasks!!.remove(task)
    }

    val fullName: String?
        get() = Companion.fullName
    val email: String?
        get() = Companion.email
    val tasks: ArrayList<UserTask>?
        get() = Companion.tasks

    companion object {
        private var fullName: String? = null
        private var email: String? = null
        private var tasks: ArrayList<UserTask>? = null
    }
}