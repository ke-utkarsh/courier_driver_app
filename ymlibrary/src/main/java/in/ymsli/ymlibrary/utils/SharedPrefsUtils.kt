/**
 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 14, 2018
 * Description
 *
 * -----------------------------------------------------------------------------------
 *
 * SharedPrefsUtils Class :  This class provide methods to read and write different type of values
 * with dynamic keys in SharedPreferences
 *
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 *
 * -----------------------------------------------------------------------------------
 *
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.utils


import android.content.Context
import android.preference.PreferenceManager
import android.text.TextUtils

object SharedPrefsUtils {

    /**
     * Helper method to retrieve a String value from SharedPreferences.
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @return The value from shared preferences, or null if the value could not be read.
     */
    fun getStringPreference(context: Context, key: String): String? {
        var value: String? = null
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getString(key, null)
        }
        return value
    }

    /**
     * Helper method to write a String value to SharedPreferences.
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @param value value to set
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setStringPreference(context: Context, key: String, value: String?): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null && !TextUtils.isEmpty(key)) {
            val editor = preferences.edit()
            editor.putString(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a float value from SharedPreferences.
     * @param context a Context object.
     * @param key To  store sharedprefrences value
     * @return The value from shared preferences, or the provided default.
     */
    fun getFloatPreference(context: Context, key: String): Float {
        var value = 0.0f
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getFloat(key, value)
        }
        return value
    }

    /**
     * Helper method to write a float value to SharedPreferences.
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @param value value to set
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setFloatPreference(context: Context, key: String, value: Float): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putFloat(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a long value from SharedPreferences.
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @return The value from shared preferences, or the provided default.
     */
    fun getLongPreference(context: Context, key: String): Long {
        var value: Long = 0
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getLong(key, value)
        }
        return value
    }

    /**
     * Helper method to write a long value to SharedPreferences.
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @param value value to set
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setLongPreference(context: Context, key: String, value: Long): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putLong(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve an integer value from SharedPreferences.
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @return The value from shared preferences, or the provided default.
     */
    fun getIntegerPreference(context: Context, key: String): Int {
        var value = 0
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getInt(key, value)
        }
        return value
    }

    /**
     * Helper method to write an integer value to SharedPreferences.
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @param value value to set
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setIntegerPreference(context: Context, key: String, value: Int): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putInt(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a boolean value from SharedPreferences.
     *
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @return The value from shared preferences, or the provided default.
     */
    fun getBooleanPreference(context: Context, key: String): Boolean {
        var value = false
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getBoolean(key, value)
        }
        return value
    }

    /**
     * Helper method to write a boolean value to SharedPreferences.
     *
     * @param context a Context object.
     * @param key To store sharedprefrences value
     * @param value value to set
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setBooleanPreference(context: Context, key: String, value: Boolean): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putBoolean(key, value)
            return editor.commit()
        }
        return false
    }
}
