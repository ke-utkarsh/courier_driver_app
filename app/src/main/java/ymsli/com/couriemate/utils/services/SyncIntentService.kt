package ymsli.com.couriemate.utils.services

import android.content.Intent
import ymsli.com.couriemate.base.BaseIntentService
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.model.TaskRetrievalRequest
import ymsli.com.couriemate.utils.braodcastreceivers.ConnectivityChangeReceiver
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Utils
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SyncIntentService : Intent service to sync database of SQLite with remote & vice-versa
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class SyncIntentService: BaseIntentService("SyncIntentService") {

    override fun injectDependencies(serviceComponent: ServiceComponent)
            = serviceComponent.inject(this)

    override fun onHandleIntent(intent: Intent?) {

        when (intent?.action) {
            ConnectivityChangeReceiver.INTENT_ACTION_NETWORK_CONNECTED -> handleNetworkChange()
        }
    }

    private fun handleNetworkChange(){
        try{
            pushToRemote()
            pullFromRemote()

          //  pushOfflineLocationToServer()
        }
        catch(exception: Exception){
            Log.d( "Network Change Sync","Failed")
        }
    }

    private fun pushToRemote() {
        if(couriemateRepository.isLoggedIn()){
            var updateData = couriemateRepository.getNotSyncedRecords()
            var errorData = couriemateRepository.syncTasks(updateData).blockingGet()
            var syncedRecords = Utils.getUpdateSyncList(updateData.asList(), errorData.asList())
            syncedRecords.forEach{couriemateRepository.updateSyncStatus(it.taskId!!, true)}
        }
    }

    private fun pullFromRemote(){
        val taskRetrievalRequest = TaskRetrievalRequest(
            driverId = couriemateRepository.getDriverId(),
            timezoneOffset = Utils.getGMTOffset(),
            source = Constants.SOURCE_MOBILE,
            updatedOn = couriemateRepository.getUpdatedOnFromSharedPreference(),
            expectedStart = Utils.getCurrentTimeInServerFormat()
        )

        var driverTasks = couriemateRepository.getDriverTasks(taskRetrievalRequest).blockingGet()
        couriemateRepository.insertDriverTasksInRoom(driverTasks)
        val updatedTime = Utils.getCurrentTimeInServerFormat()
        couriemateRepository.setUpdatedOnInSharedPreference(updatedTime)
    }

    private fun pushOfflineLocationToServer() {
        val offlineLocations = couriemateRepository.getOfflineLocations()

        offlineLocations.forEach { offlineLocation ->
            couriemateRepository.sendLocationToServer(offlineLocation).blockingGet()
            Log.d("Api","Successful"+offlineLocation.deviceTimestamp)
         //   Toast.makeText(applicationContext, "pushOfflineLocationToServer "+offlineLocation.deviceTimestamp, Toast.LENGTH_SHORT).show()

            couriemateRepository.deleteOfflineLocation(offlineLocation.deviceTimestamp)
        }
    }
}
