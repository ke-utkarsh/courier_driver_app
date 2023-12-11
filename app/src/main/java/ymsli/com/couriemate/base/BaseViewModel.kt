package ymsli.com.couriemate.base

import android.location.Geocoder
import android.util.Log
import ymsli.com.couriemate.R
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.Resource
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import ymsli.com.couriemate.model.LocationConfigResponseData
import ymsli.com.couriemate.utils.common.Event
import ymsli.com.couriemate.utils.common.ViewUtils
import java.sql.Timestamp

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseViewModel : This abstract class is the base \view model of all the
 *                  view model of activity, contains common code to all
 *                   view model
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
abstract class BaseViewModel(
    protected val schedulerProvider: SchedulerProvider,
    protected val compositeDisposable: CompositeDisposable,
    protected val networkHelper: NetworkHelper,
    private val couriemateRepository: CouriemateRepository
):ViewModel() {


    var isUploadSuccessful: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val logoutUser: MutableLiveData<Boolean> = MutableLiveData()
    val invalidInput : MutableLiveData<String> = MutableLiveData()
    /**
     * used to unsubscribe from the observer
     * once view-model is destroyed
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    /**
     * used to ack user of any validation, server response, internet connection, etc
     */
    val messageStringId: MutableLiveData<Resource<Int>> = MutableLiveData()

    val messageStringEventId: MutableLiveData<Event<Resource<Int>>> = MutableLiveData()

    val messageStringAPI: MutableLiveData<Event<String>> = MutableLiveData()

    val messageString: MutableLiveData<Resource<String>> = MutableLiveData()

    /**
     * checks instantaneous internet connectivity of the device
     */
    protected fun checkInternetConnection():Boolean = networkHelper.isNetworkConnected()

    /**
     * triggered when ignition is turned off
     */
    fun completeOnGoingTrip(geocoder: Geocoder) {
        ViewUtils.completeOnGoingTrip(geocoder,couriemateRepository)
    }

    fun setTripStatusInSharedPrefs(isTripRunning: Boolean){
        couriemateRepository.setTripStatusInSharedPrefs(isTripRunning)
    }

    fun getTripStatusInSharedPrefs(): Boolean = couriemateRepository.getTripStatusInSharedPrefs()

    /**
     * logs out user from the app
     * and clears all the data
     */
    fun logoutUser(){
        GlobalScope.launch(Dispatchers.IO){
            couriemateRepository.eraseLogoutData()
        }

        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.removeDeviceToken()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({},{})
            )
        }
    }

    /**
     * called during invalid input
     * by the user
     */
    protected open fun errorUserAcknowledgement(invalidField: String?) = invalidInput.postValue("Invalid "+invalidField)

    fun getCompanyContactNumber(): String? = couriemateRepository.getCompanyContactNumber()

    abstract fun onCreate()

    /**
     * returns user name of logged in user
     */
    fun getUserName(): String?{
        return couriemateRepository.getUserName()
    }

    /**
     * returns contact number of logged in user
     */
    fun getUserContact(): String?{
        return couriemateRepository.getUserContact()
    }

    /**
     * returns api version of logged in user
     */
    fun getApiVersion(): String{
        return couriemateRepository.getApiVersion()
    }

    /**
     * returns user type of logged in user
     */
    fun getUserType() = couriemateRepository.getUserType()

    /**
     * returns user password of logged in user
     */
    fun getUserPassword() = couriemateRepository.getUserPassword()

    /**
     * returns user auth token of logged in user
     */
    fun getAuthToken() = couriemateRepository.getAuthToken()

    /**
     * returns driver id of logged in user
     */
    fun getDriverId() = couriemateRepository.getDriverId()

    /**
     * returns user display name of logged in user
     */
    fun getUserDisplayName() = couriemateRepository.getUserDisplayName()

    /**
     * returns notification count of logged in user
     */
    fun getActiveNotificationCount() = couriemateRepository.getActiveNotificationCount()

    /**
     * clear notification count of logged in user
     */
    fun clearActiveNotificationCount() = couriemateRepository.setActiveNotificationCount(0.toString())

    fun getCompanyAddress() = couriemateRepository.getCompanyAddress()

    fun getUnreachableCustomerString() = couriemateRepository.getUnreachableCustomerString()

    /**
     * Creates a new TripId and saves it in the sharedprefs.
     * TripId = userName + timestamp
     * @author Balraj VE00YM023
     */
    fun generateAndSaveNewTripId(){
        val userId = getUserName()
        val timestamp = Timestamp(System.currentTimeMillis())
        val newTripId = "$userId _ $timestamp"
        couriemateRepository.setTripId(newTripId)
    }
}
