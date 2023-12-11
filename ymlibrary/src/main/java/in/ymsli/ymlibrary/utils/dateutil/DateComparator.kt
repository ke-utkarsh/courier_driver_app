/**
 * Project Name :YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * DateUtils Class : responsible for provding date time format operations

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description
-----------------------------------------------------------------------------------

 */
package `in`.ymsli.ymlibrary.utils.dateutil

import java.util.*
import kotlin.collections.HashMap


class DateComparator {

    /**
     * Checks if the first parameter is before the second parameter in time.
     * @param dateFirst The first date to compare.
     * @param dateSecond The second date to compare.
     * @return True if the first date in time is before the second, if not false.
     */
    fun isBefore(dateFirst: Date, dateSecond: Date): Boolean {
        return dateFirst.compareTo(dateSecond) < 0
    }


    /**
     * Calculate how many days the first time is expired than second time.
     * @param first
     * @param second
     * @return long the days
     */
    fun expireDays(first: Long, second: Long): Long {
        if (first < second)
            return 0

        val delays = first - second
        return delays / (1000 * 60 * 60 * 24)
    }


    /**
     * Calculate difference of days,time, minutes and seconds between two dates
     * @param startDate start date to calculate difference
     * @param endDate end date to calculate difference
     * @return hashmap containing difference of days,hours,minutes,sconds
     */
    fun calculateDifference(startDate: Date, endDate: Date): HashMap<String, Long> {
        var hashMap = calculateDifference(startDate.time, endDate.time)
        return hashMap

    }


    /**
     * Calculate difference of days,time, minutes and seconds between two dates
     * @param startDate start date to calculate difference in millis
     * @param endDate end date to calculate difference in millis
     * @return hashmap containing difference of days,hours,minutes,sconds
     */
    fun calculateDifference(startDatenmllis: Long, endDateinmillis: Long): HashMap<String, Long> {

        var hashMap = HashMap<String, Long>()

        //milliseconds
        var different = endDateinmillis - startDatenmllis
        val elapsedDays = different / DateConstants.daysInMilli

        different %= DateConstants.daysInMilli
        val elapsedHours = different / DateConstants.hoursInMilli

        different %= DateConstants.hoursInMilli
        val elapsedMinutes = different / DateConstants.minutesInMilli

        different %= DateConstants.minutesInMilli
        val elapsedSeconds = different / DateConstants.secondsInMilli

        hashMap.put(DateConstants.KEY_DAYS, elapsedDays) //days
        hashMap.put(DateConstants.KEY_HOURS, elapsedHours) //hours
        hashMap.put(DateConstants.KEY_MINUTES, elapsedMinutes) //minutes
        hashMap.put(DateConstants.KEY_SECONDS, elapsedSeconds) //seconds

        return hashMap

    }


}