package ymsli.com.couriemate.utils.kalman

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import ymsli.com.couriemate.utils.kalman.KalmanLocationManager.Companion.KALMAN_PROVIDER


internal class LooperThread(
    private val mContext: Context,
    private val provider: KalmanLocationManager.Provider,
    private val minTimeFilter: Long,
    private val minTimeGpsProvider: Long,
    private val minTimeNetProvider: Long,
    private val locationListener: LocationListener,
    private val forwardProviderUpdates: Boolean
) : Thread() {
    private val mClientHandler: Handler = Handler()
    private val mLocationManager: LocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var mLooper: Looper? = null
    private var mOwnHandler: Handler? = null
    private var mLastLocation: Location? = null
    private var mPredicted = false

    /**
     * Three 1-dimension trackers, since the dimensions are independent and can avoid using matrices.
     */
    private var mLatitudeTracker: Tracker1D? = null
    private var mLongitudeTracker: Tracker1D? = null
    private var mAltitudeTracker: Tracker1D? = null

    @SuppressLint("MissingPermission")
    override fun run() {
        priority = THREAD_PRIORITY
        Looper.prepare()
        mLooper = Looper.myLooper()
        if (provider == KalmanLocationManager.Provider.GPS ||
            provider == KalmanLocationManager.Provider.GPS_AND_NET) {
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTimeGpsProvider,
                0.0f,
                mOwnLocationListener,
                mLooper)
        }
        if (provider == KalmanLocationManager.Provider.NET ||
            provider == KalmanLocationManager.Provider.GPS_AND_NET) {
            mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTimeNetProvider,
                0.0f,
                mOwnLocationListener,
                mLooper
            )
        }
        Looper.loop()
    }

    fun close() {
        mLocationManager.removeUpdates(mOwnLocationListener)
        mLooper!!.quit()
    }

    private val mOwnLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            // Reusable
            val accuracy = location.accuracy.toDouble()
            var position: Double
            var noise: Double

            // Latitude
            position = location.latitude
            noise = accuracy * METER_TO_DEG
            if (mLatitudeTracker == null) {
                mLatitudeTracker = Tracker1D(TIME_STEP, COORDINATE_NOISE)
                mLatitudeTracker!!.setState(position, 0.0, noise)
            }
            if (!mPredicted) mLatitudeTracker!!.predict(0.0)
            mLatitudeTracker!!.update(position, noise)

            // Longitude
            position = location.longitude
            noise = accuracy * Math.cos(Math.toRadians(location.latitude)) * METER_TO_DEG
            if (mLongitudeTracker == null) {
                mLongitudeTracker = Tracker1D(TIME_STEP, COORDINATE_NOISE)
                mLongitudeTracker!!.setState(position, 0.0, noise)
            }
            if (!mPredicted) mLongitudeTracker!!.predict(0.0)
            mLongitudeTracker!!.update(position, noise)

            // Altitude
            if (location.hasAltitude()) {
                position = location.altitude
                noise = accuracy
                if (mAltitudeTracker == null) {
                    mAltitudeTracker = Tracker1D(TIME_STEP, ALTITUDE_NOISE)
                    mAltitudeTracker!!.setState(position, 0.0, noise)
                }
                if (!mPredicted) mAltitudeTracker!!.predict(0.0)
                mAltitudeTracker!!.update(position, noise)
            }

            // Reset predicted flag
            mPredicted = false

            // Forward update if requested
            if (forwardProviderUpdates) {
                mClientHandler.post {
                    locationListener.onLocationChanged(Location(location))
                }
            }

            // Update last location
            if (location.provider == LocationManager.GPS_PROVIDER || mLastLocation == null || mLastLocation!!.provider == LocationManager.NETWORK_PROVIDER) {
                mLastLocation = Location(location)
            }

            // Enable filter timer if this is our first measurement
            if (mOwnHandler == null) {
                mOwnHandler = Handler(mLooper, mOwnHandlerCallback)
                mOwnHandler!!.sendEmptyMessageDelayed(0, minTimeFilter)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            mClientHandler.post {
                locationListener.onStatusChanged(
                    provider,
                    status,
                    extras
                )
            }
        }

        override fun onProviderEnabled(provider: String) {
            mClientHandler.post { locationListener.onProviderEnabled(provider) }
        }

        override fun onProviderDisabled(provider: String) {
            mClientHandler.post { locationListener.onProviderDisabled(provider) }
        }
    }
    private val mOwnHandlerCallback = Handler.Callback { // Prepare location
        val location: Location = Location(KALMAN_PROVIDER)

        // Latitude
        mLatitudeTracker!!.predict(0.0)
        location.latitude = mLatitudeTracker!!.position

        // Longitude
        mLongitudeTracker!!.predict(0.0)
        location.longitude = mLongitudeTracker!!.position

        // Altitude
        if (mLastLocation!!.hasAltitude()) {
            mAltitudeTracker!!.predict(0.0)
            location.altitude = mAltitudeTracker!!.position
        }

        // Speed
        if (mLastLocation!!.hasSpeed()) location.speed = mLastLocation!!.speed

        // Bearing
        if (mLastLocation!!.hasBearing()) location.bearing = mLastLocation!!.bearing

        // Accuracy (always has)
        location.accuracy = (mLatitudeTracker!!.accuracy * DEG_TO_METER).toFloat()

        // Set times
        location.time = System.currentTimeMillis()
        if (Build.VERSION.SDK_INT >= 17) location.elapsedRealtimeNanos =
            SystemClock.elapsedRealtimeNanos()

        // Post the update in the client (UI) thread
        mClientHandler.post { locationListener.onLocationChanged(location) }

        // Enqueue next prediction
        mOwnHandler!!.removeMessages(0)
        mOwnHandler!!.sendEmptyMessageDelayed(0, minTimeFilter)
        mPredicted = true
        true
    }

    companion object {
        // Static constant
        private const val THREAD_PRIORITY = 5
        private const val DEG_TO_METER = 111225.0
        private const val METER_TO_DEG = 1.0 / DEG_TO_METER
        private const val TIME_STEP = 1.0
        private const val COORDINATE_NOISE = 4.0 * METER_TO_DEG
        private const val ALTITUDE_NOISE = 10.0
    }

    init {
        start()
    }
}