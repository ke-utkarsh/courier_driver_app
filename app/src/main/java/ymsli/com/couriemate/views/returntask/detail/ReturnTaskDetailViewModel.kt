package ymsli.com.couriemate.views.returntask.detail

import androidx.lifecycle.MutableLiveData
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.model.NotificationManagementModel
import ymsli.com.couriemate.model.NotificationManagementModelWrapper
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Resource
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import ymsli.com.couriemate.utils.common.TaskStatus
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ReturnTaskDetailViewModel : This is the view model of ReturnTaskDetailActivity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class ReturnTaskDetailViewModel(schedulerProvider: SchedulerProvider,
                                compositeDisposable: CompositeDisposable, networkHelper: NetworkHelper,
                                private val couriemateRepository: CouriemateRepository
)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, couriemateRepository) {

    var taskDetail : MutableLiveData<TaskRetrievalResponse> = MutableLiveData()

    /** Following field is used to store the updated value of order memo field */
    val orderMemo: MutableLiveData<String> = MutableLiveData()


    override fun onCreate() {}

    fun completeTask(selectedTask: TaskRetrievalResponse){
        updateOrderMemo(selectedTask)
        updateModel(selectedTask)
        GlobalScope.launch(Dispatchers.IO) {
            couriemateRepository.updateTasks(arrayOf(selectedTask))
            messageStringId.postValue(Resource.success(R.string.task_returned_success))
        }

        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.syncTasks(arrayOf(selectedTask))
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        /** if error list is empty then mark this record as synced in the DB */
                        if((it!=null) && (it.isEmpty())){
                            couriemateRepository.updateSyncStatus(selectedTask.taskId!!, true)
                        }
                        sendNotification(selectedTask)
                    }, {
                        messageStringId.postValue(Resource.error(R.string.apiCallFailed))
                    })
            )
        }
    }

    /**
     * Send notification is internet connection is available and completed task is not
     * last task for this order.
     */
    private fun sendNotification(task: TaskRetrievalResponse){
        var isLastTask = task.maxTaskSequence == task.taskSequenceNo
        if (checkInternetConnection() && !isLastTask) {
            compositeDisposable.addAll(
                couriemateRepository.sendNotification(getNotificationPacket(task))
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({}, {})
            )
        }
    }

    /**
     * Updates the fields affected by the task complete operation in the model.
     */
    private fun updateModel(task: TaskRetrievalResponse){
        val isLastTask = task.maxTaskSequence == task.taskSequenceNo

        task.lastTask = isLastTask
        task.source = Constants.SOURCE_MOBILE
        task.updatedOn = Utils.getCurrentTimeInServerFormat()
        task.updatedBy = couriemateRepository.getUserName()
        task.taskStatusId = TaskStatus.RETURNED.statusId
        task.endDate = Utils.getCurrentTimeInServerFormat()
        task.timezoneOffset = Utils.getGMTOffset()
        task.actualDelivery = if(isLastTask) Utils.getCurrentTimeInServerFormat() else null
        task.isSynced = false
        task.orderMemo = setOrderMemoValue(orderMemo.value?.toString(), task.orderMemo)

        /** Notify the UI of change */
        taskDetail.postValue(task)
    }

    /**
     * Returns appropriate notification packet for return task complete operation.
     */
    private fun getNotificationPacket(task: TaskRetrievalResponse):
            NotificationManagementModelWrapper {

        var notificationPacket= NotificationManagementModel(
            orderId = task.orderId,
            orderNo = task.orderNo,
            updatedOn = Utils.getCurrentTimeInServerFormat(),
            sendTo = Constants.ANDROID,
            taskStatus = TaskStatus.DELIVERED.statusId,
            taskSequenceNo = task.taskSequenceNo,
            taskNo = task.taskNo
        )

        return NotificationManagementModelWrapper(
            notificationManagementModelList = arrayOf(notificationPacket),
            source = Constants.SOURCE_MOBILE,
            userName = couriemateRepository.getUserName()
        )
    }

    /** Update order memo of all the tasks having this order id */
    private fun updateOrderMemo(selectedTask: TaskRetrievalResponse){
        GlobalScope.launch(Dispatchers.IO) {
            if(selectedTask.orderMemo != orderMemo.value){
                couriemateRepository.updateOrderMemoByOrderId(selectedTask.orderId!!,
                    orderMemo.value?.toString() ?: Constants.EMPTY_STRING)
            }
        }
    }

    private fun setOrderMemoValue(updatedOrderMemo: String?, originalOrderMemo: String?): String?{
        return if(updatedOrderMemo == null || updatedOrderMemo.trim() == Constants.EMPTY_STRING){
            originalOrderMemo
        } else {
            updatedOrderMemo
        }
    }
}