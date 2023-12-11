package ymsli.com.couriemate.views.notifications

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.database.entity.NotificationResponse
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.couriemate.utils.common.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 7, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * NotificationListViewModel : View model for the NotificationListFragment
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class NotificationListViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val couriemateRepository: CouriemateRepository,
    private val notificationsHandler: NotificationsHandler
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, couriemateRepository) {

    var apiSynced: MutableLiveData<Boolean> = MutableLiveData()
    var notifications: MutableLiveData<Array<NotificationResponse>> = MutableLiveData()
    override fun onCreate() {}

    /**
     * If the internet connection is available then retrieve the updated notification
     * data from the remote server and post the updated data to UI.
     * otherwise show an appropriate toast and post local notification data to UI.
     *
     * @author Balraj VE00YM023
     */
    fun getNotificationsFromServer() {
        var driverId = couriemateRepository.getDriverId()
        var timezoneOffset = Utils.getGMTOffset()
        val lastSync = couriemateRepository.getNotificationLastSync()
        if (driverId != null && checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.getAgentNotifications(driverId, timezoneOffset, lastSync)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({ notifications ->
                        /** Process all the silent notification packets */
                        GlobalScope.launch(Dispatchers.IO) {
                            notificationsHandler.process(notifications.toList())
                        }
                        apiSynced.postValue(true)
                        /** We only show ALERT notifications to user. */
                        val alertNotifications = notifications.filter { Constants.NOTIFICATION_TYPE.ALERT == it.pushType  }.toTypedArray()
                        couriemateRepository.insertNotificationsInRoom(alertNotifications)
                        if(notifications.isNotEmpty()){
                            val updatedLastSync = notifications.first().receivedOn?.toLastSync()
                            couriemateRepository.setNotificationLastSync(updatedLastSync)
                        }
                        postNotificationsToUI()
                    }, {
                        messageStringId.postValue(Resource.error(R.string.notification_sync_failed))
                        apiSynced.postValue(false)
                        postNotificationsToUI()
                    })
            )
        } else {
            messageStringId.postValue(Resource.error(R.string.notification_sync_failed))
            apiSynced.postValue(false)
            postNotificationsToUI()
        }
    }

    /**
     * Retrieve notification data from Room and post it in notifications live data field.
     * So that UI can update itself.
     * @author Balraj VE00YM023
     */
    private fun postNotificationsToUI() {
        GlobalScope.launch(Dispatchers.IO) {
            var agentNotifications = couriemateRepository.getAgentNotificationsFromRoom()
            notifications.postValue(agentNotifications)
        }
    }
}