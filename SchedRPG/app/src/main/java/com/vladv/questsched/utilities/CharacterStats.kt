package com.vladv.questsched.utilities

class CharacterStats {
    var level:Int? = null
    var currXP:Int? = null
    var maxXP:Int? = null
    var stats:ArrayList<Int> = ArrayList(6)

    constructor()
    constructor(level: Int?, currXP: Int?, maxXP: Int?, stats: ArrayList<Int>) {
        this.level = level
        this.currXP = currXP
        this.maxXP = maxXP
        this.stats = stats
    }

    //-1 lvl down, 0 - same lvl, 1 - lvl up
    fun changeXP(auxXP:Int):Int
    {
        currXP = currXP?.plus(auxXP)
        if(currXP!! >= maxXP!!)
        {
            currXP = currXP!! - maxXP!!
            lvlUP()
            return 1
        }
        else if (currXP!! <=0)
        {
            lvlDown()
            currXP = maxXP!! - currXP!!
            return -1
        }
        else return 0

    }

    private fun lvlUP()
    {
        level = level?.plus(1)
        maxXP =maxXP!! * 2
        for(i in 0 until stats.size) stats[i]= stats[i] +1

    }

    private fun lvlDown()
    {
        level = level?.plus(-1)
        maxXP =maxXP!! / 2
        for(i in 0 until stats.size-1) stats[i]= stats[i] -1

    }

    override fun toString(): String {
        return "CharacterStats(level=$level, currXP=$currXP, maxXP=$maxXP, stats=$stats)"
    }


}
