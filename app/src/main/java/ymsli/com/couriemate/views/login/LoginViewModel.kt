package ymsli.com.couriemate.views.login

import android.util.Log
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.model.TaskRetrievalRequest
import ymsli.com.couriemate.model.UserMaster
import ymsli.com.couriemate.model.UserTokenMapping
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.*
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ymsli.com.couriemate.database.entity.XMPPConfig
import ymsli.com.couriemate.model.LocationConfigResponseData

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * LoginViewModel : This is the Login Viewmodel in the couriemate android application
 *                     It is responsible for data flow between login activity
 *                      & APIs/Local data. This also survives configuration change
 *                      of the activity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class LoginViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private var couriemateRepository: CouriemateRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, couriemateRepository) {

    val loggingIn: MutableLiveData<Boolean> = MutableLiveData()
    val loggedIn:  MutableLiveData<Boolean> = MutableLiveData()
    val appDataLoaded: MutableLiveData<Boolean> = MutableLiveData()
    var usernameField: MutableLiveData<String> = MutableLiveData()
    var passwordField: MutableLiveData<String> = MutableLiveData()

    private val validationsList: MutableLiveData<List<Validation>> = MutableLiveData()

    //error live dataList
    val invalidUsername: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val invalidPassword: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    fun onUsernameChange(username: String) = usernameField.postValue(username)
    fun onPasswordChange(password: String) = passwordField.postValue(password)

    private lateinit var userInfo: UserMaster

    override fun onCreate() {}

    fun doLogin(countryCode: String) {
        val username = usernameField.value
        val password = passwordField.value
        val user =
            UserMaster(username = username, password = password)
        val validations = Validator.validateLoginFields(user)
        validationsList.postValue(validations)
        val successValidation = validations.filter { it.resource.status == Status.SUCCESS }

        when {
            /** Validations failed, notify user */
            successValidation.size != validations.size -> {
                val failedValidation =
                    validations.filter { it.resource.status == Status.ERROR }[0].field
                loginErrorUserAcknowledgement(failedValidation.toString())
            }

            /** Validations passed but no internet, notify user */
            (!checkInternetConnection()) -> {
                messageStringId.postValue(Resource.error(R.string.network_connection_error))
            }

            /** Validations passed and internet is available, continue with API call */
            else -> { //same user is logging in again
                user.username = "$username$countryCode"
               loginUser(countryCode)
            }
        }
    }

    private fun loginUser(countryCode: String){
        val username = usernameField.value
        val password = passwordField.value
        val user =
            UserMaster(username = "$username$countryCode", password = password)
        loggingIn.postValue(true)
        compositeDisposable.addAll(
            couriemateRepository.doLogin(user)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    var jwtToken = it.headers().get("Authorization").toString()
                   // Log.i("jwt",jwtToken)
                    //var jwtToken = it.headers().get("x-amzn-Remapped-Authorization").toString()
                    //if(it.headers().get("x-amzn-Remapped-Authorization").isNullOrBlank()){
                    if(it.headers().get("Authorization").isNullOrBlank()){
                        loggingIn.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.ERROR_AUTH_FAILED))
                    }
                    else {
                        couriemateRepository.setAuthorizationToken(jwtToken)
                        val userId = it.body()!!.responseData!!.userName.toLong()
                        getUserInfo(userId)
                    }
                }, {
                    loggingIn.postValue(false)
                    messageStringId.postValue(Resource.error(R.string.ERROR_AUTH_FAILED))
                })
        )
    }
    /**
     * if the username and password is correct,
     * info is fetched about corresponding user
     */
    private fun getUserInfo(userId: Long) {
        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.userInfo(userId)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        userInfo = it[0]
                        userInfo.password = passwordField.value
                        usernameField.postValue(userInfo.username)
                        loggedIn.postValue(true)
                        saveXMPPConfig(userInfo)
                        getCompanyDetails(userId)
                    }, {
                        loggingIn.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.network_default_error))
                    })
            )
        } else messageStringId.postValue(Resource.error(R.string.network_connection_error))
    }

    /**
     * fetch company details like phone number
     * and stores them locally
     */
    private fun getCompanyDetails(userId: Long) {
        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.getCompanyDetails()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        couriemateRepository.saveCompanyDetails(it)
                        syncLocalDB()
                        //getLocationConfiguration(userId)
                    }, {
                        loggingIn.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.network_internal_error))
                    })
            )
        } else {
            loggingIn.postValue(false)
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }
    }

    /**
     * called during invalid input
     * by the user
     */
    private fun loginErrorUserAcknowledgement(invalidField: String?) =
        when (invalidField) {
            Validation.Field.USERNAME.toString() ->
                invalidUsername.postValue(Event(emptyMap()))
            Validation.Field.EMPTY_PASSWORD.toString() ->
                messageStringId.postValue(Resource.error(R.string.password_field_empty))
            Validation.Field.PASSWORD.toString() ->
                invalidPassword.postValue(Event(emptyMap()))
            else -> {}
        }


    /**
     * updates FCM token in the server
     * for push push notifications
     */
    fun updateFCMToken(token: String) {
        couriemateRepository.updateFCMToken(token)
        var request = UserTokenMapping(
            deviceToken = token,
            userName = usernameField.value
        )
        compositeDisposable.addAll(
            couriemateRepository.updateFCMTokenOnServer(request)
                .subscribeOn(schedulerProvider.io())
                .subscribe({}, {})
        )
    }

    /**
     * fetch dataList that is retained by room
     * when the user logged out.
     * If this is not pushed successfully,
     * prevent next login indefinitely
     */
    private fun syncLocalDB() {
        var oldUserName = couriemateRepository.getUserName()
        var newUserName = usernameField.value
        var updatedOn = couriemateRepository.getUpdatedOnFromSharedPreference()
        if(oldUserName != newUserName){
            couriemateRepository.deleteUserChat()
        }
        GlobalScope.launch(Dispatchers.IO) {
            val notSyncedRecords = couriemateRepository.getNotSyncedRecords()

            if(notSyncedRecords.isEmpty() && oldUserName == newUserName){
                getDriverTaskList(updatedOn)
            }
            else if(notSyncedRecords.isEmpty() && oldUserName != newUserName){
                clearAndSyncLocalDB()
            }
            else if(notSyncedRecords.isNotEmpty() && oldUserName == newUserName){
                pushAndSync(notSyncedRecords, false, updatedOn)
            }
            else{
                pushAndSync(notSyncedRecords, true, null)
            }
        }
    }

    /**
     * push dataList that is retained by room
     * when the user logged out.
     * If this is not pushed successfully,
     * prevent next login indefinitely
     */
    private fun pushAndSync(notSyncedRecords: Array<TaskRetrievalResponse>,
                    clearDBFlag: Boolean, updatedOn: String?) {
        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.syncTasks(notSyncedRecords)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        if(clearDBFlag) couriemateRepository.clearDriverTasks()
                        getDriverTaskList(updatedOn)
                    }, {
                        loggedIn.postValue(false)
                    })
            )
        }
        else {
            loggingIn.postValue(false)
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }

    }



    /**
     * fetches tasks of a driver
     * from remote database
     */
    private fun getDriverTaskList(updatedOn: String? = null) {
        val taskRetrievalRequest = TaskRetrievalRequest(
            driverId = userInfo.driverId,
            timezoneOffset = Utils.getGMTOffset(),
            source = Constants.SOURCE_MOBILE,
            updatedOn = updatedOn
        )
        compositeDisposable.addAll(
            couriemateRepository.getDriverTasks(taskRetrievalRequest)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                            GlobalScope.launch(Dispatchers.IO) {
                                couriemateRepository.insertDriverTasksInRoom(it)
                            }
                            val updatedTime = Utils.getCurrentTimeInServerFormat()
                            couriemateRepository.setUpdatedOnInSharedPreference(updatedTime)
                            couriemateRepository.setUserDataInSharedPref(userInfo)
                            appDataLoaded.postValue(true)
                        }, {
                            try {
                                if ((it is HttpException) && ((it.code() == 401) || (it.code() == 403))) {
                                    logoutUser.postValue(true)
                                } else {
                                    loggingIn.postValue(false)
                                    messageStringId.postValue(Resource.error(R.string.network_default_error))
                                }
                            } catch (ex: Exception) {}
                        })
            )
    }

    /**
     * clears already stored driver tasks
     * in the local database of the phone
     */
    private fun clearAndSyncLocalDB() {
        GlobalScope.launch(Dispatchers.IO) {
            couriemateRepository.clearDriverTasks()
            getDriverTaskList(null)
        }
    }

    /**************** XMPP Chat related functionality ***********************/

    /**
     * Validates the received XMPP config for null or empty values and then
     * stores the config in the local Room database.
     * @author Balraj VE00YM023
     */
    private fun saveXMPPConfig(userMaster: UserMaster){
        couriemateRepository.truncateXMPPConfig()
        val properties = listOf(userMaster.chatUserName, userMaster.chatUserPassword,
            userMaster.chatGroupName, userMaster.chatDomainName)
        properties.forEach { if(it.isNullOrEmpty()) return }
        val config = XMPPConfig(userName = userMaster.chatUserName!!,
                                password = userMaster.chatUserPassword!!,
                                roomName = userMaster.chatGroupName!!,
                                serverDomain = userMaster.chatDomainName!!,
                                historySize = userMaster.chatHistorySize!!,
                                historyRestored = false)
        couriemateRepository.saveXMPPConfig(config)
    }
}
