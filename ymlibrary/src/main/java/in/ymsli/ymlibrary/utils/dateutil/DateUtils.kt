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

import `in`.ymsli.ymlibrary.config.YMConstants
import `in`.ymsli.ymlibrary.errors.YMExceptions
import `in`.ymsli.ymlibrary.utils.CommonUtils
import android.annotation.TargetApi
import android.os.Build
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR2
import android.text.format.DateFormat
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateUtils {

    /**
     * This method converts Date to given format
     * @param date Date
     * @param format format for required date
     * @param timeZone timezone
     * @return formatted date
     *
     */

    fun formatDateToString(date: Date?, format: String,
                           timeZone: String?): String? {
        try {
            var timeZone = timeZone
            // null check
            if (date == null) return null
            // create SimpleDateFormat object with input format
            val sdf = SimpleDateFormat(format)
            // default system timezone if passed null or empty
            if (timeZone == null || "".equals(timeZone.trim { it <= ' ' }, ignoreCase = true)) {
                timeZone = Calendar.getInstance().timeZone.id
            }
            // set timezone to SimpleDateFormat
            sdf.timeZone = TimeZone.getTimeZone(timeZone)
            // return Date in required format with timezone as String
            return sdf.format(date)
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }

    /**
     * This method converts Date to given format
     * @param date Date
     * @param format formt for required date
     * @return formatted date
     */
  //  @Throws(YMExceptions::class)
    fun formatDateToString(date: Date?, format: String): String? {
        try {
            var timeZone = Calendar.getInstance().timeZone.id

            return formatDateToString(date, format, timeZone)
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }


    /**
     * This method converts Date to given format
     * @param date Date
     * @return formatted date
     */
  //  @Throws(YMExceptions::class)
    fun formatDateToString(date: Date?): String? {
        try {


            var timeZone = Calendar.getInstance().timeZone.id

            return formatDateToString(date, YMConstants.DEFAULT_DATE_FORMAT, timeZone)
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }

    /**
     * This method converts current Timestamp to date as string
     * @param dateFormat required format
     * @return formatted date
     */
  //  @Throws(YMExceptions::class)
    fun formatCurrentTimeStamp(dateFormat: String): String? {
        try {
            var today = Calendar.getInstance().time

            var formatter = SimpleDateFormat(dateFormat)

            var formattedDate = formatter.format(today)

            return formattedDate
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }


    /**
     * This method converts given date in string format
     * @param dateFormat required format
     * @param date to convert
     * @return formatted date
     */
  //  @Throws(YMExceptions::class)
    fun formatTimeStamp(dateFormat: String, date: Date): String? {
        try {
            var formatter = SimpleDateFormat(dateFormat)

            var formattedDate = formatter.format(date)

            return formattedDate
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }


    /**
     * This method converts given date to date in string format
     * @param dateFormat required format
     * @param date in millis
     * @return formatted date
     */
  //  @Throws(YMExceptions::class)
    fun formatTimeStamp(dateFormat: String, date: Long): String? {
        try {
            var formatter = SimpleDateFormat(dateFormat)

            var formattedDate = formatter.format(date)

            return formattedDate
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }


    /**
     * This method converts given date to date in string format
     * @param dateFormat required format
     * @param date in string format
     * @return formatted date
     */
  //  @Throws(YMExceptions::class)
    fun formatTimeStamp(dateFormat: String, date: String): String? {
        try {
            var formatter = SimpleDateFormat(dateFormat)

            var formattedDate = formatter.format(date)

            return formattedDate
        }catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }

    /**
     * This method converts given date to date in string format
     * @param requiredFormat required format
     * @param date in millis
     * @param locale local
     * @return formatted date
     */
  //  @Throws(YMExceptions::class)
    fun formatTimeStamp(locale: Locale, date: Long, requiredFormat: String): String? {
        var dateFormat: SimpleDateFormat?
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                dateFormat = SimpleDateFormat(getDateFormat(locale, requiredFormat))
            } else {
                dateFormat = SimpleDateFormat(requiredFormat)
            }

            return dateFormat.format(Date(date))
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }


    /**
     * This method converts given date to date in string format
     * @param requiredFormat required format
     * @param date in millis
     * @param locale local
     * @return formatted date
     */
    @TargetApi(JELLY_BEAN_MR2)
    fun getDateFormat(locale: Locale, inputFormat: String): String {
        return DateFormat.getBestDateTimePattern(locale, inputFormat)
    }


    /**
     * This method converts given date to date in millis
     * @param dateFormat required format
     * @param date in String
     * @return formatted date
     */
    @Throws(YMExceptions::class)
    fun getTimeinMillis(date: String, dateFormat: String): Long? {
        try {

            val sdf = SimpleDateFormat(dateFormat)
            val newdate = sdf.parse(date)
            val milliseconds: Long = newdate.time
            return milliseconds
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }

    /**
     * This method converts given date to date in millis
     * @param date
     * @return formatted date
     */

    fun getTimeinMillis(date: Date): Long ?{
        try {

            val milliseconds: Long = date.time
            return milliseconds
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            CommonUtils.showLog(Log.ERROR,parseException.message)
            // throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)
        } catch (exception: Exception) {
            exception.printStackTrace()
            CommonUtils.showLog(Log.ERROR,exception.message)
            //  throw YMExceptions(YMErrorCode.PARSING_DAT_TIME_FORMAT, e.message)


        }
        return null
    }


    /**
     * To get Calender Instance
     * @return calender instance
     */
    fun getCalendarInstance(): Calendar {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = Date()
        return calendar
    }

    /**
     * To get Calender instance based on given date
     * @return calender object
     * @param date date in milliseconds
     */
    fun getCalendarInstance(date: Long?): Calendar {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = Date(date!!)
        return calendar
    }


    /**
     * getcurrentdate
     */
   fun getDate(date: Long?): Int {
        val calendar: Calendar=getCalendarInstance(date)
      return calendar.get(Calendar.DAY_OF_MONTH); //Day of the month :)

    }

    /**
     * getMinutes
     */
    fun getMinutes(date: Long?): Int {
        val calendar: Calendar=getCalendarInstance(date)
        return calendar.get(Calendar.MINUTE);
    }


    /**
     * getSeconds
     */
    fun getSeconds(date: Long?): Int {
        val calendar: Calendar=getCalendarInstance(date)
        return calendar.get(Calendar.SECOND);
    }

    /**
     * getMonth
     */
    fun getMonth(date: Long?): Int {
        val calendar: Calendar=getCalendarInstance(date)
        return calendar.get(Calendar.MONTH);
    }

    /**
     * getYear
     */
    fun getYear(date: Long?): Int {
        val calendar: Calendar=getCalendarInstance(date)
        return calendar.get(Calendar.YEAR);
    }



    /**
     * getcurrentdate
     */
    fun getDate(): Int {
        val calendar: Calendar=getCalendarInstance()
        return calendar.get(Calendar.DAY_OF_MONTH); //Day of the month :)

    }

    /**
     * getMinutes
     */
    fun getMinutes(): Int {
        val calendar: Calendar=getCalendarInstance()
        return calendar.get(Calendar.MINUTE);
    }


    /**
     * getSeconds
     */
    fun getSeconds(): Int {
        val calendar: Calendar=getCalendarInstance()
        return calendar.get(Calendar.SECOND);
    }

    /**
     * getMonth
     */
    fun getMonth(): Int {
        val calendar: Calendar=getCalendarInstance()
        return calendar.get(Calendar.MONTH);
    }

    /**
     * getYear
     */
    fun getYear(): Int {
        val calendar: Calendar=getCalendarInstance()
        return calendar.get(Calendar.YEAR);
    }

}