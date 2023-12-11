package ymsli.com.couriemate.utils.services

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Nov 15, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SyncWorker : A worker for work manager which is used by the application to perform
 *              the synchronization of local DB with remote DB.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerServiceComponent
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.di.module.SyncWorkerModule
import ymsli.com.couriemate.model.TaskRetrievalRequest
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Utils
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject

class SyncWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @Inject
    lateinit var couriemateRepository: CouriemateRepository

    override fun doWork(): Result {
        injectDependencies().inject(this)
        synchronize()
        return Result.success()
    }

    /**
     * Performs the synchronization of local DB with remote DB.
     * This method performs following actions in specified order
     *      1. Push the local changes (if any) to remote DB
     *      2. Retrieve dataList that has been changed since last sync from remote DB
     *      3. Update the local DB with retrieved records
     *
     * @author Balraj VE00YM023
     */
    private fun synchronize() {
        try{
            pushToRemote()
            clearDataBasedOnIteration()
            pullFromRemote()
        }
        catch(e: Exception){
            e.printStackTrace()
        }
    }

    /**
     * Helper method of the synchronize,
     * used to push the local changes which have not been synced with remote
     * @author Balraj VE00YM023
     */
    private fun pushToRemote(){
        var notSyncedRecords = couriemateRepository.getNotSyncedRecords()
        var result = couriemateRepository.syncTasks(notSyncedRecords).blockingGet()
        var updateSyncList = Utils.getUpdateSyncList(notSyncedRecords.asList() ,result.asList())
        updateSyncList.forEach{couriemateRepository.updateSyncStatus(it.taskId!!, true)}
    }

    /**
     * Helper method of the synchronize,
     * used to pull the records from remote which have been changed after the last sync
     * and update them in the local DB
     * @author Balraj VE00YM023
     */

    private fun pullFromRemote(){
        val expectedStart= Utils.getCurrentTimeInServerFormat()
        var updatedOn = couriemateRepository.getUpdatedOnFromSharedPreference()
        val taskRetrievalRequest = TaskRetrievalRequest(
            driverId = couriemateRepository.getDriverId(),
            timezoneOffset = Utils.getGMTOffset(),
            source = Constants.SOURCE_MOBILE,
            updatedOn = updatedOn,
            expectedStart = expectedStart
        )
        var result = couriemateRepository.getDriverTasks(taskRetrievalRequest).blockingGet()
        couriemateRepository.insertDriverTasksInRoom(result)
        val updatedTime = Utils.getCurrentTimeInServerFormat()
        couriemateRepository.setUpdatedOnInSharedPreference(updatedTime)
    }

    private fun clearDataBasedOnIteration(){
        var iteration = couriemateRepository.getSyncWorkerIteration()
        if(iteration == 84) couriemateRepository.clearDriverTasks()
        couriemateRepository.setSyncWorkerIteration((++iteration)/85)
        Log.d("Sync_Worker_Current_Iteration", iteration.toString())
    }

    private fun injectDependencies(): ServiceComponent {
        return DaggerServiceComponent
            .builder()
            .applicationComponent((context as CourieMateApplication).applicationComponent)
            .syncWorkerModule((SyncWorkerModule(this)))
            .build()
    }
}