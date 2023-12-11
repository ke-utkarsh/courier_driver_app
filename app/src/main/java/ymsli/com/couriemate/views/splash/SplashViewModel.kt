package ymsli.com.couriemate.views.splash

import android.util.Log
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.model.CodeMasterResponse
import ymsli.com.couriemate.model.AppVersionResponse
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.couriemate.R
import ymsli.com.couriemate.database.entity.NotificationResponse
import ymsli.com.couriemate.di.ViewModelScope
import ymsli.com.couriemate.utils.common.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 11, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SplashViewModel : View model for the splash activity, provides functions to interact
 *                   with the couriemate repository.
 * -----------------------------------------------------------------------------------
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

class SplashViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val couriemateRepository: CouriemateRepository,
    private val notificationsHandler: NotificationsHandler
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, couriemateRepository) {

    /** observable for the api call status */
    val apiResponse: MutableLiveData<AppVersionResponse?> = MutableLiveData()
    val notificationsSynced: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate() {}

    /**
     * If internet connection is available, then call the API's
     * otherwise post null in apiResponse so that user can move to next activity.
     * @author Balraj VE00YM023
     */
    fun getAppVersion() {
        if (checkInternetConnection()) {
            callAPI()
        }
        else{
            apiResponse.postValue(null)
        }
    }

    /**
     * This function calls following API's.
     *   1. App status api : returns the state of current apk (stable, update required etc)
     *   2. Refuse drop down data: fetches the items for refuse reason drop drop down (task detail)
     *   3. Mobile money drop down data: fetches the items for mobile money drop downs(task detail)
     *
     * @author Balraj VE00YM023
     */
    private fun callAPI() {
        GlobalScope.launch(Dispatchers.IO) {
            try{
                var apiVersionResponse =
                    couriemateRepository.fetchAppStatusAndApiInfo().blockingGet()
                var refuseReasonDropDownData =
                    couriemateRepository.getDropDownData(8).blockingGet()
                var mobileMoneyDropDownData =
                    couriemateRepository.getDropDownData(9).blockingGet()
                setApiVersionInSharedPref(apiVersionResponse)
                storeDropDownData(refuseReasonDropDownData, mobileMoneyDropDownData)
                apiResponse.postValue(apiVersionResponse)
            }
            catch(e: Exception){
                /** Api call failed, post null in apiResponse fields so that user can
                 *  continue with normal app flow */
                apiResponse.postValue(null)
            }
        }
    }

    /**
     * Sets the api version in shared prefs, api version is required for displaying in
     * Navigation bar.
     * @author Balraj VE00YM023
     */
    fun setApiVersionInSharedPref(appVersionResponse: AppVersionResponse) {
        couriemateRepository.setApiVersionInSharedPref(appVersionResponse)
    }

    /**
     * Check if user is currently logged in the application.
     */
    fun isLoggedIn(): Boolean {
        return couriemateRepository.isLoggedIn()
    }

    /**
     * Store the drop down data retrieved as a JSON object in the shared prefs as a string.
     * when drop downs are loaded JSON parsing must be handled by the calling code.
     * @author Balraj VE00YM023
     */
    private fun storeDropDownData(refuseReason: CodeMasterResponse, mobileMoney: CodeMasterResponse){
        var refuseReasons = refuseReason.data["value"]
        var mobileMoneyTypes = mobileMoney.data["value"]
        if(refuseReasons != null && mobileMoneyTypes != null){
            couriemateRepository.updateDropDownData(refuseReasons, mobileMoneyTypes)
        }
    }

    fun storeDeviceId(deviceId: String){
        Log.e("Auth Token",couriemateRepository.getAuthToken().toString())
        couriemateRepository.storeDeviceId(deviceId)
    }

    fun getLocationConfiguration(){
        //getLocationConfiguration(couriemateRepository.getUserId())
    }

    /**
     * Sync the notifications before starting the MainActivity.
     * MainActivity is started irrespective of the response received from this API.
     * @author Balraj VE00YM023
     */
    fun syncNotifications() {
        val driverId = couriemateRepository.getDriverId()
        val timezoneOffset = Utils.getGMTOffset()
        val lastSync = couriemateRepository.getNotificationLastSync()
        if (driverId != null && checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.getAgentNotifications(driverId, timezoneOffset, lastSync)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        processNotificationAPIResponse(it)
                    }, {
                        notificationsSynced.postValue(true)
                    })
            )
        } else {
            notificationsSynced.postValue(true)
        }
    }

    /**
     * Performs the required operation (UPDATE, DELETE, COMPLETE or REMINDER) for every silent
     * notification packet. then inserts all the alert notifications in the ROOM db.
     * @author Balraj VE00YM023
     */
    private fun processNotificationAPIResponse(notifications: Array<NotificationResponse>)
            = GlobalScope.launch(Dispatchers.IO) {
        notificationsHandler.process(notifications.toList())
        val alertNotifications = notifications.filter { Constants.NOTIFICATION_TYPE.ALERT == it.pushType  }.toTypedArray()
        couriemateRepository.insertNotificationsInRoom(alertNotifications)
        if(notifications.isNotEmpty()){
            val lastSync = notifications.first().receivedOn?.toLastSync()
            couriemateRepository.setNotificationLastSync(lastSync)
        }
        notificationsSynced.postValue(true)
    }
}