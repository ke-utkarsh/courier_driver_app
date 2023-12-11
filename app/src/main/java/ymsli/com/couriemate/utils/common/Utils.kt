package ymsli.com.couriemate.utils.common

import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Environment
import android.widget.ProgressBar
import org.json.JSONArray
import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.database.entity.XMPPConfig
import ymsli.com.couriemate.model.DropDownEntity
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * Utils : utils used throughtout the app
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
object Utils {

    private val dateFormatter = java.text.SimpleDateFormat(Constants.FORMAT_COURIEMATE_DATE)
    private val currentDateFormmater = java.text.SimpleDateFormat("yyyy-MM-dd")
    private val notificationTimeFormmater =
        java.text.SimpleDateFormat(Constants.NOTIFICATION_TIME_FORMAT)
    private const val TIME_ZONE_KEY_GMT = "GMT"
    private const val DAP_IOT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    var sortedAssingedTasks: ArrayList<TaskRetrievalResponse>? = null
    private const val TRIP_TIME_FORMAT = "hh:mm a"

    /**
     * sends time to API in required format
     */
    fun getCurrentTimeInServerFormat(): String {
        var tempTime = Timestamp(Date().time).toString()
        tempTime = tempTime.replace(" ", "T")
        return tempTime + getTimeZoneOffset()
    }

    /**
     * used to animate progress over the UI
     */
    fun animateProgressBar(progressBar: ProgressBar, duration: Long, progress: Int) {
        ObjectAnimator.ofInt(progressBar, "progress", progress)
            .setDuration(duration)
            .start()
    }

    /**
     * formats time to server format
     */
    fun formatTimestampToServerFormat(timestamp: Timestamp): String {
        return timestamp.toString().replace(" ", "T") + getTimeZoneOffset()
    }

    /**
     * returns difference between local time & GMT
     */
    fun getTimeZoneOffset(): String {
        val rawOffset = ((TimeZone.getDefault().rawOffset) / 1000) / 60
        val hourOffset = rawOffset / 60
        val minuteOffset = rawOffset % 60
        var hourOffsetString = hourOffset.toString()
        var minuteOffsetString = minuteOffset.toString()

        if (hourOffset >= 0) hourOffsetString = ("+" + hourOffset)
        if (hourOffsetString.length < 3) hourOffsetString =
            (hourOffsetString.substring(0, 1) + "0" + hourOffsetString.substring(
                1,
                2
            ))
        if (minuteOffsetString.length < 2) minuteOffsetString = ("0" + minuteOffsetString)
        return hourOffsetString + minuteOffsetString
    }

    /**
     * formats data for filter UI
     */
    fun formatDateForFilterDialog(timestamp: Timestamp): String {
        return SimpleDateFormat("YYYY/MM/dd").format(timestamp)
    }

    /**
     * filters erroneous tasks from task list
     */
    fun getUpdateSyncList(
        updateList: List<TaskRetrievalResponse>,
        errorList: List<TaskRetrievalResponse>
    ): List<TaskRetrievalResponse> {
        val map = HashMap<Long, TaskRetrievalResponse>()
        for (task in updateList) map[task.taskId!!] = task
        for (task in errorList) map.remove(task.taskId)
        return map.values.toList()
    }

    /**
     * returns time offset of GMT
     */
    fun getGMTOffset(): String {
        return Date().toString().substring(Date().toString().indexOf("GMT")).substring(3, 9)
    }


    /**
     * Given a timestamp object this method returns a string representation of date formatted in couriemate format
     * timestamp object to be formatted
     * @author Balraj
     */
    fun formatDate(dateString: String): String {
        val timestamp = Timestamp.valueOf(getDateTimeWithoutTimeZone(dateString))
        val timeZone = getTimeZoneOffset().replace("+", "") //getTimeZone(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = timestamp
        adjustCalenderForTimeZone(calendar, timeZone)
        val formattedDate = dateFormatter.format(calendar.time)
        val indexOfFirstSpace = formattedDate.indexOf(" ")
        val dayOfMonth = Integer.parseInt(formattedDate.substring(0, indexOfFirstSpace))
        val daySuffix = getDaySuffix(dayOfMonth)
        return dayOfMonth.toString() + daySuffix + formattedDate.substring(indexOfFirstSpace)
    }

    /**
     * Helper method to format date.
     * given a day of month it returns the appropriate suffix for that particular day.
     * @param dayOfMonth day of month for which a suffix is required
     * @return day suffix which can be any one of the {st, nd, rd, th}
     * @author Balraj VE00YM023
     */
    private fun getDaySuffix(dayOfMonth: Int): String {
        when (dayOfMonth) {
            1 -> return Constants.DAY_1_SUFFIX
            2 -> return Constants.DAY_2_SUFFIX
            3 -> return Constants.DAY_3_SUFFIX
        }
        return Constants.DAY_DEFAULT_SUFFIX
    }

    /**
     * retrieves current data time with no offset
     */
    private fun getDateTimeWithoutTimeZone(dateString: String): String {
        return dateString.replace("T", " ")
            .substring(0, dateString.indexOf("+"))
    }

    /**
     * returns local time zone
     */
    private fun getTimeZone(dateString: String): String {
        return dateString.substring(dateString.indexOf("+") + 1)
    }

    /**
     * used to show local time, the GMT time is manipulated
     * based on local timezone
     */
    private fun adjustCalenderForTimeZone(calendar: Calendar, timeZone: String) {
        if (isUTC(timeZone)) {
            val localOffset = getTimeZoneOffset().substring(1)
            val hours =
                if (localOffset[0] == '0') localOffset.substring(1, 2) else localOffset.substring(
                    0,
                    2
                )
            val minutes = localOffset.substring(2)
            calendar.add(Calendar.HOUR, hours.toInt())
            calendar.add(Calendar.MINUTE, minutes.toInt())
        }
    }

    private fun isUTC(timeZone: String): Boolean {
        return timeZone == "0000"
    }

    /**
     * This method is used to convert currency values to couriemate format (12,123.12)
     * @param value currency value to be formatted
     * @author Balraj
     */
    fun formatCurrencyValue(value: Double): String {
        if (value.equals(0.0)) {
            return "0.00"
        }
        val formatter = DecimalFormat(Constants.FORMAT_COURIEMATE_CURRENCY)
        return formatter.format(value)
    }

    /**
     * Given a timestamp object this method returns a string representation of date formatted in couriemate format
     * @param timestamp timestamp object to be formatted
     * @author Balraj
     */
    fun formatDate(timestamp: Timestamp): String {
        val formattedDate = dateFormatter.format(timestamp)
        val indexOfFirstSpace = formattedDate.indexOf(" ")
        val dayOfMonth = Integer.parseInt(formattedDate.substring(0, indexOfFirstSpace))
        val daySuffix = getDaySuffix(dayOfMonth)
        return dayOfMonth.toString() + daySuffix + formattedDate.substring(indexOfFirstSpace)

    }

    fun getCurrentDatePrefix(): String = currentDateFormmater.format(Date()) + "%"

    /**
     * returns timestamp of older day
     * passed as parameter in method
     */
    fun getPreviousDayTimeStamp(anotherDay: Int, isPast: Boolean): Timestamp {
        val cal = Calendar.getInstance()
        if (isPast) cal.add(Calendar.DATE, -anotherDay)
        else cal.add(Calendar.DATE, anotherDay)

        val s = SimpleDateFormat("yyyy-MM-dd")
        return Timestamp((SimpleDateFormat("yyyy-MM-dd").parse(s.format(Date(cal.timeInMillis)))).time)
    }

    /**
     * used to filter tasks which
     * lie between particular dates
     */
    fun getTasksBetweenDuration(
        tasks: ArrayList<TaskRetrievalResponse>,
        fromDate: Timestamp?,
        toDate: Timestamp?
    ): List<TaskRetrievalResponse> {
        val recentList: List<TaskRetrievalResponse> = ArrayList<TaskRetrievalResponse>()
        for (t in tasks) {
            val taskEndDate = t.endDate
            val temp = getDateTimeWithoutTimeZone(taskEndDate!!)
            val taskTime = (SimpleDateFormat("yyyy-MM-dd hh:mm").parse(temp))
            if (taskTime.after(fromDate) && (taskTime.before(toDate))) {
                (recentList as java.util.ArrayList).add(t)
            }
        }
        val sortedDoneTasks = recentList.sortedWith(ViewUtils.taskListComparatorEndDate)
        return sortedDoneTasks
    }

    /**
     * used to filter task history which
     * lie between particular dates
     */
    fun getTasksHistoryBetweenDuration(
        tasks: ArrayList<TaskHistoryResponse>,
        fromDate: Timestamp?,
        toDate: Timestamp?
    ): List<TaskHistoryResponse> {
        val recentList: List<TaskHistoryResponse> = ArrayList<TaskHistoryResponse>()
        for (t in tasks) {
            val taskEndDate = t.endDate
            val temp = getDateTimeWithoutTimeZone(taskEndDate)
            val taskTime = (SimpleDateFormat("yyyy-MM-dd hh:mm").parse(temp))
            if (taskTime.after(fromDate) && (taskTime.before(toDate))) {
                (recentList as java.util.ArrayList).add(t)
            }
        }
        val sortedDoneTasks = recentList.sortedWith(ViewUtils.taskHistoryComparatorEndDate)
        return sortedDoneTasks
    }

    /**
     * formats given data as YYYY/MM/dd
     */
    fun formatDateForListItem(dateString: String): String {
        val timestamp = Timestamp.valueOf(getDateTimeWithoutTimeZone(dateString))
        val timeZone = getTimeZone(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = timestamp
        adjustCalenderForTimeZone(calendar, timeZone)
        return SimpleDateFormat("YYYY/MM/dd").format(calendar.time)
    }

    /**
     * formats time for device notifications
     */
    fun formatTimeForNotification(timeStamp: String): String {
        val timestamp = Timestamp.valueOf(getDateTimeWithoutTimeZone(timeStamp))
        val timeZone = getTimeZoneOffset().replace("+", "")
        val calendar = Calendar.getInstance()
        calendar.time = timestamp
        adjustCalenderForTimeZone(calendar, timeZone)
        return notificationTimeFormmater.format(calendar.time)
    }

    /**
     * Puts item in spinner when task refuse fragment is active
     */
    fun getDefaultRefuseReasonData() = ArrayList<DropDownEntity>().apply {
        add(DropDownEntity(1, "No Money"))
        add(DropDownEntity(2, "Item not needed anymore"))
        add(DropDownEntity(3, "Wrong item"))
        add(DropDownEntity(4, "Doubt item"))
        add(DropDownEntity(5, "Others"))
    }

    /**
     * Puts item in spinner when task delivery fragment is active
     */
    fun getDefaultMobileMoneyTypes() = ArrayList<DropDownEntity>().apply {
        add(DropDownEntity(1, "MTN"))
        add(DropDownEntity(2, "Airtel"))
    }

    /** returns time in milliseconds in GMT Zone */
    fun getTimeInMilliSec(): Long {
        val cal = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE_KEY_GMT))
        return cal.timeInMillis
    }

    /**
     * returns current time in format
     * required to upload file to
     * DAPIoT server
     */
    fun getTimeInDAPIoTFormat(): String {
        val formatter = java.text.SimpleDateFormat(DAP_IOT_DATE_TIME_FORMAT)
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date())
    }

    /** returns time in DAPIoT format specified in DAPIoT document */
    fun getTimeForFileName(fileNameTime: String): String? {
        val formatter =
            android.icu.text.SimpleDateFormat(DAP_IOT_DATE_TIME_FORMAT)
        val formatterNew =
            android.icu.text.SimpleDateFormat("YYYYMMddHHmmssSSS")
        try {
            return formatterNew.format(formatter.parse(fileNameTime))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /** Returns the date part from the given date time instance. */
    fun getDeliveryTime(millis: Long): String {
        return try {
            java.text.SimpleDateFormat(TRIP_TIME_FORMAT).format(millis)
        } catch (cause: Exception) {
            Constants.NA_KEY
        }
    }

    fun getDateTimeForTxHistory(inputDate: String): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(inputDate)
        return java.text.SimpleDateFormat("hh:mm a").format(date)
    }

    fun getTimestampForDate(): String {
        val calendar: Calendar = GregorianCalendar()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        val d2 = calendar.time
        return d2.time.toString()
    }

    fun formatTimestampForUI(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd h:mm a")
        return formatter.format(millis)
    }
    fun writeToFile(data: String) {
        var logFile: File
        if (Build.VERSION.SDK_INT >= 30) {
            logFile =
                File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}/CouriemateLocationLogsProdBuild")
        } else {
            logFile = File("/sdcard/couriemateLogs.txt")
        }
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append(data)
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}

fun String.parseJsonForSpinnerAdapter(): ArrayList<DropDownEntity> {
    var dataList = ArrayList<DropDownEntity>()
    var jsonArray = JSONArray(this)
    for (i in 0 until jsonArray.length()) {
        var jsonObject = jsonArray.getJSONObject(i)
        var code = jsonObject["codeValue"].toString().toInt()
        var value = jsonObject["dataValue"].toString()
        dataList.add(DropDownEntity(code, value))
    }
    return dataList
}

/**
 * Converts values received in format '2020-02-26T17:34:27.071+0000'
 * to '2020-02-26 17:34:27.071'
 * @author Balraj VE00YM023
 */
fun String.toLastSync(): String? {
    if (this.isEmpty() || this.isBlank()) return null
    val indexOfPlus = this.indexOf("+")
    if (indexOfPlus < 0) return null
    return this.substring(0, indexOfPlus).replace("T", " ")
}

/**
 * Checks if the this XMPPConfig object contains valid XMPP configuration details.
 * @author Balraj VE00YM023
 * @return true if XMPP config related properties are not null and empty, false otherwise
 */
fun XMPPConfig?.isValid(): Boolean {
    return (this != null &&
            userName.isNotEmpty() &&
            password.isNotEmpty() &&
            roomName.isNotEmpty() &&
            serverDomain.isNotEmpty())
}







