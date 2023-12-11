package ymsli.com.couriemate.views.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ymsli.com.couriemate.base.BaseItemViewModel
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Dec 20, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TaskListItemViewModel : Item view model used by the current task fragment recycler
 *                         adapter.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TaskListItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    couriemateRepository: CouriemateRepository
): BaseItemViewModel<TaskRetrievalResponse>(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    /** map the fields required by the UI */
    val orderNo: LiveData<String> = Transformations.map(data) {
        it.orderNo
    }
    val taskStatusId : LiveData<Int> = Transformations.map(data){it.taskStatusId}
    val address: LiveData<String> = Transformations.map(data) { it.dropAddress }

    val updatedOn: LiveData<String> = Transformations.map(data){
        if(it.updatedOn == null) it.createdOn
        else it.updatedOn
    }

    val startDate: LiveData<String> = Transformations.map(data){
        it.startDate
    }

    val endDate: LiveData<String> = Transformations.map(data){
        it.endDate
    }

    override fun onCreate() {}
}