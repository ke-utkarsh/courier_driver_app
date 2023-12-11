package ymsli.com.couriemate.preferences

import android.content.SharedPreferences
import io.reactivex.Single
import ymsli.com.couriemate.BuildConfig
import ymsli.com.couriemate.model.AppVersionResponse
import ymsli.com.couriemate.model.CompanyDetails
import ymsli.com.couriemate.model.LocationConfigResponseModel
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.model.UserMaster
import ymsli.com.couriemate.utils.common.Constants.Companion.DEFAULT_TRIP_ID
import ymsli.com.couriemate.utils.common.Constants.Companion.EMPTY_STRING
import ymsli.com.couriemate.utils.common.Utils
import javax.inject.Inject
import javax.inject.Singleton

//Wrapper class for app shared preferences
//only repository has access to the instance
//of this class
@Singleton
class CouriemateSharedPreferences @Inject constructor(
    private val couriemateSharedPreferences: SharedPreferences
){

    private companion object {
        private const val TRIP_ID = "TRIP_ID"
        private const val PREF_KEY_COMPANY_CODE = "COMPANY_CODE"
        private const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
        private const val PREF_KEY_NOTIFICATION_LAST_SYNC = "PREF_KEY_NOTIFICATION_LAST_SYNC"
    }

    /**
     * saves the API version response in
     * shared preference
     */
    fun setApiVersionInSharedPref(appVersionResponse: AppVersionResponse){
        if (appVersionResponse.apiVersion!!.contains("-")) {
            couriemateSharedPreferences.edit().putString(
                Constants.SHARED_PREF_KEY_API_VERSION,
                appVersionResponse.apiVersion.substring(0, appVersionResponse.apiVersion.indexOf('-')))
                .apply()
        } else {
            couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_API_VERSION, appVersionResponse.apiVersion).apply()
        }
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_API_PROFILE, appVersionResponse.springProfile).apply()
    }

    fun getApiVersion(): String?{
        return couriemateSharedPreferences.getString(Constants.SHARED_PREF_KEY_API_VERSION, Constants.EMPTY_STRING)
    }

    fun isLoggedIn():Boolean{
        return couriemateSharedPreferences.getBoolean(Constants.SHARED_PREF_KEY_IS_LOGGED,false)
    }

    fun getInitStatus():Boolean{
        return couriemateSharedPreferences.getBoolean(Constants.SHARED_PREF_KEY_INIT_STATUS,false)
    }

    fun setInitStatus(initStatus : Boolean){
        couriemateSharedPreferences.edit().putBoolean(Constants.SHARED_PREF_KEY_INIT_STATUS,initStatus).commit()
    }

    fun getCCUID(): String? = couriemateSharedPreferences.getString(Constants.CCUID_KEY,null)

    fun setUserDataInSharedPref(user: UserMaster){
        couriemateSharedPreferences.edit().putBoolean(Constants.SHARED_PREF_KEY_IS_LOGGED,true).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_DRIVER_ID, user.driverId.toString()).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_USER_PHONE,user.mobileNo).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_USER_NAME,user.username).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_PASSWORD,user.password).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_DEVICE_ID,user.deviceToken).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_USER_TYPE, user.userType).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_USER_DISPLAY_NAME, user.displayName).apply()
        couriemateSharedPreferences.edit().putString(Constants.CCUID_KEY, user.ccuId).apply()
        couriemateSharedPreferences.edit().putLong(PREF_KEY_USER_ID, user.userId!!).commit()
    }

    fun getAuthorizationToken():String?{
        return couriemateSharedPreferences.getString(Constants.SHARED_PREF_KEY_API_AUTH_TOKEN,null)
    }

    fun setUpdatedOnInSharedPreference(updatedOn : String){
        couriemateSharedPreferences.edit().putString(Constants.UPDATED_ON_SHARED_PREF_KEY,updatedOn).apply()
    }

    fun getUpdatedOnFromSharedPreference():String?{
        return couriemateSharedPreferences.getString(Constants.UPDATED_ON_SHARED_PREF_KEY,null)
    }

    fun eraseLogoutDataFromSharedPreferences(){
        couriemateSharedPreferences.edit().putBoolean(Constants.SHARED_PREF_KEY_IS_LOGGED,false).apply()
        couriemateSharedPreferences.edit().putBoolean(Constants.SHARED_PREF_KEY_INIT_STATUS,false).apply()
        couriemateSharedPreferences.edit().putString(Constants.CCUID_KEY, null).apply()
        couriemateSharedPreferences.edit().putInt(Constants.PREVIOUS_DAY,0).apply()
    }

    /**
     * Returns the driver Id of currently logged in user.
     */
    fun getDriverId(): Int?{
        return couriemateSharedPreferences.getString(Constants.SHARED_PREF_KEY_DRIVER_ID,null)?.toInt()
    }

    fun getUserPassword(): String?{
        return couriemateSharedPreferences.getString(
            Constants.SHARED_PREF_KEY_PASSWORD,
            Constants.EMPTY_STRING)
    }

    fun updateUserPassword(newPassword: String){
        couriemateSharedPreferences.edit().
            putString(Constants.SHARED_PREF_KEY_PASSWORD,newPassword).apply()
    }

    fun getUserName(): String?{
        return couriemateSharedPreferences.getString(
            Constants.SHARED_PREF_KEY_USER_NAME,
            Constants.EMPTY_STRING)
    }

    fun saveCompanyDetails(companyDetails: CompanyDetails){
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_COMPANY_CONTACT, companyDetails.contactNumber).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_COMPANY_ADDRESS, companyDetails.address).apply()
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_CUSTOMER_NOT_REACHABLE,companyDetails.defaultMsgToCustomer).apply()
        couriemateSharedPreferences.edit().putString(PREF_KEY_COMPANY_CODE, companyDetails.companyCode).apply()
    }

    fun getCompanyContactNumber():String? = couriemateSharedPreferences.getString(Constants.SHARED_PREF_KEY_COMPANY_CONTACT,null)


    fun updateFCMToken(token: String){
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_FCM_TOKEN, token).apply()
    }

    fun getFCMToken(): String?{
        return couriemateSharedPreferences.getString(Constants.SHARED_PREF_KEY_FCM_TOKEN,null)
    }

    fun getUserContact(): String?{
        return couriemateSharedPreferences.getString(Constants.SHARED_PREF_KEY_USER_PHONE,null)
    }

    fun setAuthorizationToken(authorizationToken : String){
        couriemateSharedPreferences.edit().putString(Constants.SHARED_PREF_KEY_API_AUTH_TOKEN,authorizationToken).commit()
    }

    fun setSyncWorkerIteration(iteration: Int){
        couriemateSharedPreferences.edit().putInt(Constants.SHARED_PREF_KEY_SYNC_WORKER_ITERATION, iteration).apply()
    }

    fun getSyncWorkerIteration(): Int{
        return couriemateSharedPreferences.getInt(Constants.SHARED_PREF_KEY_SYNC_WORKER_ITERATION, 0)
    }

    fun getUserType(): String {
        return couriemateSharedPreferences
            .getString(Constants.SHARED_PREF_KEY_USER_TYPE, Constants.EMPTY_STRING)?:Constants.EMPTY_STRING
    }

    fun updateDropDownData(refuseReason: String, mobileMoney: String) {
        couriemateSharedPreferences.edit().
            putString(Constants.SHARED_PREF_KEY_REFUSE_REASONS, refuseReason).apply()
        couriemateSharedPreferences.edit().
            putString(Constants.SHARED_PREF_KEY_MOBILE_MONEY_TYPES, mobileMoney).apply()
    }

    fun getRefuseReasons(): String?{
        return couriemateSharedPreferences
            .getString(Constants.SHARED_PREF_KEY_REFUSE_REASONS, null)
    }

    fun getMobileMoneyTypes(): String?{
        return couriemateSharedPreferences
            .getString(Constants.SHARED_PREF_KEY_MOBILE_MONEY_TYPES, null)
    }

    fun getUserDisplayName() = couriemateSharedPreferences
        .getString(Constants.SHARED_PREF_KEY_USER_DISPLAY_NAME, Constants.EMPTY_STRING)

    fun getActiveNotificationCount() = couriemateSharedPreferences
        .getString(Constants.SHARED_PREF_KEY_NOTIFICATION_COUNT, "0")

    fun setActiveNotificationCount(count: String) {
        couriemateSharedPreferences.edit()
            .putString(Constants.SHARED_PREF_KEY_NOTIFICATION_COUNT, count).apply()
    }

    fun getCompanyAddress() = couriemateSharedPreferences
        .getString(Constants.SHARED_PREF_KEY_COMPANY_ADDRESS,  Constants.EMPTY_STRING)

    fun getUnreachableCustomerString() = couriemateSharedPreferences
        .getString(Constants.SHARED_PREF_KEY_CUSTOMER_NOT_REACHABLE,null)

    fun setStoreDeviceId(deviceId: String){
        couriemateSharedPreferences.edit()
            .putString(Constants.UNIQUE_DEVICE_ID_KEY, deviceId).commit()
    }

    fun getStoreDeviceId(): String? = couriemateSharedPreferences
    .getString(Constants.UNIQUE_DEVICE_ID_KEY,Constants.EMPTY_STRING)

    fun setTripStatusInSharedPrefs(isTripRunning: Boolean) = couriemateSharedPreferences.edit().putBoolean(Constants.IS_TRIP_RUNNING_STATUS,isTripRunning).commit()

    fun getTripStatusInSharedPrefs(): Boolean = couriemateSharedPreferences.getBoolean(Constants.IS_TRIP_RUNNING_STATUS,false)

    fun setTripId(tripId: String){
        couriemateSharedPreferences.edit().putString(TRIP_ID, tripId).commit()
    }

    fun getTripId() = couriemateSharedPreferences.getString(TRIP_ID, DEFAULT_TRIP_ID)
    fun getCompanyCode() = couriemateSharedPreferences.getString(PREF_KEY_COMPANY_CODE, EMPTY_STRING) ?: EMPTY_STRING
    fun getUserId() = couriemateSharedPreferences.getLong(PREF_KEY_USER_ID, 0L)

    //region store trip configs
    fun setMinimumDuration(duration: Int) = couriemateSharedPreferences.edit().putInt(Constants.MIN_TRIP_DURATION,duration).commit()

    fun getMinimumDuration(): Int = couriemateSharedPreferences.getInt(Constants.MIN_TRIP_DURATION,Constants.MIN_TRIP_DURATION_VALUE)

    fun setMinimumDistance(distance: Int) = couriemateSharedPreferences.edit().putInt(Constants.MIN_TRIP_DISTANCE,distance).commit()

    fun getMinimumDistance(): Int = couriemateSharedPreferences.getInt(Constants.MIN_TRIP_DISTANCE,Constants.MIN_TRIP_DISTANCE_VALUE)

    fun setLocationHistoryURL(url: String) = couriemateSharedPreferences.edit().putString(Constants.LOCATION_HISTORY_SERVER,url).commit()

    fun getLocationHistoryURL(): String? = couriemateSharedPreferences.getString(Constants.LOCATION_HISTORY_SERVER,null)
    //endregion store trip configs

    //region location configuration
    fun setSendLocation(sendLocation: Boolean) = couriemateSharedPreferences.edit().putBoolean(Constants.SEND_LOCATION_KEY,sendLocation).commit()

    fun getSendLocation() : Boolean = couriemateSharedPreferences.getBoolean(Constants.SEND_LOCATION_KEY,false)

    fun setLocationInterval(interval: Int) = couriemateSharedPreferences.edit().putInt(Constants.LOCATION_INTERVAL_KEY,interval).commit()

    fun getLocationInterval(): Int = couriemateSharedPreferences.getInt(Constants.LOCATION_INTERVAL_KEY,10)

    //save last api calling time
    fun setLastAPICallingTime(interval: Long) = couriemateSharedPreferences.edit().putLong(Constants.LAST_API_CALLING_TIME_KEY,interval).commit()

    fun getLastAPICallingTime(): Long = couriemateSharedPreferences.getLong(Constants.LAST_API_CALLING_TIME_KEY,0)


    fun setLocationSpeed(speed: Int) = couriemateSharedPreferences.edit().putInt(Constants.LOCATION_SPEED_KEY,speed).commit()

    fun getLocationSpeed() : Int = couriemateSharedPreferences.getInt(Constants.LOCATION_SPEED_KEY,5)

    fun setSleepModeTime(sleepModeTime: Int) = couriemateSharedPreferences.edit().putInt(Constants.SLEEP_MODE_TIME_KEY,sleepModeTime).commit()

    fun getSleepModeTime() : Int = couriemateSharedPreferences.getInt(Constants.SLEEP_MODE_TIME_KEY,30)

    fun setLastLocationTimestamp(lastLocationTime: Long) = couriemateSharedPreferences.edit().putLong(Constants.LAST_LOCATION_TIMESTAMP_KEY,lastLocationTime).commit()

    fun getLastLocationTimestamp() : Long = couriemateSharedPreferences.getLong(Constants.LAST_LOCATION_TIMESTAMP_KEY,Utils.getTimeInMilliSec())

    fun setLocationConfigServerURL(serverURL: String) = couriemateSharedPreferences.edit().putString(Constants.LOCATION_SERVER_URL_KEY,serverURL).commit()

    fun getLocationConfigServerURL(): String? = couriemateSharedPreferences.getString(Constants.LOCATION_SERVER_URL_KEY,null)

    fun getNotificationLastSync() = couriemateSharedPreferences.getString(PREF_KEY_NOTIFICATION_LAST_SYNC, null)
    fun setNotificationLastSync(lastSync: String?){
        couriemateSharedPreferences.edit().putString(PREF_KEY_NOTIFICATION_LAST_SYNC, lastSync).commit()
    }
    //endregion location configuration
}