package ymsli.com.couriemate.utils.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseService
import ymsli.com.couriemate.di.component.ForegroundServiceComponent
import ymsli.com.couriemate.model.LocationTrackingRequest
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.ViewUtils
import ymsli.com.couriemate.utils.kalman.KalmanLocationManager
import ymsli.com.couriemate.views.main.MainActivity
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.sql.Timestamp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ForegroundLocationService: BaseService()/*SensorEventListener*/ {
    companion object {
        private const val PACKAGE_NAME = "ymsli.com.couriemate"
        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"
        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
        private const val ERROR_MISSING_LOCATION = "Location missing in callback."
        private const val TWO_MINTS_MILLIS = 120000
    }
    private var configurationChange = false
    private var serviceRunningInForeground = false
    private val localBinder = LocalBinder()
    private var lastAPICallingTime : Long = 0

    @Inject
    lateinit var notificationManager: NotificationManager

    private var lastExecutionTime: Long? = null
    private var lastLocation: Location? = null
    private var lastLocationCapturedTime: Long? = null


    /**
     * Instead of the native LocationManager implementation
     * we use a modified version which applies kalman filter to the location updates.
     * @author Balraj VE00YM023
     */
    private lateinit var kalmanLocationManager: KalmanLocationManager

    /**
     * This location listener will be invoked by the kalmanLocationManager
     * to deliver the location updates.
     * @author Balraj VE00YM023
     */
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Utils.writeToFile("\n1A. onLocationChanged is called        : ${Timestamp(System.currentTimeMillis())}")

            //Toast.makeText(applicationContext, "on Location Changed", Toast.LENGTH_SHORT).show()
            if (location != null && checkSpeedThreshold(location) && couriemateRepository.getSendLocation()) {
                Utils.writeToFile("5. Criteria satisfied for calling onNewLocation      : ${Timestamp(System.currentTimeMillis())}")

                val speed = calculateSpeedFromLastLocation(location)
                onNewLocation(location,speed?:0.0)
            }
            else {
                Utils.writeToFile("\n1B. Location is null       : ${Timestamp(System.currentTimeMillis())}")
                Log.d(TAG, ERROR_MISSING_LOCATION)
            }
            lastLocation = location
            lastLocationCapturedTime = Utils.getTimeInMilliSec()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    override fun onCreate() {
        super.onCreate()
        lastAPICallingTime = 0
        kalmanLocationManager = KalmanLocationManager(applicationContext)
    }

    /**
     * calculates speed based on previous location and time interval
     * this uses speed distance formula to calculate speed
     * returns speed in kmph
     */
    private fun calculateSpeedFromLastLocation(location: Location): Double?{
        Utils.writeToFile("3. calculateSpeedFromLastLocation        : ${Timestamp(System.currentTimeMillis())}")

        try {
            if (lastLocation != null && lastLocationCapturedTime != null) {
                val distance = location.distanceTo(lastLocation)
                val currentTime = Utils.getTimeInMilliSec()
                val timeTaken = (currentTime - lastLocationCapturedTime!!) / 1000
                val speedMPS = (distance / timeTaken)
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.CEILING
                return df.format(convertToKMPH(speedMPS)).toDouble()
            }
        }
        catch (ex: Exception){
            return 0.0
        }
        return null
    }

    private fun checkSpeedThreshold(location: Location):Boolean{
        Utils.writeToFile("2. checkSpeedThreshhold called       : ${Timestamp(System.currentTimeMillis())} locationSpeed from server: $couriemateRepository.getLocationSpeed()")

        val speed = calculateSpeedFromLastLocation(location)
        val locationSpeed = couriemateRepository.getLocationSpeed()
        if(speed!=null && speed >= couriemateRepository.getLocationSpeed()){
            Utils.writeToFile("4A. criteria satisfied for speed  and set setLastLocationTimestamp        : ${Timestamp(System.currentTimeMillis())}  locationSpeed from server $locationSpeed")

            couriemateRepository.setLastLocationTimestamp(Utils.getTimeInMilliSec())
            return true
        }

        else{
            Utils.writeToFile("4B. criteria not satisfied for speed  and set setLastLocationTimestamp        : location speed from server : $locationSpeed timestamp:  ${Timestamp(System.currentTimeMillis())}")

            val timestampDiff = couriemateRepository.getSleepModeTime() * 60 * 1000
            val lastLocationTimestamp = couriemateRepository.getLastLocationTimestamp()
            val currentTimestamp = Utils.getTimeInMilliSec()
            return ((currentTimestamp - lastLocationTimestamp)<timestampDiff)
        }
    }

    private fun convertToKMPH(rawSpeed: Float): Double{
        return rawSpeed * 3.6
    }

    override fun injectDependencies(foregroundServiceComponent: ForegroundServiceComponent) = foregroundServiceComponent.inject(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val cancelLocationTrackingFromNotification =
            intent?.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)
        if (cancelLocationTrackingFromNotification==true) {
            stopTrackingLocation()
            stopSelf()
        }
        return START_REDELIVER_INTENT // Tells the system to recreate the service if it's been killed by OS
    }

    /* Activity comes into foreground and binds to service, so the service can
       become a background services. */
    override fun onBind(intent: Intent?): IBinder? {
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }
    /* Activity returns to the foreground and rebinds to service, so the service
       can become a background services. */
    override fun onRebind(intent: Intent?) {
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }
    /* Activity leaves foreground, so service needs to become a foreground service
       to maintain the 'while-in-use' label. */
    override fun onUnbind(intent: Intent?): Boolean {
        if (!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this)) {
            val notification =
                generateNotification()
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }
    /* Binding to this service doesn't actually trigger onStartCommand(). That is needed to
       ensure this Service can be promoted to a foreground service, i.e., the service needs to
       be officially started. */
    fun startTrackingLocation() {
        SharedPreferenceUtil.saveLocationTrackingPref(this, true)
        startService(Intent(applicationContext, ForegroundLocationService::class.java))
        try {
            if(checkGPS() && couriemateRepository.getTripStatusInSharedPrefs()){
                val filterTime = (couriemateRepository.getLocationInterval()*1000).toLong()
                kalmanLocationManager.requestLocationUpdates(
                    KalmanLocationManager.Provider.GPS, filterTime, locationListener, false)
                lastExecutionTime = Utils.getTimeInMilliSec()
            }
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        }
    }

    private fun checkGPS(): Boolean{
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return ViewUtils.checkIsGPSEnabledForForegroundService(locationManager)
    }
    /**
     * callback method triggered when app is killed in background and foreground service is active.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        ViewUtils.completeOnGoingTrip(Geocoder(this, Locale.getDefault()),couriemateRepository)
        stopTrackingLocation()
        couriemateRepository.setTripStatusInSharedPrefs(false)
        super.onTaskRemoved(rootIntent)
        this.stopSelf()
    }

    fun stopTrackingLocation() {
        try {
            kalmanLocationManager.removeUpdates(locationListener)
            ViewUtils.startZipUploadService(baseContext)
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, true)
        }
    }


    /**
     * When new location is captured, try to get the address with geo coder
     * and then update the Room DB locations DB.
     */
    private fun onNewLocation(location: Location, speed: Double) {
        /* Notify anyone listening for broadcasts about the new location. */
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        val apiCallingTime = Utils.getTimeInMilliSec()
        val configInterval = 0.95*(couriemateRepository.getLocationInterval()*1000).toLong()

        val lastAPICallingTime1 = couriemateRepository.getLastAPICallingTime()
        Utils.writeToFile("6. onNewLocation - configInterval       : $configInterval, timestamp ${Timestamp(System.currentTimeMillis())}  lastAPICallingTime:  $lastAPICallingTime1   apiCallingTime: $apiCallingTime")

        if(apiCallingTime-couriemateRepository.getLastAPICallingTime()>=configInterval) {
            Utils.writeToFile("7A. Criteria meet for interval of sending location to server      : lastapicalling time: $lastAPICallingTime1 ${Timestamp(System.currentTimeMillis())}  configInterval: $configInterval")

            // save to shared preference
            couriemateRepository.setLastAPICallingTime(apiCallingTime)
            //lastAPICallingTime = apiCallingTime
            sendLocationToServer(location.latitude, location.longitude, speed)
        }else{
            Utils.writeToFile("7B. Criteria not meet for interval of sending location to server      : ${Timestamp(System.currentTimeMillis())}  configInterval: $configInterval")

        }
    }
    /** Post a notification to the system notification try to keep the service running */
    private fun generateNotification(): Notification {
        var foregroundText: String = this.resources.getString(R.string.foreground_service_notif_message)
        val titleText = getString(R.string.app_name)
        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(NOTIFICATION_CHANNEL_ID, titleText,
                NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationChannel.setSound(null,null)
        /* Adds NotificationChannel to system. Attempting to create an
           existing notification channel with its original values performs
           no operation, so it's safe to perform the below sequence. */
        notificationManager.createNotificationChannel(notificationChannel)
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(foregroundText)
            .setBigContentTitle(titleText)
        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        val mainIntent = Intent(this, MainActivity::class.java)
        val homeActivityPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(foregroundText)
            .setSmallIcon(R.mipmap.couriemate_launcher_round)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setContentIntent(homeActivityPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    /**
     * If network is connected then push the current location to the server,
     * location data is used for live location tracking feature.
     * @author Balraj VE00YM023
     */
    private fun sendLocationToServer(latitude: Double, longitude: Double,speed: Double){
        val userName = couriemateRepository.getUserName()
        Utils.writeToFile("8A. Checking network before sending location to server       : ${Timestamp(System.currentTimeMillis())}")

        if(networkHelper.isNetworkConnected() && !userName.isNullOrEmpty()){
            val tripId = couriemateRepository.getTripId()
            val userId = couriemateRepository.getUserId()
            val companyCode = couriemateRepository.getCompanyCode()
            val deviceTimestamp = Utils.getTimeInDAPIoTFormat()
            val request = LocationTrackingRequest(tripId, companyCode, userId, latitude,
                longitude,speed,deviceTimestamp)
            GlobalScope.launch(Dispatchers.IO) {
                Utils.writeToFile("9. Calling API      : ${Timestamp(System.currentTimeMillis())}")
                Utils.writeToFile("10. REQUEST      : tripId -> $tripId, companyCode -> $companyCode, userId -> $userId, latitude -> $latitude, longitude -> $longitude, speed -> $speed, deviceTimestamp -> $deviceTimestamp")

                couriemateRepository.sendLocationToServer(request)
                        .subscribeOn(scheduleProvider.io())
                        .subscribe({
                            Utils.writeToFile("11A. API SUCCESSFUL                : ${Timestamp(System.currentTimeMillis())}")

                            Log.d("Api","Successful")

                            //Check if there is any offline records available
                            pushOfflineLocationToServer()

                            //Toast.makeText(applicationContext, "In Location send success", Toast.LENGTH_SHORT).show()
                        },{
                            Utils.writeToFile("11B. API FAILED  : ${it.localizedMessage}  : ${Timestamp(System.currentTimeMillis())}")

                            Log.d("Api","Failed")

                            Utils.writeToFile("11C. API FAILED - save location in DB   : ${Timestamp(System.currentTimeMillis())}  request:   $request")

                            couriemateRepository.insertOfflineLocation(request)
                        })
            }
        } else{
            //save data to DB

            val tripId = couriemateRepository.getTripId()
            val userId = couriemateRepository.getUserId()
            val companyCode = couriemateRepository.getCompanyCode()
            val request = LocationTrackingRequest(tripId, companyCode, userId, latitude,
                longitude,speed,deviceTimestamp = Utils.getTimeInDAPIoTFormat())
            Log.e("timestamp while saving offline location",request.deviceTimestamp)

            Utils.writeToFile("8B. No network - save location in DB              : ${Timestamp(System.currentTimeMillis())}  request   $request")

            couriemateRepository.insertOfflineLocation(request)



        }
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: ForegroundLocationService
            get() = this@ForegroundLocationService
    }

    /** Update the location data in room DB */
    private fun addData(latitude: Double, longitude: Double){
        ViewUtils.storeTripLocations(couriemateRepository, latitude, longitude)
        /* run ZipUploadService every 2 minutes */
        val currentTime = Utils.getTimeInMilliSec()
        if(lastExecutionTime!=null) {
            /* ZipUploadService will be triggered periodically for 2 minutes */
            if ((currentTime - lastExecutionTime!!) > (TWO_MINTS_MILLIS)) {
                ViewUtils.startZipUploadService(baseContext)
                lastExecutionTime = Utils.getTimeInMilliSec()
            }
        }
    }

    private fun pushOfflineLocationToServer() {
        Utils.writeToFile("12. Checking any offline locations in DB         : ${Timestamp(System.currentTimeMillis())}")

        val offlineLocations = couriemateRepository.getOfflineLocations()

        offlineLocations.forEach { offlineLocation ->
            Utils.writeToFile("13. Sending offline locations from Db to server        : ${Timestamp(System.currentTimeMillis())}")
            try {
                couriemateRepository.sendLocationToServer(offlineLocation).blockingGet()
            }
            catch(e:Exception){
                Utils.writeToFile("14. Sending offline locations from Db to server failure       : ${Timestamp(System.currentTimeMillis())}")
            }
            Log.d("Api","Successful"+offlineLocation.deviceTimestamp)
            //   Toast.makeText(applicationContext, "pushOfflineLocationToServer "+offlineLocation.deviceTimestamp, Toast.LENGTH_SHORT).show()
            Utils.writeToFile("15. Deleting offline location after sending to server from Db      : ${Timestamp(System.currentTimeMillis())}")

            couriemateRepository.deleteOfflineLocation(offlineLocation.deviceTimestamp)
        }
    }
}
private const val TAG = "ForegroundLocationService"
/*
 * The desired interval for location updates. Inexact. Updates may be
 * more or less frequent.
 */
private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = TimeUnit.SECONDS.toMillis(5)
/*
 * The fastest rate for active location updates. Updates will never be
 * more frequent than this value.
 */
private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS
private const val NOTIFICATION_ID = 12345678
private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"

