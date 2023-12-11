package ymsli.com.couriemate.utils.common

import android.Manifest
import android.app.Application
import android.content.ContentValues
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.provider.CalendarContract
import android.util.Log
import androidx.core.content.ContextCompat
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONException
import org.json.JSONObject
import ymsli.com.couriemate.database.entity.NotificationResponse
import ymsli.com.couriemate.model.TaskRetrievalRequest
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.rx.SchedulerProvider

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @date   JULY 7, 2021
 * @author Balraj VE00YM023
 * Copyright (c) 2021, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * NotificationsHandler : Performs the required operation as specified by the notification
 * packet.
 * Following operation types are handled
 * 1. UPDATE (Updates the order fields)
 * 2. DELETE (Deletes an task, when its unassigned from back end)
 * 3. COMPLETED (Marks the assigned task as completed, so that driver can pick it up)
 * 4. REMINDER (Adds a reminder in calendar)
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

class NotificationsHandler(private val ctx: Application,
                           private val repository: CouriemateRepository,
                           private val schedulerProvider: SchedulerProvider,
                           private val compositeDisposable: CompositeDisposable) {

    private companion object {
        private const val TAG = "NotificationHandler"
        private const val DELETE = "delete"
        private const val UPDATE = "update"
        private const val REQUEST_DATA = "requestData"
        private const val OPERATION = "operation"
        private const val SYNC = "sync"
        private const val COMPLETED = "completed"
        private const val FALSE = "false"
        private const val REMINDER = "reminder"
        private const val KEY_TASK_STATUS = "taskStatus"
        private const val KEY_EVENT_HEADER = "eventHeader"
        private const val KEY_EVENT_BODY = "eventBody"
        private const val REMINDER_TITLE = "Collect payment"
        private const val REMINDER_BODY = "Collect payment for couriemate order"
        private const val TASK_STATUS_DELIVERED = 4
    }

    /**
     * Given a list of notifications, processes all the 'SILENT' notifications which
     * are not processed yet. operation information is contained within the request data.
     * @param notifications list of notifications, can be of any type (SILENT, ALERT)
     * @author Balraj
     */
    fun process(notifications: List<NotificationResponse>){
        notifications.asSequence()
            .filter { it.pushType == Constants.NOTIFICATION_TYPE.SILENT }
            .filter { !repository.isNotificationProcessed(it.notificationId) }
            .forEach { processNotification(it) }
    }

    private fun processNotification(notification: NotificationResponse) {
        try {
            val outerObject = JSONObject(notification.requestData)
            val innerObject = JSONObject(outerObject[REQUEST_DATA].toString())
            when (innerObject[OPERATION]) {
                UPDATE -> performUpdate(innerObject)
                DELETE -> performDelete(innerObject)
                COMPLETED -> performCompleted(innerObject)
                REMINDER -> handleReminder(innerObject)
                else -> { }
            }
            repository.insertProcessedNotification(notification.notificationId)
        } catch (exception: JSONException) {
            Log.d(TAG, "FAILED TO PARSE REQUEST DATA FOR NOTIFICATION_ID : ${notification.notificationId}")
        }
    }


    /******************************** HANDLER FUNCTIONS FOR DIFFERENT OPERATION TYPES *************/

    private fun performUpdate(jsonObject: JSONObject){
        repository.updateOrderDataByFCM(jsonObject)
        if((FALSE != jsonObject[SYNC].toString()) && (repository.getUserName() != null)){
            try{
                performPushAndPull()
            }
            catch(exception: Exception){
                Log.d(TAG, "Push and pull failed")
            }
        }
    }

    private fun performDelete(jsonObject: JSONObject){
        val taskId  = jsonObject["taskId"].toString().toInt()
        val orderId = jsonObject["orderId"].toString().toInt()
        repository.deleteAssignedTokenByFCM(taskId, orderId)
    }

    private fun performCompleted(jsonObject: JSONObject){
        val orderId = jsonObject["orderId"].toString().toInt()
        repository.setTaskPickableByFCM(orderId)
    }

    private fun performPushAndPull(){
        val notSyncedRecords = repository.getNotSyncedRecords()
        compositeDisposable.addAll(repository.syncTasks(notSyncedRecords)
            .subscribeOn(schedulerProvider.io())
            .subscribe({ tasks ->
                val updateSyncList = Utils.getUpdateSyncList(notSyncedRecords.asList(), tasks.asList())
                updateSyncList.forEach{repository.updateSyncStatus(it.taskId!!, true)}
                pullFromRemote()
            }, {
                Log.d(TAG, "Failed to push un synced records to server")
            })
        )
    }

    private fun pullFromRemote(){
        val expectedStart= Utils.getCurrentTimeInServerFormat()
        val updatedOn = repository.getUpdatedOnFromSharedPreference()
        val taskRetrievalRequest = TaskRetrievalRequest(
            driverId = repository.getDriverId(),
            timezoneOffset = Utils.getGMTOffset(),
            source = Constants.SOURCE_MOBILE,
            updatedOn = updatedOn,
            expectedStart = expectedStart)
        compositeDisposable.addAll(repository.getDriverTasks(taskRetrievalRequest)
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                repository.insertDriverTasksInRoom(it)
                val updatedTime = Utils.getCurrentTimeInServerFormat()
                repository.setUpdatedOnInSharedPreference(updatedTime)
            },{
                Log.d(TAG, "Failed to pull driver tasks from remote server")
            })
        )
    }

    private fun handleReminder(jsonObject: JSONObject){
        var taskStatusId = jsonObject[KEY_TASK_STATUS].toString().toInt()
        if(taskStatusId == TASK_STATUS_DELIVERED){
            var title = jsonObject[KEY_EVENT_HEADER]?.toString() ?: REMINDER_TITLE
            var description = jsonObject[KEY_EVENT_BODY]?.toString() ?: REMINDER_BODY
            addReminderToCalender(title, description)
        }
    }

    private fun addReminderToCalender(title: String, description: String){
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CALENDAR)
            == PackageManager.PERMISSION_GRANTED) {
            val date = Calendar.getInstance()
            date.add(Calendar.DATE, 1)
            date.set(Calendar.HOUR, 9)
            date.set(Calendar.AM_PM, Calendar.AM)
            date.set(Calendar.MINUTE, 0)
            date.set(Calendar.SECOND, 0)

            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, date.timeInMillis)
                put(CalendarContract.Events.DTEND, date.timeInMillis + 60 * 60 * 1000)
                put(CalendarContract.Events.TITLE, title)
                put(CalendarContract.Events.DESCRIPTION, description)
                put(CalendarContract.Events.CALENDAR_ID, 3)
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString())
            }
            val eventURI = ctx.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            val reminderValues = ContentValues().apply {
                put(CalendarContract.Reminders.MINUTES, 15)
                put(CalendarContract.Reminders.EVENT_ID, eventURI!!.lastPathSegment)
                put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            }
            ctx.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
        }
    }
}