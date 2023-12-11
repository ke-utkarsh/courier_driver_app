package ymsli.com.couriemate.utils.services

import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerServiceComponent
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.di.module.FCMServiceModule
import ymsli.com.couriemate.model.TaskRetrievalRequest
import ymsli.com.couriemate.model.UserTokenMapping
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Utils
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.provider.CalendarContract
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * FCMService : triggered using FCM and responsible for notification in the device
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var couriemateRepository: CouriemateRepository

    companion object {
        private const val TAG = "CouriemateFCMService"
        private const val DELETE = "delete"
        private const val UPDATE = "update"
        private const val REQUEST_DATA = "requestData"
        private const val OPERATION = "operation"
        private const val SYNC = "sync"
        private const val COMPLETED = "completed"
        private const val REMINDER = "reminder"
        private const val KEY_NOTIFICATION_ID = "notificationId"
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised.
     */
    override fun onNewToken(token: String) {
        injectDependencies().inject(this)
        couriemateRepository.updateFCMToken(token)
        sendRegistrationToServer(token)
    }

    /**
     * callback method which triggers when
     * notification is pushed in the device
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        injectDependencies().inject(this)
        remoteMessage?.data?.isNotEmpty()?.let {
            val jsonObject = JSONObject(remoteMessage?.data[REQUEST_DATA])

            /* If notification is already processed (may be using notifications API) then return */
            val notificationId = jsonObject[KEY_NOTIFICATION_ID]?.toString()?.toLong() ?: return
            val alreadyProcessed = couriemateRepository.isNotificationProcessed(notificationId)
            if(alreadyProcessed) { return }

            /* Only continue if notification is not already processed */
            when (jsonObject[OPERATION]) {
                UPDATE -> operateAndMarkProcessed(notificationId){ performUpdate(jsonObject) }
                DELETE -> operateAndMarkProcessed(notificationId){ performDelete(jsonObject) }
                COMPLETED -> operateAndMarkProcessed(notificationId){ performCompleted(jsonObject) }
                REMINDER -> operateAndMarkProcessed(notificationId){ handleReminder(jsonObject) }
                else -> {}
            }
        }
    }

    private fun performUpdate(jsonObject: JSONObject){
        couriemateRepository.updateOrderDataByFCM(jsonObject)
        if((jsonObject[SYNC].toString() != "false") && (couriemateRepository.getUserName() != null)){
            updateActiveNotificationCount()
            try{
                pushToRemote()
                pullFromRemote()
            }
            catch(exception: Exception){
                Log.d("FCM Service Sync", "Failed")
            }
        }
    }

    private fun performDelete(jsonObject: JSONObject){
        var taskId  = jsonObject["taskId"].toString().toInt()
        var orderId = jsonObject["orderId"].toString().toInt()
        couriemateRepository.deleteAssignedTokenByFCM(taskId, orderId)
    }

    private fun performCompleted(jsonObject: JSONObject){
        updateActiveNotificationCount()
        var orderId = jsonObject["orderId"].toString().toInt()
        couriemateRepository.setTaskPickableByFCM(orderId)
    }

    private fun handleReminder(jsonObject: JSONObject){
        updateActiveNotificationCount()
        var taskStatusId = jsonObject["taskStatus"].toString().toInt()
        if(taskStatusId == 4){
            var title = jsonObject["eventHeader"]?.toString()?:"Collect payment"
            var description = jsonObject["eventBody"]?.toString()?:"Collect payment for couriemate order"
            addReminderToCalender(title, description)
        }
    }

    private fun sendRegistrationToServer(token: String) {
        var userName = couriemateRepository.getUserName()
        if(!userName.isNullOrEmpty()){
            var request = UserTokenMapping(
                deviceToken = token,
                userName = userName
            )
            couriemateRepository.updateFCMTokenOnServer(request).blockingGet()
        }
    }

    private fun pushToRemote(){
        var notSyncedRecords = couriemateRepository.getNotSyncedRecords()
        var result = couriemateRepository.syncTasks(notSyncedRecords).blockingGet()
        var updateSyncList = Utils.getUpdateSyncList(notSyncedRecords.asList() ,result.asList())
        updateSyncList.forEach{couriemateRepository.updateSyncStatus(it.taskId!!, true)}
    }

    private fun pullFromRemote(){
        val expectedStart= Utils.getCurrentTimeInServerFormat()
        var updatedOn = couriemateRepository.getUpdatedOnFromSharedPreference()
        val taskRetrievalRequest = TaskRetrievalRequest(
            driverId = couriemateRepository.getDriverId(),
            timezoneOffset = Utils.getGMTOffset(),
            source = Constants.SOURCE_MOBILE,
            updatedOn = updatedOn,
            expectedStart = expectedStart
        )
        var result = couriemateRepository.getDriverTasks(taskRetrievalRequest).blockingGet()
        couriemateRepository.insertDriverTasksInRoom(result)
        val updatedTime = Utils.getCurrentTimeInServerFormat()
        couriemateRepository.setUpdatedOnInSharedPreference(updatedTime)
    }

    private fun updateActiveNotificationCount(){
        couriemateRepository.getActiveNotificationCount()?.let{
            var currentCount = it.toInt()
            couriemateRepository.setActiveNotificationCount((currentCount+1).toString())
        }
    }

    private fun addReminderToCalender(title: String, description: String){
        if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.WRITE_CALENDAR)
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
            val eventURI = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            val reminderValues = ContentValues().apply {
                put(CalendarContract.Reminders.MINUTES, 15)
                put(CalendarContract.Reminders.EVENT_ID, eventURI!!.lastPathSegment)
                put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            }
            contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
        }
    }

    private fun injectDependencies(): ServiceComponent =
        DaggerServiceComponent
            .builder()
            .applicationComponent((application as CourieMateApplication).applicationComponent)
            .fCMServiceModule(FCMServiceModule(this))
            .build()

    /**
     * Perform the given operation (UPDATE, DELETE, COMPLETE, REMINDER) and then
     * mark the given notification processed.
     * @author BALRAJ VE00YM023
     */
    private fun operateAndMarkProcessed(notificationId: Long, operation: ()->Unit){
        operation()
        couriemateRepository.insertProcessedNotification(notificationId)
    }
}