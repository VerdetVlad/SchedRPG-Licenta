package com.vladv.questsched.utilities

class Recurrence{

    //0 - dont repeat; 1 - every day; 2 - every week; 3 - every month
    var recurringFrequency:Int? = null
    var recurringDays:ArrayList<Boolean>? = ArrayList(7)
    var untilDate: MyDate? = null


    constructor()

    constructor(recurringFrequency: Int?, recurringDays: ArrayList<Boolean>?, untilDate: MyDate?) {
        this.recurringFrequency = recurringFrequency
        this.recurringDays = recurringDays
        this.untilDate = untilDate
    }

    constructor(str:String)
    {
        val words = str.split(" ", ignoreCase = true)
        recurringFrequency = words[0].toInt()
        for(i in 1..7)
            recurringDays?.set(i,
                words[i+1]=="1"
            )
        untilDate = MyDate(words[8].toInt(),
            words[9].toInt(),
            words[10].toInt(),
            words[11].toInt())
    }


    fun setWeekDays(mon: Boolean,tue: Boolean,wen: Boolean,thr: Boolean,fri: Boolean,sat: Boolean,sun: Boolean)
    {
        recurringDays?.set(0, mon)
        recurringDays?.set(1, tue)
        recurringDays?.set(2, wen)
        recurringDays?.set(3, thr)
        recurringDays?.set(4, fri)
        recurringDays?.set(5, sat)
        recurringDays?.set(6, sun)

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recurrence

        if (recurringFrequency != other.recurringFrequency) return false
        if (recurringDays != other.recurringDays) return false
        if (untilDate != other.untilDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recurringFrequency ?: 0
        result = 31 * result + (recurringDays?.hashCode() ?: 0)
        return result
    }


    fun frequencyToString():String
    {
        val frequency = arrayOf(
            "Never",
            "Daily",
            "Weekly",
            "Monthly"
        )
        return frequency[recurringFrequency!!]
    }

    fun recurringDaysToString():String
    {
        var str = ""
        val days = arrayOf(
            "Mon",
            "Tue",
            "Wen",
            "Thur",
            "Fri",
            "Sat",
            "Sun"
        )
        for(i in 0..6) str+=if(recurringDays?.get(i) == true) (days[i] + " ") else ""

        return str
    }

    override fun toString(): String {
        var str:String = ""
        str += recurringFrequency
        for(i in 0..6) str+=if(recurringDays?.get(i) == true) " 1" else " 0"
        str+= " " + untilDate.toString()

        return str
    }


}