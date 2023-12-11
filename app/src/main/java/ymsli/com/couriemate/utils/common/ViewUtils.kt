package ymsli.com.couriemate.utils.common

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseActivity
import ymsli.com.couriemate.database.entity.*
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.services.TransactionConfigWorkManager
import ymsli.com.couriemate.utils.services.TripConfigWorkManager
import ymsli.com.couriemate.utils.services.ZipUploadService
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Comparator

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewUtils : used for task sorting/filtering
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
object ViewUtils {
    private val timeStampFormatter = android.icu.text.SimpleDateFormat(Constants.COURIEMATE_TIMESTAMP_FORMAT)

    //Trip parameters
    private const val MAX_GEO_CODER_RESULTS = 1
    private const val TAG_ACCEL_SENSOR = "AccelSensorvalues"
    private const val TAG_GYRO_SENSOR = "GyroSensorvalues"
    private const val ZIP_UPLOAD_SERVICE_JOB_ID = 56

    /**
     * Used to sort the task list and return task list.
     * it sorts the data by taskStatusId and then by updatedOn.
     * if updated on field of some task is null(its has not been updated yet)
     * then createdOn field's value is used as updatedOn
     *
     * @author Balraj VE00YM023
     */
    val taskListComparator = Comparator<TaskRetrievalResponse>{ a, b ->
        var firstTaskUpdatedOn = if(a.updatedOn == null) a.createdOn else a.updatedOn
        var secondTaskUpdatedOn = if(b.updatedOn == null) b.createdOn else b.updatedOn
        if(a.taskStatusId == b.taskStatusId){
            timeStampFormatter.parse(secondTaskUpdatedOn).
                compareTo(timeStampFormatter.parse(firstTaskUpdatedOn))
        }
        else{
            a.taskStatusId!!.compareTo(b.taskStatusId!!)
        }
    }

    val doneTasksComparator = Comparator<TaskRetrievalResponse>{ a, b ->
        timeStampFormatter.parse(b.endDate)
            .compareTo(timeStampFormatter.parse(a.endDate))
    }

    /**
     * Before continuing with the task update operation, confirm with user if they really want to
     * perform the selected operation. and if user clicks OK then execute the onConfirm.
     *
     * @param context required to show dialog
     * @param message message to be shown in confirmation dialog
     * @param onConfirm listener to be executed when user confirms the update operation
     *
     * @author Balraj VE00YM023
     */
    fun showConfirmationDialog(context: Context,
                                       message: String,
                                       onConfirm: DialogInterface.OnClickListener){
        var onCancel  = DialogInterface.OnClickListener { _, _ -> }
        var alertDialog = getConfirmationDialog(context, R.style.AlertDialogTheme,
            message, onConfirm, onCancel)
        alertDialog!!.show()
    }


    /**
     * Input applyReturnListFilter implementation to disallow any space character input.
     * this applyReturnListFilter is used by password input fields, so that user can not input any space.
     *
     * @author Balraj VE00YM023
     */
    private val spaceFilter = object : InputFilter {
        override fun filter(source: CharSequence, start: Int, end: Int,
                            dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            for (i in start until end) {
                if (Character.isSpaceChar(source[i])) {
                    return Constants.EMPTY_STRING
                }
            }
            return null
        }
    }

    /**
     * Return the input applyReturnListFilter to disallow any space character.
     * @author Balraj VE00YM023
     */
    fun getSpaceFilter(): InputFilter {
        return spaceFilter
    }


    /**
     * Returns a configured alert dialog which can be used
     * to show some notification to the user.
     * this dialog has only one button with text 'OK'.
     *
     * @author Balraj VE00YM023
     */
    fun getNotificationDialog(
        context: Context, theme: Int, message: String,
        onConfirm: DialogInterface.OnClickListener
    ): AlertDialog? {
        var alterDialogBuilder = AlertDialog.Builder(context, theme)
        alterDialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.ACTION_OK), onConfirm)
        return alterDialogBuilder.create()
    }

    /**
     * Returns a configured alert dialog which can be used
     * to show some action message to the user.
     * this dialog has two action buttons, 'OK' and 'Cancel' respectively.
     *
     * @author Balraj VE00YM023
     */
    fun getConfirmationDialog(
        context: Context, theme: Int, message: String,
        onConfirm: DialogInterface.OnClickListener,
        onCancel: DialogInterface.OnClickListener
    ): AlertDialog? {

        var alterDialogBuilder = AlertDialog.Builder(context, theme)
        alterDialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.ACTION_OK), onConfirm)
            .setNegativeButton(context.getString(R.string.ACTION_CANCEL), onCancel)
        return alterDialogBuilder.create()
    }

    /**
     *method is responsible for generating dialog box
     * this is a generic dialog generator for version check
     */
    fun showVersionDialogBox(activity: BaseActivity<*>, theme: Int,
                             message: String, isErrorDialog:Boolean,
                             onConfirm: DialogInterface.OnClickListener,
                             onCancel: DialogInterface.OnClickListener):AlertDialog?{
        val alterDialogBuilder = AlertDialog.Builder(activity, theme)
        alterDialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(activity.getString(R.string.ACTION_OK), onConfirm)
        if(isErrorDialog) alterDialogBuilder.setNegativeButton(activity.getString(R.string.ACTION_CANCEL), onCancel)
        return alterDialogBuilder.create()
    }

    /**
     * Sorts two tasks based on their assigned date in descending order.
     * this comparator is used by the assigned tasks tab.
     * change date: 26/02/2020
     * @author Balraj VE00YM023
     */
    val taskListComparatorAssignedDate = Comparator<TaskRetrievalResponse>{ a, b ->
        b.assignedDate!!.compareTo(a.assignedDate!!)
    }

    /**
     * sorts tasks based on end date
     */
    val taskListComparatorEndDate = Comparator<TaskRetrievalResponse>{ a,b ->
        val task1 = a.endDate
        val task2 = b.endDate
        task2!!.compareTo(task1!!)
    }

    /**
     * sorts tasks history based on end date
     */
    val taskHistoryComparatorEndDate = Comparator<TaskHistoryResponse>{ a,b ->
        val task1 = a.endDate
        val task2 = b.endDate
        task2.compareTo(task1)
    }

    /**
     * checks if GPS is enabled or not
     */
    fun checkIsGPSEnabledForForegroundService(locationManager: LocationManager): Boolean{
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * triggered when ignition is turned off
     */
    fun completeOnGoingTrip(geocoder: Geocoder, couriemateRepository: CouriemateRepository) {
        GlobalScope.launch(Dispatchers.IO) {
            val activeTrip = couriemateRepository.getOnGoingTrip()
            if (activeTrip.isNotEmpty()) {
                //Get battery voltage and brake count
                val tripEntity = activeTrip[0]
                try {
                    tripEntity.endTime = Utils.getTimeInMilliSec()
                    val lastLocation = couriemateRepository.getTripLastLocation(tripEntity.tripId)

                    val tempEndLat = lastLocation.latitude
                    val tempEndLong = lastLocation.longitude
                    var addresses: List<Address>?
                    var googleAddress = ""
                    try {
                        //possible exception if internet is not there
                        addresses = geocoder.getFromLocation(tempEndLat.toDouble(),
                            tempEndLong.toDouble(), MAX_GEO_CODER_RESULTS
                        )
                        googleAddress = addresses[0].getAddressLine(0)
                    }
                    catch (ex: Exception) { }

                    tripEntity.endAddress = googleAddress
                    tripEntity.isActive = false
                    tripEntity.imei = couriemateRepository.getDeviceId()
                    //Calculate avg speed in metre/seconds
                    val timeTravelledInSeconds =
                        TimeUnit.MILLISECONDS.toSeconds(tripEntity.endTime!! - tripEntity.startTime!!)

                    if (tripEntity.distanceCovered != null) {
                        tripEntity.averageSpeed =
                            ((tripEntity.distanceCovered!!*18) / (5*timeTravelledInSeconds))
                    }

                    tripEntity.startLat = activeTrip[0].startLat
                    tripEntity.startLon = activeTrip[0].startLon
                    tripEntity.endLat = lastLocation.latitude.toDouble()
                    tripEntity.endLon = lastLocation.longitude.toDouble()
                    tripEntity.userId = couriemateRepository.getUserName()?:Constants.NA_KEY
                    couriemateRepository.insertNewTrip(tripEntity)
                }
                catch (ex: Exception) {

                }
            }
        }
    }

    /**
     * used for storing trip locations in room
     * Locations are stored in new trip or in
     * an existing trip.
     */
    fun storeTripLocations(couriemateRepository: CouriemateRepository,latitude: Double, longtitude: Double){
        //Create new trip in local storage with injected locations
        val ccuId = couriemateRepository.getCCUIDForTrip()//Send actual trip ID
        val currentTime = Utils.getTimestampForDate()
        val tripId = "${ccuId}_${currentTime}" //tripId created using CCUID and trip creation timestamp

        //store location in lat_long_entity
        val latLongEntity = LatLongEntity(tripId = tripId,latitude = latitude.toString(),longitude = longtitude.toString(),
            locationCaptureTime = Utils.getTimeInDAPIoTFormat(),isFileCreated = false)
        couriemateRepository.insertNewLocation(latLongEntity)
    }

    /**
     * responsible for adding sensor data to the active trip
     * This data is being stored as a comma separated
     * string in the room database
     */
    fun addSensorData(lastExecution: Long,couriemateRepository: CouriemateRepository,event: SensorEvent): Long?{
        val diff = (Calendar.getInstance().timeInMillis - lastExecution)/1000
            if (diff >= 2) {//This is responsible for a minimum period of 2 seconds for capturing sensor data
                val df = DecimalFormat("#.######") //Need to format sensor response upto 6 decimal places
                //If the accelerometer sensor responds
                if (event.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    Log.d(TAG_ACCEL_SENSOR,"${df.format(x)},${df.format(y)},${df.format(z)}")
                    val sensorTime = Utils.getTimeInDAPIoTFormat()
                    //update the trip accelerometer values in the room
                    //eA1HRepository.updateOnGoingTripWithAccelerometerData(trip.tripId!!, trip.accelerometerReadings!!,trip.accelerometerReadingTimings!!)
                    val accelerometerEntity = AccelerometerEntity(x = df.format(x),
                        y = df.format(y),
                        z = df.format(z),
                        sensorTime = sensorTime,
                        isFileCreated = false,
                        tripId = "${couriemateRepository.getCCUIDForTrip()}_${Utils.getTimestampForDate()}" )
                    couriemateRepository.insertNewAccel(accelerometerEntity)
                }

                //If the gyrometer sensor responds
                else if (event.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                    val x: Float = event.values[0]
                    val y: Float = event.values[1]
                    val z: Float = event.values[2]
                    Log.d(TAG_GYRO_SENSOR,"${df.format(x)},${df.format(y)},${df.format(z)}")
                    val sensorTime = Utils.getTimeInDAPIoTFormat()
                    //insert the trip gyrometer values in the room
                    val gyroEntity = GyroEntity(x = df.format(x),
                        y = df.format(y),
                        z = df.format(z),
                        sensorTime = sensorTime,
                        isFileCreated = false,
                        tripId = "${couriemateRepository.getCCUIDForTrip()}_${Utils.getTimestampForDate()}")
                    couriemateRepository.insertNewGyro(gyroEntity)
                }
                //below paramter is being returned in order to maintain current sensor values recorded time
                //device need to maintain a period of 2 seconds before storing another set of sensor data
                return Calendar.getInstance().timeInMillis
            }
        return null
    }

    /**
     * file upload service to sync local sensor
     * and location data to DAP Server
     */
    fun startZipUploadService(baseContext: Context){
        val componentName = ComponentName(baseContext, ZipUploadService::class.java)
        val info = JobInfo.Builder(ZIP_UPLOAD_SERVICE_JOB_ID, componentName)
        info.setMinimumLatency(1*1000)// minimum latency is 1 seconds
        info.setOverrideDeadline(60*1000) // override deadline caused by another instance of service is 60 seconds
        val scheduler = baseContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(info.build())
    }

    /**
     * starts Trip Config Work manager
     */
    fun startTripConfigService(baseContext: Context){
        val tripConfig: WorkRequest = OneTimeWorkRequest.Builder(TripConfigWorkManager::class.java).setInitialDelay(1,TimeUnit.SECONDS).build()
        WorkManager.getInstance(baseContext).enqueue(tripConfig)
    }

    fun startTransactionConfigService(baseContext: Context){
        val transactionConfig: WorkRequest = OneTimeWorkRequest.Builder(TransactionConfigWorkManager::class.java).setInitialDelay(1,TimeUnit.SECONDS).build()
        WorkManager.getInstance(baseContext).enqueue(transactionConfig)
    }
}