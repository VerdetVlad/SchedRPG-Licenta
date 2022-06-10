package com.vladv.questsched.utilities

import java.time.YearMonth
import java.util.*

class MyDate{

    var weekDay: Int? = null
    var day: Int? = null
    var month: Int? = null
    var year: Int? = null

    constructor()

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
        this.month = monthToInt(month)
        this.year = year!!.toInt()
    }

    constructor(str:String)
    {
        val delimiter1 = "/"
        val delimiter2 = " "
        val delimiter3 = ","
        val words = str.split(delimiter1, delimiter3, delimiter2, ignoreCase = true)
        when (words.size) {
            3 -> {
                this.weekDay = 0
                this.day = words[0].toInt()
                this.month = words[1].toInt()
                this.year = words[2].toInt()
            }
            6 -> {
                this.weekDay = words[0].toInt()
                this.day = words[2].toInt()
                this.month = words[1].toInt()
                this.year = words[5].toInt()
            }
            else -> {
                this.weekDay = words[0].toInt()
                this.day = words[1].toInt()
                this.month = words[2].toInt()
                this.year = words[3].toInt()
            }
        }
    }

    constructor(c:Calendar)
    {
        weekDay = myDayOfWeek(c[Calendar.DAY_OF_WEEK])
        year = c[Calendar.YEAR]
        month = c[Calendar.MONTH] + 1
        day = c[Calendar.DAY_OF_MONTH]

    }

    override fun toString(): String {
        return "$weekDay $day $month $year"
    }

    fun toStringDate(): String {
        val auxDay:String = if(day!! <10) "0$day"
                            else day.toString()
        val auxMonth:String = if(month!! <10) "0$month"
                              else month.toString()

        return "$auxDay/$auxMonth/$year"
    }

    fun toStringDateKey(): String {
        val auxDay:String = if(day!! <10) "0$day"
        else day.toString()
        val auxMonth:String = if(month!! <10) "0$month"
        else month.toString()

        return "$year $auxMonth $auxDay"
    }

    fun toStringWeekDay(): String {
        return weekDay.toString()
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyDate

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

    private fun weekDayToInt(weekDay: String?): Int {
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

    private fun monthToInt(weekDay: String?): Int {
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

    //true if this>= other
    fun compareDates( other:MyDate):Boolean
    {
        if(this == other) return true
        return when {
            this.year!! > other.year!! -> true
            this.year!! < other.year!! -> false
            else -> {
                when {
                    this.month!! > other.month!! -> true
                    this.month!! < other.month!! -> false
                    else -> {
                        this.day!! >= other.day!!
                    }
                }

            }
        }
    }


    fun increaseDayByOne() {

        val yearMonthObject: YearMonth = YearMonth.of(year!!, month!!)
        val daysInMonth: Int = yearMonthObject.lengthOfMonth()

        day = day?.plus(1)
        if(day!! >daysInMonth) {
            day = 1;
            month = month?.plus(1)
            if(month!!>12){
                month = 1
                year = year?.plus(1)
            }
        }


        weekDay = if(weekDay==7) 1
                  else weekDay?.plus(1)
    }

    fun increaseDayByOne(monthDays: Int) {
        day = day?.plus(1)
        if(day!! >monthDays) {
            day = 1;
            month = month?.plus(1)
            if(month!!>12){
                month = 1
                year = year?.plus(1)
            }
        }


        weekDay = if(weekDay==7) 1
        else weekDay?.plus(1)
    }




    private fun myDayOfWeek(dayOfWeek:Int) :Int
    {
        return if(dayOfWeek == 1) 7
        else (dayOfWeek - 1)
    }

    fun todayCheck(): Boolean {

        val calendar = Calendar.getInstance()
        val date = MyDate(calendar)
        return this == date
    }
}