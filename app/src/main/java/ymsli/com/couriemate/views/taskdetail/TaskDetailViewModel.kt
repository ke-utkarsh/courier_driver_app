package ymsli.com.couriemate.views.taskdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.model.NotificationManagementModel
import ymsli.com.couriemate.model.NotificationManagementModelWrapper
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.*
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import ymsli.com.couriemate.utils.common.TaskStatus
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Http2
import ymsli.com.couriemate.model.LocationSharingRequest
import java.sql.Timestamp
import java.util.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskDetailViewModel : This is the Task Detail view model and responsible for
 *                          data rendering over UI via APIs/local storage.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class TaskDetailViewModel(
    schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper, private val couriemateRepository: CouriemateRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, couriemateRepository) {

    var taskDetail: MutableLiveData<TaskRetrievalResponse> = MutableLiveData()
    //used by receiver refusal
    var refusalComment: MutableLiveData<String> = MutableLiveData()
    var refusalReason: MutableLiveData<String> = MutableLiveData()

    //used by delivery fragment
    var codReceived: MutableLiveData<String> = MutableLiveData()
    var mmReceived: MutableLiveData<String> = MutableLiveData()
    var taskMemo: MutableLiveData<String> = MutableLiveData()
    var mmType: MutableLiveData<Int> = MutableLiveData()

    var latitude: MutableLiveData<Double> = MutableLiveData()
    var longitude: MutableLiveData<Double> = MutableLiveData()

    //used by fail fragment
    var showDatePicker: MutableLiveData<Boolean> = MutableLiveData()
    var nextDeliveryDateTime: MutableLiveData<Timestamp> = MutableLiveData()
    var failureComments: MutableLiveData<String> = MutableLiveData()
    var taskStatusMessage: MutableLiveData<String> = MutableLiveData()
    var disableButtons: MutableLiveData<Boolean> = MutableLiveData()
    var showProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    var failCarrierTaskUIHide : MutableLiveData<Boolean> = MutableLiveData()

    //user error ack
    //error live dataList
    val invalidRetrydate: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val invalidFailureComments: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    /** Following field is used to store the updated value of order memo field */
    val orderMemo: MutableLiveData<String> = MutableLiveData()

    /** Used for disabling the order memo input field when update is perfomed */
    val disableOrderMemo: MutableLiveData<Boolean> = MutableLiveData()

    val smsBodyLiveData: MutableLiveData<String> = MutableLiveData()
    val arrivalTimeLiveData: MutableLiveData<String> = MutableLiveData()
    override fun onCreate() {}

    /**
     * handles when the receiver
     * refuses the delivery
     */
    fun refuseDelivery() {
        showProgressBar.postValue(true)
        val validations: List<Validation> = Validator.validateRefusedDelivery(refusalComment.value,refusalReason.value)
        val successValidation = validations.filter { it.resource.status == Status.SUCCESS }

        when{
            /** Validation failed, notify user */
            successValidation.size != validations.size -> {
                showProgressBar.postValue(false)
                val failedValidation =
                    validations.filter { it.resource.status == Status.ERROR }[0].field
                errorUserAcknowledgement(failedValidation.toString())
            }

            /** validations passed, continue with API call */
            else -> {
                val refusedTask = taskDetail.value!!
                updateTaskStateForRefuse(refusedTask)
                syncTasks(refusedTask)

                val notificationPacket = NotificationManagementModel(
                    orderId = refusedTask.orderId,
                    orderNo = refusedTask.orderNo,
                    deliveryDistrictId = refusedTask.deliveryDistrictId,
                    updatedOn = Utils.getCurrentTimeInServerFormat(),
                    sendTo = Constants.WEB,
                    taskStatus = TaskStatus.REFUSED.statusId,
                    taskSequenceNo = refusedTask.taskSequenceNo,
                    taskStatusMessage = Constants.REFUSED_MSG + refusedTask.receiverName
                )
                val notificationPacketList = arrayOf((notificationPacket))

                var notificationModel = NotificationManagementModelWrapper(
                    notificationManagementModelList = notificationPacketList,
                    source = Constants.SOURCE_MOBILE,
                    userName = couriemateRepository.getUserName()
                )
                triggerNotificationToAdmin(notificationModel)
            }
        }
    }

    /**
     * handles the successful
     * delivery of an item
     */
    fun proceedDelivery(expectingCod: Boolean, signature: ByteArray?) {
        showProgressBar.postValue(true)
        val selectedTask = taskDetail.value!!

        /** Only perform validations if we are expecting Cod, otherwise just
         *  continue with task update. */
        if(expectingCod){
            var totalAmountReceived = codReceived.value?.toDouble() ?: 0.0
            mmReceived.value?.toDouble()?.let { totalAmountReceived += it }
            val validations = Validator.validateCod(selectedTask.expectedCodAmount ?: 0.0,
                totalAmountReceived)
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
            if (successValidation.size == validations.size) {
                syncTasks(updateTaskStateForDeliver(selectedTask, signature))
            }
            else {
                showProgressBar.postValue(false)
                val failedValidation = validations.filter { it.resource.status == Status.ERROR }[0]
                messageStringId.postValue(failedValidation.resource)
            }
        }
        else{
            syncTasks(updateTaskStateForDeliver(selectedTask, signature))
        }
    }

    /**
     * handles the failure of
     * a particular delivery
     */
    fun failDelivery(failureReason: String?) {
        val validations = Validator.validateFailedCustomerDelivery(failureReason,failureComments.value,nextDeliveryDateTime.value.toString())
        val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
        if((successValidation.size != validations.size)){
            val failedValidation =
                validations.filter { it.resource.status == Status.ERROR }[0].field
            failedTaskErrorUserAcknowledgement(failedValidation.toString())
        }

        else {
            showProgressBar.postValue(true)
            val failedTask = taskDetail.value!!
            updateTaskStateForFail(failedTask, failureReason)
            syncTasks(failedTask)
            disableButtons.postValue(true)
            //trigger notification on web
            var taskStatusMessage = Constants.FAIL_MSG
            if (!failureReason.equals("Other")) {
                taskStatusMessage = taskStatusMessage + Constants.HYPHEN + failureReason
            }
            var notificationPacket = NotificationManagementModel(
                orderId = taskDetail.value?.orderId,
                orderNo = taskDetail.value?.orderNo,
                deliveryDistrictId = taskDetail.value?.deliveryDistrictId,
                updatedOn = Utils.getCurrentTimeInServerFormat(),//Utils.getUpdatedOnForTaskUpdate(),
                sendTo = Constants.WEB,
                taskStatus = TaskStatus.FAILED.statusId,
                taskSequenceNo = taskDetail.value?.taskSequenceNo,
                taskStatusMessage = taskStatusMessage
            )

            var notificationPacketList =
                arrayOf((notificationPacket))
            var notificationModel = NotificationManagementModelWrapper(
                notificationManagementModelList = notificationPacketList,
                source = Constants.SOURCE_MOBILE,
                userName = couriemateRepository.getUserName()
            )
            triggerNotificationToAdmin(notificationModel)
        }
    }

    /**
     * fails the task to be
     * delivered to another carrier
     */
    fun failDeliveryForCarrier(failureReason: String?,taskStatusMessageTemp: String) {
        val tempTask = taskDetail.value!!
        tempTask.source = Constants.SOURCE_MOBILE
        tempTask.updatedOn = Utils.getCurrentTimeInServerFormat()
        tempTask.updatedBy = couriemateRepository.getUserName()
        tempTask.failureCount = (tempTask.failureCount?:0) + 1
        tempTask.taskStatusId = TaskStatus.FAILED.statusId
        tempTask.failureComment = failureComments.value
        tempTask.failureReason = failureReason
        tempTask.timezoneOffset = Utils.getGMTOffset()
        tempTask.isSynced = false
        tempTask.orderMemo = setOrderMemoValue(orderMemo.value?.toString(), tempTask.orderMemo)
        tempTask.expectedDelivery = getNextRetryDate()
        syncTasks(tempTask)
        failCarrierTaskUIHide.postValue(true)

        //trigger notification to admin
        val notificationPacket = NotificationManagementModel(
            orderId=tempTask.orderId,
            orderNo = tempTask.orderNo,
            deliveryDistrictId = tempTask.deliveryDistrictId,
            updatedOn =  Utils.getCurrentTimeInServerFormat(),
            sendTo = Constants.WEB,
            taskStatus = TaskStatus.FAILED.statusId,
            taskSequenceNo = tempTask.taskSequenceNo,
            taskStatusMessage = taskStatusMessageTemp
        )

        val notificationPacketList = arrayOf(notificationPacket)
        val notificationModel = NotificationManagementModelWrapper(
            notificationManagementModelList =notificationPacketList,
            source = Constants.SOURCE_MOBILE,
            userName = couriemateRepository.getUserName())

        triggerNotificationToAdmin(notificationModel)
    }

    /**
     * handles the successful
     * delivery of an item
     */
    fun proceedDeliveryToCarrier() {
        showProgressBar.postValue(true)
        val currentTime = Utils.getCurrentTimeInServerFormat()
        val tempTask = taskDetail.value!!
        tempTask.source = Constants.SOURCE_MOBILE
        tempTask.updatedOn = currentTime
        tempTask.updatedBy = couriemateRepository.getUserName()
        tempTask.taskId = taskDetail.value!!.taskId
        tempTask.maxTaskSequence = taskDetail.value!!.maxTaskSequence
        tempTask.taskStatusId = TaskStatus.DELIVERED.statusId
        tempTask.endDate = currentTime
        tempTask.timezoneOffset = Utils.getGMTOffset()
        tempTask.isSynced = false
        tempTask.orderMemo = setOrderMemoValue(orderMemo.value?.toString(), tempTask.orderMemo)
        syncTasks(tempTask)

        var notificationPacket=NotificationManagementModel(
            orderId=tempTask.orderId,
            orderNo = tempTask.orderNo,
            updatedOn =  Utils.getCurrentTimeInServerFormat(),
            sendTo = Constants.ANDROID,
            taskStatus = TaskStatus.DELIVERED.statusId,
            taskSequenceNo = tempTask.taskSequenceNo,
            taskNo = tempTask.taskNo

        )
        var notificationPacketList=
            arrayOf((notificationPacket))


        var notificationModel= NotificationManagementModelWrapper(

            notificationManagementModelList =notificationPacketList,
            source = Constants.SOURCE_MOBILE,
            userName = couriemateRepository.getUserName())

        triggerNotificationToAdmin(notificationModel)
    }

    /**
     * if tasks is failed/delivered/refused,
     * below method is responsible for updating
     * local database and remote database
     */
    fun syncTasks(task: TaskRetrievalResponse) {

        disableOrderMemo.postValue(true)
        GlobalScope.launch(Dispatchers.IO) {
            couriemateRepository.updateTasks(arrayOf(task))
            taskDetail.postValue(task)
            showProgressBar.postValue(false)
            messageStringId.postValue(Resource.error(R.string.sucessfulUpdation))
        }

        //call APIs to push it to remote DB
        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.syncTasks(arrayOf(task))
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        if (it.isEmpty()){
                            updateTaskSyncStatus(task)
                        }
                    }, {})
            )
        }
    }


    /**
     * updates local database that a particular task has been
     * synced in remote database
     */
    private fun updateTaskSyncStatus(task: TaskRetrievalResponse){
        GlobalScope.launch(Dispatchers.IO) {
            couriemateRepository.updateSyncStatus(task.taskId!!, true)
        }
    }

    /**
     * calls API to trigger notification to
     * the admin panel on web
     */
    private fun triggerNotificationToAdmin(notificationManagementModelWrapper: NotificationManagementModelWrapper) {
        compositeDisposable.addAll(
            couriemateRepository.sendNotification(notificationManagementModelWrapper)
                .subscribeOn(schedulerProvider.io())
                .subscribe({},{})
        )
    }

    /**
     * updates necessary parameter in model
     * when task is set to delivered
     */
    private fun updateTaskStateForDeliver(
        task: TaskRetrievalResponse,
        signature: ByteArray?
    ): TaskRetrievalResponse{
        val currentTime = Utils.getCurrentTimeInServerFormat()
        val customerAddressPresent =
            (task.customerAdressLatitude != null && task.customerAdressLongitude != null)
        val notExpectingCod = (task.orderTypeId!! in 0..2)
        updateCommonFields(task)
        task.actualDelivery = currentTime
        task.driverCodAmount = if(notExpectingCod) 0.0 else codReceived.value?.toDouble()
        task.taskStatusId = TaskStatus.DELIVERED.statusId
        task.endDate = currentTime
        task.addressPresent = customerAddressPresent
        task.customerAdressLatitude = if(customerAddressPresent) task.customerAdressLatitude else latitude.value
        task.customerAdressLongitude = if(customerAddressPresent) task.customerAdressLongitude else longitude.value
        task.latitude = latitude.value?:task.customerAdressLatitude
        task.longitude = longitude.value?:task.customerAdressLongitude
        task.taskMemo = taskMemo.value
        task.receiverOtherPayment = mmReceived.value?.toDouble()?:0.0
        task.receiverOtherPaymentMode = if((task.expectedCodAmount == 0.0) || (task.receiverOtherPayment == 0.0)) null else mmType.value
        task.orderStatusId = task.orderStatusId
        task.startDate = task.startDate
        task.orderMemo = setOrderMemoValue(orderMemo.value?.toString(), task.orderMemo)
        task.signature = signature
        task.deliveryFeeWithTax = task.deliveryFeeWithTax
        return task
    }

    /**
     * updates necessary parameter in model
     * when task is set to refused
     */
    private fun updateTaskStateForRefuse(task: TaskRetrievalResponse){
        updateCommonFields(task)
        val customerAddressPresent =
            (task.customerAdressLatitude != null && task.customerAdressLongitude != null)
        task.addressPresent = customerAddressPresent
        task.customerAdressLatitude = if(customerAddressPresent) task.customerAdressLatitude else latitude.value
        task.customerAdressLongitude = if(customerAddressPresent) task.customerAdressLongitude else longitude.value
        task.latitude = latitude.value?:task.customerAdressLatitude
        task.longitude = longitude.value?:task.customerAdressLongitude
        task.taskStatusId = TaskStatus.REFUSED.statusId
        task.refuseReason = refusalReason.value
        task.refusalComment = refusalComment.value
        task.endDate = Utils.getCurrentTimeInServerFormat()
    }

    /**
     * updates necessary parameter in model
     * when task is set to failed
     */
    private fun updateTaskStateForFail(task: TaskRetrievalResponse,
                                       failureReason: String?){
        updateCommonFields(task)
        task.taskStatusId = TaskStatus.FAILED.statusId
        task.failureCount = (task.failureCount+1)
        task.failureComment = failureComments.value
        task.failureReason = failureReason
        task.expectedDelivery = Utils.formatTimestampToServerFormat(nextDeliveryDateTime.value!!)
    }

    /**
     * updates necessary parameter in model
     * when task is set to delivered/refused/failed
     * These are common parameter changes
     */
    private fun updateCommonFields(task: TaskRetrievalResponse){
        task.updatedOn = Utils.getCurrentTimeInServerFormat()
        task.updatedBy = couriemateRepository.getUserName()
        task.source = Constants.SOURCE_MOBILE
        task.timezoneOffset = Utils.getGMTOffset()
        task.isSynced = false
        task.orderMemo = setOrderMemoValue(orderMemo.value?.toString(), task.orderMemo)
    }

    fun getRefuseReasons() = couriemateRepository.getRefuseReasons()
    fun getMobileMoneyTypes() = couriemateRepository.getMobileMoneyTypes()

    /**
     * validation error/ack shown to user while failing the task
     */
    private fun failedTaskErrorUserAcknowledgement(invalidField: String?) =
    when (invalidField) {
        Validation.Field.RETRYDATE.toString() ->
        invalidRetrydate.postValue(Event(emptyMap()))
        Validation.Field.FAILUREREASON.toString() ->
        invalidFailureComments.postValue(Event(emptyMap()))
        else -> {}
    }

    /** Used to get next day as retry date when delivery is failed for first task */
    private fun getNextRetryDate(): String{
        val d = (Date(System.currentTimeMillis() + 86400000))
        return Utils.formatTimestampToServerFormat(Timestamp((d).time))
    }

    private fun setOrderMemoValue(updatedOrderMemo: String?, originalOrderMemo: String?): String?{
        return if(updatedOrderMemo == null || updatedOrderMemo.trim() == Constants.EMPTY_STRING){
            originalOrderMemo
        } else {
            updatedOrderMemo
        }
    }

    /**
     * gets SMS body of location sharing
     * from API
     */
    fun getSMSBody(){
        val locationSharingRequest = LocationSharingRequest(companyCode = couriemateRepository.getCompanyCode(),taskId = taskDetail.value?.taskId!!,
        templateId = 1,userId = couriemateRepository.getUserId().toString())
        if(checkInternetConnection()){
            compositeDisposable.addAll(
                couriemateRepository.getSMSBody(locationSharingRequest)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        if(it.responseCode==200){
                            var smsBody = it.responseData
                            smsBody = smsBody.replace("[[RECEIVER_NAME]]",taskDetail.value?.receiverName?:Constants.NA_KEY)
                            smsBody = smsBody.replace("[[ARRIVAL_TIME]]",arrivalTimeLiveData.value?:Constants.NA_KEY)
                            smsBodyLiveData.postValue(smsBody)
                        }
                        else{
                            // show error
                            messageString.postValue(Resource.error(it.responseData))
                        }
                    },{
                        //TODO show error
                        messageString.postValue(Resource.error("Something went wrong"))
                    })
            )
        }
        else{
            messageString.postValue(Resource.error("No internet connection, cannot share location!!"))
        }
    }
}

