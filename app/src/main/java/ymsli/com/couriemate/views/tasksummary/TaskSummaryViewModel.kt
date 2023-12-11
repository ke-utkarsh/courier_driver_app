package ymsli.com.couriemate.views.tasksummary

import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.database.entity.TaskSummaryResponse
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 11, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TaskSummaryViewModel : View model for the Task Summary fragment
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TaskSummaryViewModel(schedulerProvider: SchedulerProvider,
                              compositeDisposable: CompositeDisposable,
                              networkHelper: NetworkHelper,
                              private val couriemateRepository: CouriemateRepository
    )
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    var taskSummary: MutableLiveData<TaskSummaryResponse> = MutableLiveData()
    var apiCallFinished: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate() {}

    /**
     * Loads the task summary data from remote server into local room database,
     * and updates the local DB.
     *
     * @author Balraj VE00YM023
     */
    fun getDriverTaskSummaryFromServer(driverId: Int) {
        compositeDisposable.addAll(
            couriemateRepository.getDriverTaskSummaryFromServer(driverId)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    couriemateRepository.insertDriverTaskSummaryInRoom(it)
                    apiCallFinished.postValue(true)
                }, {
                    apiCallFinished.postValue(true)
                })
        )
    }

    /**
     * Retrieve driver tasks summary from the local DB.
     * @param periodId period for which task summary is requested
     * @author Balraj VE00YM023
     */
    fun getDriverTaskSummary(periodId: Int){
        GlobalScope.launch(Dispatchers.IO) {
            var tasks =  couriemateRepository.getDriverTaskSummaryByPeriod(periodId)
            taskSummary.postValue(tasks)
        }
    }

}