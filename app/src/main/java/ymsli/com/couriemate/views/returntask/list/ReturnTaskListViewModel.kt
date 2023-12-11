package ymsli.com.couriemate.views.returntask.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.model.TaskRetrievalRequest
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.*
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Nov 11, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * ReturnTaskListViewModel : View model for all the return tasks list fragments.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class ReturnTaskListViewModel(schedulerProvider: SchedulerProvider,
                              compositeDisposable: CompositeDisposable,
                              networkHelper: NetworkHelper,
                              private val couriemateRepository: CouriemateRepository
):
    BaseViewModel(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    val isDataRefreshed : MutableLiveData<Boolean> = MutableLiveData()
    val errorDataRefreshed : MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate() {}

    /**
     * Retrieve return tasks from the local DB.
     */
    fun getReturnTasks(): LiveData<Array<TaskRetrievalResponse>> {
        return couriemateRepository.getDriverTasks(Constants.RETURN_TASK_PATTERN)
    }

    /**
     * If internet connection is available then
     * Fetch the updated tasks data from the remote server.
     * This method only retrieves records which have been updated after the last sync time.
     *
     * @author Balraj VE00YM023
     */
    fun refreshDriverTasks(){
        isDataRefreshed.postValue(false)
        errorDataRefreshed.postValue(false)
        val expectedStart= Utils.getCurrentTimeInServerFormat()
        var updatedOn = couriemateRepository.getUpdatedOnFromSharedPreference()
        val taskRetrievalRequest = TaskRetrievalRequest(
            driverId = couriemateRepository.getDriverId(),
            timezoneOffset = Utils.getGMTOffset(),
            source = Constants.SOURCE_MOBILE,
            updatedOn = updatedOn,
            expectedStart = expectedStart
        )
        if(checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.getDriverTasks(taskRetrievalRequest)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        GlobalScope.launch(Dispatchers.Default) {
                            couriemateRepository.insertDriverTasksInRoom(it)
                            val updatedTime = Utils.getCurrentTimeInServerFormat()
                            couriemateRepository.setUpdatedOnInSharedPreference(updatedTime)
                            isDataRefreshed.postValue(true)
                        }
                    }, {
                        if((it is HttpException) && ((it.code() == 401) || (it.code() == 403))){
                                logoutUser.postValue(true)
                        }
                        else {
                            errorDataRefreshed.postValue(true)
                            messageStringId.postValue(Resource.error(R.string.network_default_error))
                        }
                    })
            )
        }
    }

}