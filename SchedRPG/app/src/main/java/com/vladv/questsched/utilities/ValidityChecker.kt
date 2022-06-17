package com.vladv.questsched.utilities

import com.vladv.questsched.user.Quest

class ValidityChecker {
    companion object{
        fun validName(name:String):Boolean{
            return true
        }
        fun validEmail(email:String):Boolean{
            return true
        }
        fun validPassword(password:String):Boolean{
            return true
        }
        fun validQuest(quest: Quest):Boolean{
            return true
        }
    }
}