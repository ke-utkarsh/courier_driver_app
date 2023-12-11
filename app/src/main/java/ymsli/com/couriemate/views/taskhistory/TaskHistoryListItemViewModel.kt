package ymsli.com.couriemate.views.taskhistory

import ymsli.com.couriemate.base.BaseItemViewModel
import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskHistoryListItemViewModel : This is the Task history item view model which renders
 *                                  information about tasks in the recycler view
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class TaskHistoryListItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    couriemateRepository: CouriemateRepository
): BaseItemViewModel<TaskHistoryResponse>(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    val taskId: LiveData<String> = Transformations.map(data) {
        it.orderNo
    }
    val taskStatusId : LiveData<Int> = Transformations.map(data){it.taskStatusId}

    val deliveryLocation: LiveData<String> = Transformations.map(data) { it.deliveryLocation }

    val endDate: LiveData<String> = Transformations.map(data) { it.endDate }

    override fun onCreate() {}
}