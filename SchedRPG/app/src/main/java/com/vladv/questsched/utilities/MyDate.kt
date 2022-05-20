package com.vladv.questsched.utilities

class MyDate{

    var weekDay: Int? = null
    var day: Int? = null
    var month: Int? = null
    var year: Int? = null

    constructor(weekDay: Int?, day: Int?, month: Int?, year: Int?) {
        this.weekDay = weekDay
        this.day = day
        this.month = month
        this.year = year
    }


    constructor(weekDay: String?, day:String?, month: String?, year: String?)
    {
        this.weekDay = weekDayToInt(weekDay)
        this.day = day!!.toInt()
        this.month = mounthToInt(month)
        this.year = year!!.toInt()
    }

    constructor(str:String?)
    {
        val delimiter1 = "/"
        val delimiter2 = " "
        val delimiter3 = ","
        val words = str!!.split(delimiter1, delimiter3, delimiter2, ignoreCase = true)
        if(words.size == 3) {
            this.weekDay = 0
            this.day = words[0].toInt()
            this.month = words[1].toInt()
            this.year = words[2].toInt()
        }
        else {
            this.weekDay = words[0].toInt()
            this.day = words[1].toInt()
            this.month = words[2].toInt()
            this.year = words[3].toInt()
        }
    }


    override fun toString(): String {
        return "$weekDay $day $month $year"
    }

    fun toStringDate(): String {
        return "$day/$month/$year"
    }

    fun toStringWeekDay(): String {
        return weekDay.toString()
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyDate

        if (weekDay != other.weekDay) return false
        if (day != other.day) return false
        if (month != other.month) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = weekDay ?: 0
        result = 31 * result + (day ?: 0)
        result = 31 * result + (month ?: 0)
        result = 31 * result + (year ?: 0)
        return result
    }

    fun weekDayToInt(weekDay: String?): Int {
        return when (weekDay) {
            "Mon" -> 1
            "Tue" -> 2
            "Wed" -> 3
            "Thu" -> 4
            "Fri" -> 5
            "Sat" -> 6
            "Sun" -> 7
            else -> 0
        }
    }

    fun mounthToInt(weekDay: String?): Int {
        return when (weekDay) {
            "Jan" -> 1
            "Feb" -> 2
            "Mar" -> 3
            "Apr" -> 4
            "May" -> 5
            "Jun" -> 6
            "Jul" -> 7
            "Aug" -> 8
            "Sep" -> 9
            "Oct" -> 10
            "Nov" -> 11
            "Dec" -> 12
            else -> 0
        }
    }





}