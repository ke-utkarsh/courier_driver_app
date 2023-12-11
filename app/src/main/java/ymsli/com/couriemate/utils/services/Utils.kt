package ymsli.com.couriemate.utils.services

import android.content.Context
import ymsli.com.couriemate.R


/**
 * Provides access to SharedPreferences for location to Activities and Services.
 */
internal object SharedPreferenceUtil {

    const val KEY_FOREGROUND_ONLY_ENABLED = "tracking_foreground_only_location"

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            context.getString(R.string.project_id), Context.MODE_PRIVATE)
            .getBoolean(KEY_FOREGROUND_ONLY_ENABLED, false)

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
   /* fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            context.getString(R.string.project_id),
            Context.MODE_PRIVATE).edit {
            putBoolean(KEY_FOREGROUND_ONLY_ENABLED, requestingLocationUpdates)
        }*/

    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) {
        val sharedPrefs = context.getSharedPreferences(context.getString(R.string.project_id), Context.MODE_PRIVATE)
        sharedPrefs?.edit()?.putBoolean(KEY_FOREGROUND_ONLY_ENABLED,requestingLocationUpdates)?.commit()
    }
}