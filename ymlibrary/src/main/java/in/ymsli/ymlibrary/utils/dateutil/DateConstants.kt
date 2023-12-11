/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 18, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 *
 * DateConstants : This class contains all constants value for DateUtilities
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.utils.dateutil

class DateConstants {


    companion object {


        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24


        val KEY_DAYS: String = "days" //key for  days value
        val KEY_HOURS: String = "hours" //key for  hours values
        val KEY_MINUTES: String = "minutes" //key for minutes values
        val KEY_SECONDS: String = "seconds" //key for seconds values
    }


}