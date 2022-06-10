package com.vladv.questsched.user

class CharacterStats {
    var level:Int? = null
    var statsLvl:ArrayList<Int> = ArrayList(6)
    var statsCurrXP:ArrayList<Int> = ArrayList(6)
    var statsMaxXP:ArrayList<Int> = ArrayList(6)

    constructor()
    constructor(
        level: Int?,
        statsLvl: ArrayList<Int>,
        statsCurrXP: ArrayList<Int>,
        statsMaxXP: ArrayList<Int>
    ) {
        this.level = level
        this.statsLvl = statsLvl
        this.statsCurrXP = statsCurrXP
        this.statsMaxXP = statsMaxXP
    }


    //-1 lvl down, 0 - same lvl, 1 - lvl up
    fun changeXP(statChanged:Int,auxXP:Int):Int
    {
        var currXP = statsCurrXP[statChanged]
        val maxXP = statsMaxXP[statChanged]

        currXP = currXP.plus(auxXP)
        return when {
            currXP >= maxXP -> {
                currXP -= maxXP
                statsCurrXP[statChanged]=currXP
                lvlUP(statChanged)
                1
            }
            currXP <=0 -> {
                if(statsLvl[statChanged]>1){
                    lvlDown(statChanged)
                    currXP += statsMaxXP[statChanged]
                    statsCurrXP[statChanged]=currXP
                }
                else{
                    statsCurrXP[statChanged]=0
                }
                -1
            }
            else -> {
                statsCurrXP[statChanged]=currXP
                0
            }
        }

    }

    private fun lvlUP(statChanged: Int)
    {
        level = level?.plus(1)
        statsMaxXP[statChanged] = (statsMaxXP[statChanged] * 2)
        statsLvl[statChanged] +=1

    }

    private fun lvlDown(statChanged: Int)
    {

        level = level?.plus(-1)
        statsMaxXP[statChanged] = (statsMaxXP[statChanged] / 2)
        statsLvl[statChanged] -=1

    }

    override fun toString(): String {
        return "CharacterStats(level=$level, statsLvl=$statsLvl, statsCurrXP=$statsCurrXP, statsMaxXP=$statsMaxXP)"
    }


}
