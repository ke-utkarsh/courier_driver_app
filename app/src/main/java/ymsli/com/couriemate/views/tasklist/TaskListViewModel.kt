package ymsli.com.couriemate.views.tasklist

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.*
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import retrofit2.HttpException
import ymsli.com.couriemate.database.entity.*
import ymsli.com.couriemate.model.*
import ymsli.com.couriemate.utils.common.Constants.Companion.EMPTY_STRING
import ymsli.com.couriemate.views.chat.ChatMessage
import ymsli.com.couriemate.views.taskhistory.filter.TaskHistoryFilterModel
import ymsli.com.couriemate.xmpp.ConnectionStateListener
import ymsli.com.couriemate.xmpp.XMPPManager
import java.net.HttpURLConnection
import java.sql.Timestamp

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Dec 20, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TaskListViewModel : View model for all the current tasks list fragements.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TaskListViewModel(schedulerProvider: SchedulerProvider,
                        compositeDisposable: CompositeDisposable,
                        networkHelper: NetworkHelper,
                        private val couriemateRepository: CouriemateRepository,
                        private val notificationsHandler: NotificationsHandler
):
    BaseViewModel(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    val isDataRefreshed : MutableLiveData<Boolean> = MutableLiveData()
    val errorDataRefreshed : MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Following live data objects are used for filter module implemented in fragments.
     * using these live data, main activity notifies the fragments of any filter dialog state
     * change.
     */
    val filterApplied: MutableLiveData<FilterModel> = MutableLiveData()
    val filterState: MutableLiveData<FilterModel> = MutableLiveData()
    var sortedAssignedTasks: MutableLiveData<List<TaskRetrievalResponse>> = MutableLiveData()
    var sortedDoneTasks: MutableLiveData<List<TaskRetrievalResponse>> = MutableLiveData()

    val taskHistoryFilterApplied: MutableLiveData<TaskHistoryFilterModel> = MutableLiveData()
    val taskHistoryFilterState: MutableLiveData<TaskHistoryFilterModel> = MutableLiveData()

    //mutable live data for payment registration fragment
    var amoundPaid: MutableLiveData<Double> = MutableLiveData()
    var paymentReceivingBank: MutableLiveData<String> = MutableLiveData()
    var paymentNotes: MutableLiveData<String> = MutableLiveData()
    var paymentReceipt: MutableLiveData<String> = MutableLiveData()
    var showProgress: MutableLiveData<Boolean> = MutableLiveData()

    var startTrip: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var expenditureIdValue: MutableLiveData<Long> = MutableLiveData()

    //payment user ack
    var paymentReceiptUserAck : MutableLiveData<Resource<Int>> = MutableLiveData()
    var transactionHistoryItemSelected: MutableLiveData<DeliveryExpenses> = MutableLiveData()

    var deliveryExpensesList: MutableLiveData<List<DeliveryExpenses>> = MutableLiveData()
    var isReceiptValue: MutableLiveData<Boolean> = MutableLiveData()
    var itemIdValue: MutableLiveData<Int> = MutableLiveData()
    var balanceValue: MutableLiveData<Event<Long>> = MutableLiveData()

    override fun onCreate() {

    }

    /**
     * Fetch return tasks from the local DB.
     */
    fun getReturnTasks(): LiveData<Array<TaskRetrievalResponse>> {
        return couriemateRepository.getDriverTasks(Constants.RETURN_TASK_PATTERN)
    }

    /** Fetch forward tasks from the room DB. */
    fun getDriverTasks(): LiveData<Array<TaskRetrievalResponse>> = couriemateRepository.getDriverTasks(
        Constants.FORWARD_TASK_PATTERN)

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
        val updatedOn = couriemateRepository.getUpdatedOnFromSharedPreference()
        val taskRetrievalRequest = TaskRetrievalRequest(
            driverId = couriemateRepository.getDriverId(),
            timezoneOffset = Utils.getGMTOffset(),
            source = Constants.SOURCE_MOBILE,
            updatedOn = updatedOn,
            expectedStart = expectedStart
        )
        Log.i("TaskModel",taskRetrievalRequest.toString())

        if(checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.getDriverTasks(taskRetrievalRequest)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        /** Insert the updated data in local db */
                        GlobalScope.launch(Dispatchers.Default) {
                            couriemateRepository.insertDriverTasksInRoom(it)
                            val updatedTime = Utils.getCurrentTimeInServerFormat()
                            couriemateRepository.setUpdatedOnInSharedPreference(updatedTime)
                        }
                    }, {
                        if((it is HttpException) && ((it.code() == 401) || (it.code()== 403))){
                            logoutUser.postValue(true)
                        }
                        else {
                            errorDataRefreshed.postValue(true)
                            messageStringId.postValue(Resource.error(R.string.network_default_error))
                        }
                    })
            )
        }
        else messageStringId.postValue(Resource.error(R.string.network_connection_error))
    }

    /**
     * used to perform the update operation when user picks up one or more
     * return tasks by either swiping or multi select functionality.
     *
     * this method first updates the local DB and then tries to push the changes
     * to the server if network is available.
     *
     * @author Balraj VE00YM023
     */
    fun performPickup(selectedTasks: List<TaskRetrievalResponse>) {
        updateFields(selectedTasks)

        GlobalScope.launch(Dispatchers.IO) {
            couriemateRepository.updateTasks(selectedTasks.toTypedArray())
        }
        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.syncTasks(selectedTasks.toTypedArray())
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({ result ->
                        val updateSyncList = Utils.getUpdateSyncList(selectedTasks,result.asList())
                        updateSyncList.forEach{couriemateRepository.updateSyncStatus(it.taskId!!, true)}
                    }, {
                        messageStringId.postValue(Resource.error(R.string.apiCallFailed))
                    })
            )
        }
    }

    /**
     * When performing pickup operation, update all the required fields in
     * the selected task model.
     *
     * @author Balraj VE00YM023
     */
    private fun updateFields(selectedTasks: List<TaskRetrievalResponse>){
        val timezoneOffset = Utils.getGMTOffset()

        for(task in selectedTasks){
            val currentTime = Utils.getCurrentTimeInServerFormat()
            task.startDate = currentTime
            task.taskStatusId = TaskStatus.DELIVERING.statusId
            task.updatedOn = currentTime
            task.updatedBy = couriemateRepository.getUserName()
            task.source = Constants.SOURCE_MOBILE
            task.timezoneOffset = timezoneOffset
            task.isSynced = false
            task.orderStatusId = OrderStatus.DELIVERING.statusId
        }
    }


    /**
     * Get assigned tasks for this driver.
     * used to populate the list on assigned tasks tab.
     *
     * @author Balraj VE00YM023
     */
    fun getAssignedTasksForToday(): LiveData<Array<TaskRetrievalResponse>>{
        val taskStatusIds = listOf(TaskStatus.ASSIGNED.statusId)
        return couriemateRepository.getTasksByStatusAndPickupDate(taskStatusIds)
    }

    /**
     * Get ongoing tasks(Delivering and Failed) for this driver.
     * used to populate the list on ongoing tasks tab.
     *
     * @author Balraj VE00YM023
     */
    fun getOngoingTasksForToday(): LiveData<Array<TaskRetrievalResponse>>{
        val taskStatusIds = listOf(TaskStatus.DELIVERING.statusId, TaskStatus.FAILED.statusId)
        return couriemateRepository.getTasksByStatusAndPickupDate(taskStatusIds)
    }

    /**
     * Get done tasks(Delivered, Returned and Refused) for this driver.
     * used to populate the list on done tasks tab.
     *
     * @author Balraj VE00YM023
     */
    fun getDoneTasksForToday(): LiveData<Array<TaskRetrievalResponse>>{
        val taskStatusIds = listOf(TaskStatus.DELIVERED.statusId, TaskStatus.RETURNED.statusId, TaskStatus.REFUSED.statusId)
        return couriemateRepository.getDoneTasksByStatusAndPickupDate(taskStatusIds)
    }

    /**
     * Sort the assigned tasks by assigned date in descending order.
     * change date: 26/02/2020
     * @author Balraj VE00YM023
     */
    fun assignedListSorter(inputList: Array<TaskRetrievalResponse>){
        GlobalScope.launch(Dispatchers.IO) {
            sortedAssignedTasks.postValue(inputList.sortedWith(ViewUtils.taskListComparatorAssignedDate))
        }
    }

    /**
     * This function is used to sort the done tasks list.
     */
    fun doneListSorter(tasks: Array<TaskRetrievalResponse>){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        var filterEndDate = Timestamp(calendar.timeInMillis)
        val timeStampFormatter = SimpleDateFormat(Constants.COURIEMATE_TIMESTAMP_FORMAT)

        GlobalScope.launch(Dispatchers.IO) {
            var sortedList = tasks
                .filter { timeStampFormatter.parse(it.endDate) >= filterEndDate }
                .sortedWith(ViewUtils.doneTasksComparator)
            sortedDoneTasks.postValue(sortedList)
        }
    }

    /**
     * Get tasks for the Delivered tab of Return tasks screen.
     * this function returns all the tasks having status delivered and
     * order status is delivering.
     *
     * @author Balraj VE00YM023
     */
    fun getAssignedReturnTasks(): LiveData<Array<TaskRetrievalResponse>> {
        return couriemateRepository.getReturnTasksByStatus(TaskStatus.DELIVERED.statusId,OrderStatus.DELIVERING.statusId)
    }

    /**
     * Get tasks for the Returned tab of Return tasks screen.
     * this function returns all the tasks having status Returned and
     * order status is delivering.
     *
     * @author Balraj VE00YM023
     */
    fun getDeliveringReturnTasks(): LiveData<Array<TaskRetrievalResponse>>{
        return couriemateRepository.getReturnTasksByStatus(TaskStatus.RETURNED.statusId,OrderStatus.DELIVERING.statusId)
    }

    /**
     * calls the API to get driver task history
     * also fetch the data from room database
     */
    fun getDriverTasksHistory(){
        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.getDriverTasksHistory()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        GlobalScope.launch(Dispatchers.IO) {
                            couriemateRepository.insertDriverTasksHistoryInRoom(it)
                        }
                    }, {
                        messageStringId.postValue(Resource.error(R.string.apiCallFailed))
                    })
            )
        }
    }

    fun getDriverTasksFromRoom(): LiveData<Array<TaskHistoryResponse>> = couriemateRepository.getDriverTasksHistoryInRoom()

    /**
     * If internet connection is available then performs the required validations and
     * then uploads the payment receipt data set to the remote server.
     */
    fun uploadPaymentRegistrationReceipt(){
        if(checkInternetConnection()) {
            val validations = Validator.validatePaymentRegistrationInput(amoundPaid.value,paymentReceivingBank.value,paymentNotes.value,paymentReceipt.value)
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }

            if(successValidation.size == validations.size) {
                showProgress.postValue(true)
                val paymentRegistrationRequest = PaymentRegistrationRequest(
                    couriemateRepository.getDriverId()!!,
                    couriemateRepository.getUserDisplayName()!!,
                    amoundPaid.value!!,
                    paymentReceivingBank.value!!,
                    paymentNotes.value!!,
                    paymentReceipt.value!!,
                    Utils.getGMTOffset()
                )
                compositeDisposable.addAll(
                    couriemateRepository.uploadPaymentRegistrationReceipt(paymentRegistrationRequest)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            showProgress.postValue(false)
                            isUploadSuccessful.postValue(Event(true))
                           amoundPaid = MutableLiveData()
                            paymentReceivingBank = MutableLiveData()
                            paymentNotes = MutableLiveData()
                            paymentReceipt = MutableLiveData()
                            messageStringEventId.postValue(Event(Resource.error(R.string.receipt_uploaded)))
                        }, {
                            showProgress.postValue(false)
                            messageStringId.postValue(Resource.error(R.string.network_default_error))
                        })
                )
            }
            else {
                val failedValidation = validations.filter { it.resource.status == Status.ERROR }[0]
                //messageStringId.postValue(failedValidation.resource)
                paymentReceiptUserAck.postValue(failedValidation.resource)

            }
        }
        else messageStringId.postValue(Resource.error(R.string.network_connection_error))
    }

    /**
     * this function will save/update
     * transaction in the remove server
     */
    fun saveTransaction(amount:String?,signature: ByteArray?,receipt: Boolean,isBankDeposit: Boolean,
                        isUpdate: Boolean,description: String?,selectedTransaction: DeliveryExpenses?){
        if(checkInternetConnection()) {
            //In case of bank deposit
            // receipt show send true by default
            if(isBankDeposit){
                isReceiptValue.value = true
            }
            //Check if anything is updated
            //only then call the API
            if(isUpdate){
                if(selectedTransaction?.amount.toString().equals(amount) && selectedTransaction?.description.equals(description) &&
                        (receipt == selectedTransaction?.isReciept)){
                    messageStringEventId.postValue(Event(Resource.error(R.string.nothing_updated)))
                    return
                }
            }

            val validations = Validator.validateTransactionRegistrationInput(amount,signature,receipt,isBankDeposit,isUpdate)
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
            if(successValidation.size == validations.size) {

                showProgress.postValue(true)
                val rowState:String
                var expenditureId:Long?=null
                if(isUpdate) {
                    rowState = Constants.EXPENSES_UPDATE
                    expenditureId = expenditureIdValue.value
                }
                else rowState = Constants.EXPENSES_INSERT

                val txRecord = DetailList(amount = amount!!.toInt(),description = description,isReciept = isReceiptValue.value?:true,itemId = itemIdValue.value!!,
                    personId = couriemateRepository.getDriverId()!!,receieverSignature = signature,rowstate = rowState,expenditureId = expenditureId)
                val list = ArrayList<DetailList>()
                list.add(txRecord)
                val updateDeliveryExpensesRequest = UpdateDeliveryExpensesRequest(source = Constants.SOURCE_MOBILE,userName = couriemateRepository.getUserName()!!,detailList = list)
                compositeDisposable.addAll(
                    couriemateRepository.updateDeliveryExpenses(updateDeliveryExpensesRequest)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            if(it.responseCode==HttpURLConnection.HTTP_OK) {
                                isUploadSuccessful.postValue(Event(true))
                                messageStringEventId.postValue(Event(Resource.error(R.string.tx_registered)))
                            }
                            else if(it.responseCode==HttpURLConnection.HTTP_BAD_REQUEST){
                                messageStringAPI.postValue(Event(it.responseData.toString()))
                            }
                            else{
                                messageStringEventId.postValue(Event(Resource.error(R.string.network_default_error)))
                            }
                            showProgress.postValue(false)
                        },{
                            showProgress.postValue(false)
                            messageStringEventId.postValue(Event(Resource.error(R.string.network_default_error)))
                        })
                )
            }
            else {
                val failedValidation = validations.filter { it.resource.status == Status.ERROR }[0]
                messageStringEventId.postValue(Event(failedValidation.resource))

            }
        }
        else messageStringEventId.postValue(Event(Resource.error(R.string.network_connection_error)))
    }

    /**
     * fetch list of transaction history of
     * expenses done by the driver for present
     * day only
     */
    fun getDeliveryExpensesList(){
        if(checkInternetConnection()){
            showProgress.postValue(true)
            compositeDisposable.addAll(
                couriemateRepository.getDeliveryExpensesList()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        showProgress.postValue(false)
                        deliveryExpensesList.postValue(it.responseData)
                    },{
                        showProgress.postValue(false)
                        messageStringEventId.postValue(Event(Resource.error(R.string.network_default_error)))
                    })
            )
        }
        else messageStringEventId.postValue(Event(Resource.error(R.string.network_connection_error)))
    }

    fun getBalanceBeforeTransaction(){
        if(checkInternetConnection()){
            showProgress.postValue(true)
            compositeDisposable.addAll(
                couriemateRepository.getBalanceBeforeTransaction()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        showProgress.postValue(false)
                        if(it.responseCode==HttpURLConnection.HTTP_OK) {
                            // post balance over UI
                            balanceValue.postValue(Event(it.responseData.balance))
                        }
                        else{
                            // post balance over UI
                            balanceValue.postValue(Event(0))
                        }
                    },{
                        showProgress.postValue(false)
                        messageStringEventId.postValue(Event(Resource.error(R.string.network_default_error)))
                    })
            )
        }
        else messageStringEventId.postValue(Event(Resource.error(R.string.network_connection_error)))
    }

    fun getTransactionConfigItems(): LiveData<Array<TransactionConfigItemEntity>> = couriemateRepository.getTransactionConfigItems()

    fun getReceiptConfigItems(): LiveData<Array<ReceiptConfigEntity>> = couriemateRepository.getReceiptConfigItems()

    /**
     * Sync the notifications before starting the MainActivity.
     * MainActivity is started irrespective of the response received from this API.
     * @author Balraj VE00YM023
     */
    fun syncNotifications() {
        val driverId = couriemateRepository.getDriverId()
        val timezoneOffset = Utils.getGMTOffset()
        val lastSync = couriemateRepository.getNotificationLastSync()
        if (driverId != null && checkInternetConnection()) {
            compositeDisposable.addAll(
                couriemateRepository.getAgentNotifications(driverId, timezoneOffset, lastSync)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        processNotificationAPIResponse(it)
                    }, {
                        Log.e(TAG, "Notifications sync failed")
                    })
            )
        }
        else { Log.e(TAG, "Notifications sync failed") }
    }

    /**
     * Performs the required operation (UPDATE, DELETE, COMPLETE or REMINDER) for every silent
     * notification packet. then inserts all the alert notifications in the ROOM db.
     * @author Balraj VE00YM023
     */
    private fun processNotificationAPIResponse(notifications: Array<NotificationResponse>)
            = GlobalScope.launch(Dispatchers.IO) {
        notificationsHandler.process(notifications.toList())
        val alertNotifications = notifications.filter { Constants.NOTIFICATION_TYPE.ALERT == it.pushType  }.toTypedArray()
        couriemateRepository.insertNotificationsInRoom(alertNotifications)
        if(notifications.isNotEmpty()){
            val lastSync = notifications.first().receivedOn?.toLastSync()
            couriemateRepository.setNotificationLastSync(lastSync)
        }
        Log.e(TAG, "Notifications sync success")
    }

    private companion object { private const val TAG = "TaskListViewModel" }


    /************************** FOLLOWING FUNCTIONS ARE USED BY CHAT FEATURE *********************/
    var xmppConfig: XMPPConfig? private set
    private var _xmppManager: XMPPManager? = null
    lateinit var xmppManager: XMPPManager private set
    var message = EMPTY_STRING
    val chatError: MutableLiveData<String> = MutableLiveData()
    var chatFragmentLoaded = false

    init {
        xmppConfig = couriemateRepository.getXMPPConfig()
    }
    override fun onCleared() {
        super.onCleared()
        _xmppManager = null
    }

    fun getXMPPConfig() = couriemateRepository.getXMPPConfig()

    fun getChatNotification() = couriemateRepository.getChatNotification()

    fun initXMPPManager(listener: ConnectionStateListener) = viewModelScope.launch(Dispatchers.IO){
        if(xmppConfig.isValid()){
            _xmppManager = XMPPManager(couriemateRepository, listener)
            xmppManager = _xmppManager!!
            xmppManager.login()
            xmppManager.setupMUC()
        }
    }

    fun reConnectXMPP(){
        if(xmppConfig.isValid() && ::xmppManager.isInitialized){
            xmppManager.login()
            xmppManager.setupMUC()
        }
    }

    fun getUserChat(): LiveData<List<ChatMessage>> = couriemateRepository.getUserChat()

    fun sendChatMessage(): Any = when {
        networkHelper.isNetworkConnected() -> {
            viewModelScope.launch(Dispatchers.IO) { xmppManager.sendMessage(message) }
        }
        else -> chatError.postValue("No network, Please try again")
    }

    fun setChatStatus(newStatus: Presence.Type) = _xmppManager?.changeStatus(newStatus)
    fun deleteNotification() {
        couriemateRepository.deleteChatNotification()
    }
}