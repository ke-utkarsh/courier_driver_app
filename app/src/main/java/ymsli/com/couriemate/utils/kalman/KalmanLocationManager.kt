package ymsli.com.couriemate.utils.kalman

import android.content.Context
import android.location.LocationListener
import android.util.Log


class KalmanLocationManager(private val mContext: Context) {

    enum class Provider { GPS, NET, GPS_AND_NET }

    companion object {
        const val KALMAN_PROVIDER = "kalman"
        private val TAG = KalmanLocationManager::class.java.simpleName
        private const val MIN_TIME_GPS = 1000L
        private const val MIN_TIME_NET = 5000L
    }

    /** Map that associates provided LocationListeners with created LooperThreads. */
    private val mListener2Thread: MutableMap<LocationListener, LooperThread?> = HashMap()

    /**
     * Register for Location estimates using the given LocationListener callback.
     *
     * @param provider Specifies which of the native location providers to use.
     * @param minTimeFilter Minimum time interval between location estimates, in milliseconds.
     *                      Indicates the frequency of predictions to be calculated by the filter,
     *                      thus the frequency of callbacks to be received by the given location listener.
     *
     * @param listener A LocationListener whose onLocationChanged(Location) method will be called
     *                 for each location estimate produced by the filter.
     *                 It will also receive the status updates from the native providers.
     *
     * @param forwardProviderReadings Also forward location readings from the native providers to the given listener.
     *                                Note that *status* updates will always be forwarded.
     */
    fun requestLocationUpdates(provider: Provider,
                               minTimeFilter: Long,
                               listener: LocationListener,
                               forwardProviderReadings: Boolean) {

        if (mListener2Thread.containsKey(listener)) {
            Log.d(TAG, "Listener already in use. Removing.")
            removeUpdates(listener)
        }
        val looperThread = LooperThread(
            mContext, provider, minTimeFilter, MIN_TIME_GPS, MIN_TIME_NET,
            listener, forwardProviderReadings)
        mListener2Thread[listener] = looperThread
    }

    /**
     * Removes location estimates for the specified LocationListener.
     * Following this call, updates will no longer occur for this listener.
     * @param listener Listener object that no longer needs location estimates.
     * @author Balraj VE00YM023
     */
    fun removeUpdates(listener: LocationListener?) {
        val looperThread = mListener2Thread.remove(listener)
        if (looperThread == null) {
            Log.d(TAG, "LocationListener wasn't registered in this instance.")
            return
        }
        looperThread.close()
    }
}