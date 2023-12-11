package ymsli.com.couriemate.repository

import android.util.Log
import ymsli.com.couriemate.BuildConfig
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import ymsli.com.couriemate.database.CouriemateDatabase
import ymsli.com.couriemate.database.entity.*
import ymsli.com.couriemate.model.*
import ymsli.com.couriemate.network.CouriemateNetworkService
import ymsli.com.couriemate.preferences.CouriemateSharedPreferences
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Utils
import io.reactivex.Single
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import ymsli.com.couriemate.network.DAPIoTNetworkService
import ymsli.com.couriemate.utils.common.Constants.Companion.DEFAULT_TRIP_ID
import ymsli.com.couriemate.views.chat.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @date   Oct 15, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * CouriemateRepository : acts as an intermediate layer between view models and dataList source.
 * Data source can be shared pref, room db or API calls via retrofit2 library
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * Balraj VE00YM023     Jan 30, 2020        Adjusted the structuring of functions
 * -----------------------------------------------------------------------------------
 */
@Singleton
class CouriemateRepository @Inject constructor(
    private val networkService: CouriemateNetworkService,
    private val couriemateSharedPreferences: CouriemateSharedPreferences,
    private val couriemateDatabase: CouriemateDatabase,
    private val dapIoTNetworkService: DAPIoTNetworkService
){

    /********************************************************************************************/
    /*********** Following functions geLt/save data from/to shared prefs  *************** */

    fun isLoggedIn():Boolean{
        return couriemateSharedPreferences.isLoggedIn()
    }

    fun getInitStatus():Boolean{
        return couriemateSharedPreferences.getInitStatus()
    }

    fun getCCUID(): String? = couriemateSharedPreferences.getCCUID()

    fun setUserDataInSharedPref(user: UserMaster){
        couriemateSharedPreferences.setUserDataInSharedPref(user)
    }

    fun getApiVersion(): String{
        return (couriemateSharedPreferences.getApiVersion()) ?: Constants.EMPTY_STRING
    }

    fun setApiVersionInSharedPref(appVersionResponse: AppVersionResponse){
        couriemateSharedPreferences.setApiVersionInSharedPref(appVersionResponse)
    }

    /** stores/updates the value of last sync date-time in Shared preference */
    fun setUpdatedOnInSharedPreference(updatedOn : String){
        couriemateSharedPreferences.setUpdatedOnInSharedPreference(updatedOn)
    }

    fun getUpdatedOnFromSharedPreference():String?{
        return couriemateSharedPreferences.getUpdatedOnFromSharedPreference()
    }

    /** erases dataList from Shared Pref and Room once the use performs logout operation */
    fun eraseLogoutData(){
        removeDriverHistoryTasks()
        clearDriverTaskSummary()
        clearNotificationsFromRoom()
        setActiveNotificationCount("0")
        couriemateSharedPreferences.eraseLogoutDataFromSharedPreferences()
    }

    fun getUserName(): String?{
        return couriemateSharedPreferences.getUserName()
    }

    fun getUserPassword():String?{
        return couriemateSharedPreferences.getUserPassword()
    }

    fun getDriverId():Int?{
        return couriemateSharedPreferences.getDriverId()
    }

    fun getUserType() = couriemateSharedPreferences.getUserType()

    fun getCompanyContactNumber():String? = couriemateSharedPreferences.getCompanyContactNumber()

    fun getCompanyAddress() = couriemateSharedPreferences.getCompanyAddress()

    fun getUnreachableCustomerString() = couriemateSharedPreferences.getUnreachableCustomerString()

    fun saveCompanyDetails(companyDetails: CompanyDetails)
            = couriemateSharedPreferences.saveCompanyDetails(companyDetails)

    fun updateUserPassword(newPassword: String){
        couriemateSharedPreferences.updateUserPassword(newPassword)
    }

    private fun getFCMToken(): String?{
        return couriemateSharedPreferences.getFCMToken()
    }

    fun updateFCMToken(token: String){
        couriemateSharedPreferences.updateFCMToken(token)
    }

    fun getUserContact(): String?{
        return couriemateSharedPreferences.getUserContact()
    }

    fun setAuthorizationToken(authorizationToken : String){
        couriemateSharedPreferences.setAuthorizationToken(authorizationToken)
    }

    fun getAuthToken() = couriemateSharedPreferences.getAuthorizationToken()

    fun getSyncWorkerIteration(): Int{
        return couriemateSharedPreferences.getSyncWorkerIteration()
    }

    fun setSyncWorkerIteration(iteration: Int){
        couriemateSharedPreferences.setSyncWorkerIteration(iteration)
    }

    fun updateDropDownData(refuseReason: String, mobileMoney: String) {
        couriemateSharedPreferences.updateDropDownData(refuseReason, mobileMoney)
    }

    fun getRefuseReasons() = couriemateSharedPreferences.getRefuseReasons()

    fun getMobileMoneyTypes() = couriemateSharedPreferences.getMobileMoneyTypes()

    fun getActiveNotificationCount() = couriemateSharedPreferences.getActiveNotificationCount()

    fun setActiveNotificationCount(count: String){
        couriemateSharedPreferences.setActiveNotificationCount(count)
    }


    /********************************************************************************************/
    /*********** Following functions get/save data from/to Room database *************** */


    /** inserts driver tasks in room database */
    fun insertDriverTasksInRoom(tasks : Array<TaskRetrievalResponse>){
        couriemateDatabase.taskRetrievalDao().insertDriverTasksInRoom(*tasks)
    }

    /**
     * fetch the tasks or deliverables assigned to the driver from room database
     * @param taskPattern string specifying the type of tasks required.
     *                   to get forward tasks pass "T" and for return tasks
     *                   pass "R"
     */
    fun getDriverTasks(taskPattern: String): LiveData<Array<TaskRetrievalResponse>> {
        return couriemateDatabase.taskRetrievalDao().getDriverTasks(taskPattern)
    }

    /** erases all the entries from room database upon refresh button tap */
    fun clearDriverTasks(){
        couriemateDatabase.taskRetrievalDao().deleteDriverTasks()
    }

    /** updates task in room db before making API call */
    fun updateTasks(taskEntity: Array<TaskRetrievalResponse>){
        couriemateDatabase.taskRetrievalDao().insertDriverTasksInRoom(*taskEntity)
    }

    fun updateSyncStatus(taskId: Long, isSynced: Boolean){
        couriemateDatabase.taskRetrievalDao().updateSyncStatus(taskId, isSynced)
    }

    fun deleteAssignedTokenByFCM(taskId: Int, orderId: Int){
        couriemateDatabase.taskRetrievalDao().deleteAssignedTask(taskId, orderId)
    }

    fun setTaskPickableByFCM(orderId: Int){
        couriemateDatabase.taskRetrievalDao().setTaskPickable(orderId)
    }

    /**
     * Used to update the order master table when any column
     * of order table has been modified by the operator
     *
     * @author Balraj VE00YM023
     */
    fun updateOrderDataByFCM(jsonObject: JSONObject) {
        if(jsonObject.has("orderStatus") && jsonObject["orderStatus"].toString() != "null"){
            couriemateDatabase.taskRetrievalDao()
                .updateOrderStatusId(jsonObject["orderStatus"].toString().toInt(),
                                     jsonObject["orderId"].toString().toInt())
        }
        if (jsonObject.has("dropAddress") && jsonObject["dropAddress"].toString() != "null"){
            couriemateDatabase.taskRetrievalDao()
                .updateLastTaskDeliveryAddress(jsonObject["dropAddress"].toString(),
                                               jsonObject["orderId"].toString().toInt())
        }
        if(jsonObject.has("noOfRetries") && jsonObject["noOfRetries"].toString() != "null"){
            couriemateDatabase.taskRetrievalDao()
                .updateNoOfRetries(jsonObject["noOfRetries"].toString().toInt(),
                                   jsonObject["orderId"].toString().toInt())
        }
        if(jsonObject.has("itemQuantity") && jsonObject["itemQuantity"].toString() != "null"){
            couriemateDatabase.taskRetrievalDao()
                .updateItemQuantity(jsonObject["itemQuantity"].toString().toInt(),
                                    jsonObject["orderId"].toString().toInt())
        }
        if(jsonObject.has("senderMemo") && jsonObject["senderMemo"].toString() != "null"){
            couriemateDatabase.taskRetrievalDao()
                .updateOrderMemoByOrderId(jsonObject["orderId"].toString().toLong(),
                    jsonObject["senderMemo"].toString())
        }
    }

    fun getNotSyncedRecords(): Array<TaskRetrievalResponse>{
        return couriemateDatabase.taskRetrievalDao().getNotSyncedRecords()
    }

    fun getTasksByStatusAndPickupDate(taskStatusIds: List<Int>)
            : LiveData<Array<TaskRetrievalResponse>> {
        return couriemateDatabase.taskRetrievalDao().getTasksByStatusAndPickupDate(taskStatusIds)
    }

    fun getDoneTasksByStatusAndPickupDate(taskStatusIds: List<Int>)
            : LiveData<Array<TaskRetrievalResponse>> =
        couriemateDatabase.taskRetrievalDao().getDoneTasksByStatusAndPickupDate(taskStatusIds)

    fun getReturnTasksByStatus(taskStatusId: Int,orderStatusId: Int): LiveData<Array<TaskRetrievalResponse>>{
        return couriemateDatabase.taskRetrievalDao().getReturnTasksByStatus(taskStatusId,orderStatusId)
    }

    fun insertDriverTasksHistoryInRoom(historyTasks: Array<TaskHistoryResponse>){
        couriemateDatabase.taskHistoryDao().insertDriverHistoryTasksInRoom(*historyTasks)
    }

    fun getDriverTasksHistoryInRoom(): LiveData<Array<TaskHistoryResponse>>
            = couriemateDatabase.taskHistoryDao().getDriverHistoryTasks()

    fun insertDriverTaskSummaryInRoom(summary: Array<TaskSummaryResponse>){
        couriemateDatabase.taskSummaryDao().insertDriverTaskSummary(*summary)
    }

    fun getDriverTaskSummaryByPeriod(periodId: Int): TaskSummaryResponse {
        return couriemateDatabase.taskSummaryDao().getDriverTaskSummaryByPeriod(periodId)
    }

    private fun removeDriverHistoryTasks()
            = couriemateDatabase.taskHistoryDao().removeDriverHistoryTasks()

    fun getUserDisplayName() = couriemateSharedPreferences.getUserDisplayName()

    private fun clearDriverTaskSummary()
            = couriemateDatabase.taskSummaryDao().clearDriverTaskSummary()

    fun insertNotificationsInRoom(notifications: Array<NotificationResponse>) {
        couriemateDatabase.notificationsDao().insertNotificationsInRoom(*notifications)
        couriemateDatabase.notificationsDao().truncate()
    }

    fun clearNotificationsFromRoom(){
        couriemateDatabase.notificationsDao().clearNotifications()
    }

    fun getAgentNotificationsFromRoom(): Array<NotificationResponse>{
        return couriemateDatabase.notificationsDao().getNotifications()
    }

    /********************************************************************************************/
    /*********** Following functions use network service to fetch/upload data From remote server */

    fun fetchAppStatusAndApiInfo():Single<AppVersionResponse> =
        networkService.fetchAppStatusAndApiInfo(BuildConfig.VERSION_NAME)

    fun getDropDownData(dataId: Int) = networkService.getDropDownData(dataId,
        couriemateSharedPreferences.getAuthorizationToken())

    fun doLogin(user: UserMaster):Single<Response<APIResponse>>{
        return networkService.doLogin(user)
    }

    fun getCompanyDetails():Single<CompanyDetails>{
        return networkService.getCompanyDetails()
    }

    fun userInfo(userId: Long):Single<List<UserMaster>>{
        return networkService.userInfo(userId)
    }

    fun updateFCMTokenOnServer(request: UserTokenMapping): Single<Int> {
        return networkService.updateFCMToken(request,
            couriemateSharedPreferences.getAuthorizationToken())
    }

    fun getDriverTasks(taskRetrievalRequest: TaskRetrievalRequest):Single<Array<TaskRetrievalResponse>>{
        return networkService.getDriverTasks(taskRetrievalRequest,couriemateSharedPreferences.getAuthorizationToken())
    }

    fun syncTasks(taskEntity: Array<TaskRetrievalResponse>):Single<Array<TaskRetrievalResponse>>{
        return networkService.syncTasks(taskEntity,couriemateSharedPreferences.getAuthorizationToken())
    }

    fun sendNotification(notificationPacket: NotificationManagementModelWrapper): Single<String>{
        return networkService.sendNotification(notificationPacket,
            couriemateSharedPreferences.getAuthorizationToken())
    }

    fun getDriverTasksHistory():Single<Array<TaskHistoryResponse>>{
        return networkService.getDriverTasksHistory(couriemateSharedPreferences.getDriverId(),
            Utils.getTimeZoneOffset(), couriemateSharedPreferences.getAuthorizationToken())
    }

    /** Retrieves driver task summary from remote server */
    fun getDriverTaskSummaryFromServer(driverId: Int): Single<Array<TaskSummaryResponse>> {
        return networkService.getDriverTaskSummary(driverId,
            couriemateSharedPreferences.getAuthorizationToken())
    }

    /**
     * Retrieves the notifications for this agent from the remote server.
     * @author Balraj VE00YM023
     */
    fun getAgentNotifications(driverId: Int, timezoneOffset: String, lastSync: String?): Single<Array<NotificationResponse>> {
        return networkService.getAgentNotifications(driverId, timezoneOffset,lastSync,
            couriemateSharedPreferences.getAuthorizationToken())
    }

    fun uploadPaymentRegistrationReceipt(paymentReg: PaymentRegistrationRequest): Single<PaymentRegResponse> =
        networkService.uploadPaymentRegistrationReceipt(paymentReg,couriemateSharedPreferences.getAuthorizationToken())

    fun changePassword(request: ChangePasswordRequestDTO): Single<ChangePasswordResponseDTO>{
        return networkService.changePassword(request,
            couriemateSharedPreferences.getAuthorizationToken())
    }

    /**
     * Removes the FCM token associated with this device from remote server.
     * This function is called on log out.
     * @author Balraj VE00YM023
     */
    fun removeDeviceToken(): Single<Int> {
        return networkService.removeDeviceToken(
            UserTokenMapping(deviceToken = getFCMToken()),
            couriemateSharedPreferences.getAuthorizationToken())
    }


    /**
     * When order memo field is updated for a task, all the other
     * tasks associated with same order id must also be updated.
     *
     * This is done when user updates order memo from task detail screen,
     * or a notification is received when operator has updated this field from back end.
     *
     * @author Balraj VE00YM023
     */
    fun updateOrderMemoByOrderId(orderId: Long, orderMemo: String)
            = couriemateDatabase.taskRetrievalDao().updateOrderMemoByOrderId(orderId, orderMemo)

    //region trip methods
    fun getOnGoingTrip():Array<TripEntity> = couriemateDatabase.tripDao().getOnGoingTrip()

    fun getTripLastLocation(tripId: String):LatLongEntity = couriemateDatabase.latLongDao().getTripLastLocation(tripId)

    fun insertNewTrip(tripEntity: TripEntity) = couriemateDatabase.tripDao().insertNewTrip(tripEntity)

    fun getPotentialLastTrip(): TripEntity? = couriemateDatabase.tripDao().getPotentialLastTrip()

    fun insertNewLocation(location: LatLongEntity) = couriemateDatabase.latLongDao().insertNewLocation(location)

    fun updatePotentialTripEndCoordinates(potentialEndTime: Long,potentialLastLatitude: Double,potentialLastLongitude: Double,distanceCovered: Float, tripId: String) =
        couriemateDatabase.tripDao().updatePotentialTripEndCoordinates(potentialEndTime,potentialLastLatitude, potentialLastLongitude,distanceCovered, tripId)

    fun insertNewGyro(gyro: GyroEntity) =  couriemateDatabase.gyroDao().insertNewGyro(gyro)

    fun insertNewAccel(accelerometerEntity: AccelerometerEntity) = couriemateDatabase.accelerometerDao().insertNewAccel(accelerometerEntity)

    fun getUnsyncedFiles(): Array<DAPIoTFileEntity> = couriemateDatabase.dapIoTFileDao().getUnsyncedFiles()

    fun removeFileEntry(fileName: String) = couriemateDatabase.dapIoTFileDao().removeFileEntry(fileName)

    fun getUnsyncedGyroEntity(): Array<GyroEntity> = couriemateDatabase.gyroDao().getUnsyncedGyroEntity()

    fun removeSyncedGyroData(id: Long) = couriemateDatabase.gyroDao().removeSyncedGyroData(id)

    fun clearGyroTable() = couriemateDatabase.gyroDao().clearGyroTable()

    fun getUnsyncedAccelEntity(): Array<AccelerometerEntity> = couriemateDatabase.accelerometerDao().getUnsyncedAccelEntity()

    fun removeSyncedAccelData(id: Long) = couriemateDatabase.accelerometerDao().removeSyncedAccelData(id)

    fun clearAccelTable() = couriemateDatabase.accelerometerDao().clearAccelTable()

    fun getCCUIDForTrip(): String = "A000${getCCUID()}"

    fun getUnsyncedDAPTrips(): Array<LatLongEntity> = couriemateDatabase.latLongDao().getUnsyncedTrips()

    fun updateTripParameter(locationId: Long) = couriemateDatabase.latLongDao().updateTripParameter(locationId)

    fun storeFileInfo(file: DAPIoTFileEntity) = couriemateDatabase.dapIoTFileDao().storeFileInfo(file)

    fun updateFileEntity(id: Long,nextTry: Long,retryAttempts: Int) = couriemateDatabase.dapIoTFileDao().updateFileEntity(id,nextTry, retryAttempts)

    fun getFileEntity(fileName: String): DAPIoTFileEntity? = couriemateDatabase.dapIoTFileDao().getFileEntity(fileName)

    fun sentLocationDataToDAPIoTServer(xuid: String, contentDisposition: String, fileBody: RequestBody): Observable<DAPIoTFileUploadResponse> {
        return dapIoTNetworkService.sentLocationDataToDAPIoTServer(
            "application/octet-stream",
            "application/octet-stream", xuid, contentDisposition, fileBody
        )
    }

    fun setTripStatusInSharedPrefs(isTripRunning: Boolean) = couriemateSharedPreferences.setTripStatusInSharedPrefs(isTripRunning)

    fun getTripStatusInSharedPrefs(): Boolean = couriemateSharedPreferences.getTripStatusInSharedPrefs()

    fun storeDeviceId(deviceId: String){
        couriemateSharedPreferences.setStoreDeviceId(deviceId)
    }

    fun getDeviceId(): String? = couriemateSharedPreferences.getStoreDeviceId()

    //endregion trip methods

    /**
     * Sends captured device location to the remote server (for the purpose of live tracking).
     * @author Balraj VE00YM023
     */
    fun sendLocationToServer(request: LocationTrackingRequest): Single<Any> {
        return networkService.sendLocationToServer(getLocationHistoryURL(),request)
    }

    /**
     * Getter and setter for 'TripId'.
     * TripId is used by the live location API request.
     * @author Balraj VE00YM023
     */
    fun setTripId(tripId: String) = couriemateSharedPreferences.setTripId(tripId)
    fun getTripId() = couriemateSharedPreferences.getTripId() ?: DEFAULT_TRIP_ID

    fun getCompanyCode(): String = couriemateSharedPreferences.getCompanyCode()
    fun getUserId() = couriemateSharedPreferences.getUserId()

    fun getCodeMasterConfig(dataId: Int):Single<CodeMasterResponse> = networkService.getDropDownData(dataId,couriemateSharedPreferences.getAuthorizationToken())

    //region store trip configs
    fun setMinimumDuration(duration: Int) = couriemateSharedPreferences.setMinimumDuration(duration)

    fun getMinimumDuration(): Int = couriemateSharedPreferences.getMinimumDuration()

    fun setMinimumDistance(distance: Int) = couriemateSharedPreferences.setMinimumDistance(distance)

    fun getMinimumDistance(): Int = couriemateSharedPreferences.getMinimumDistance()

    fun setLocationHistoryURL(url: String) = couriemateSharedPreferences.setLocationHistoryURL(url)

    fun getLocationHistoryURL(): String = couriemateSharedPreferences.getLocationHistoryURL()?:"http://uat-vts-api.ap-south-1.elasticbeanstalk.com/save-location-history"
    //endregion store trip configs

    //region location configuration
    fun getLocationConfiguration(userId: Long,serverURL: String) : Single<LocationConfigResponseModel> = networkService.getLocationConfiguration(serverURL,getCompanyCode(),userId.toString())

    fun setSendLocation(sendLocation: Boolean) = couriemateSharedPreferences.setSendLocation(sendLocation)

    fun getSendLocation() : Boolean = couriemateSharedPreferences.getSendLocation()

    fun setLocationInterval(interval: Int) = couriemateSharedPreferences.setLocationInterval(interval)

    fun getLocationInterval(): Int = couriemateSharedPreferences.getLocationInterval()

    //save last api calling time
    fun setLastAPICallingTime(interval: Long) = couriemateSharedPreferences.setLastAPICallingTime(interval)

    fun getLastAPICallingTime(): Long = couriemateSharedPreferences.getLastAPICallingTime()


    fun setLocationSpeed(speed: Int) = couriemateSharedPreferences.setLocationSpeed(speed)

    fun getLocationSpeed() : Int = couriemateSharedPreferences.getLocationSpeed()

    fun setSleepModeTime(sleepModeTime: Int) = couriemateSharedPreferences.setSleepModeTime(sleepModeTime)

    fun getSleepModeTime() : Int = couriemateSharedPreferences.getSleepModeTime()

    fun setLastLocationTimestamp(lastLocationTime: Long) = couriemateSharedPreferences.setLastLocationTimestamp(lastLocationTime)

    fun getLastLocationTimestamp() : Long = couriemateSharedPreferences.getLastLocationTimestamp()

    fun getSMSBody(locationSharingRequest: LocationSharingRequest): Single<LocationSharingResponse> = networkService.getSMSBody(locationSharingRequest,couriemateSharedPreferences.getAuthorizationToken())

    fun setLocationConfigServerURL(serverURL: String) = couriemateSharedPreferences.setLocationConfigServerURL(serverURL)

    fun getLocationConfigServerURL(): String? = couriemateSharedPreferences.getLocationConfigServerURL()
    //endregion location configuration

    /* Getter and setter for notifications API last sync value */
    fun getNotificationLastSync() = couriemateSharedPreferences.getNotificationLastSync()
    fun setNotificationLastSync(lastSync: String?) = couriemateSharedPreferences.setNotificationLastSync(lastSync)

    /**
     * Inserts the processed notification id in the room database table.
     * we need to store this data in room so that if we receive the same notification again
     * we don't process it.
     * After insertion we truncate the table to only contain records from last two months.
     *
     * @param notificationId
     * @author Balraj VE00YM023
     */
    fun insertProcessedNotification(notificationId: Long){
        val currentMillis = System.currentTimeMillis()
        val constraintMillis = currentMillis - Constants.TWO_MONTH_MILLIS
        couriemateDatabase.processedNotificationDao()
            .insertProcessedNotificationId(ProcessedNotification(notificationId, currentMillis))
        couriemateDatabase.processedNotificationDao()
            .truncateByCreatedOn(constraintMillis)
    }

    /**
     * Checks if the notification with given notificationId have been processed.
     * @param notificationId notification to check
     * @return true if the notification have already been processed, false otherwise
     * @author Balraj VE00YM023
     */
    fun isNotificationProcessed(notificationId: Long): Boolean{
        return couriemateDatabase.processedNotificationDao()
            .isProcessed(notificationId)
    }

    //region delivery expenses
    fun getDeliveryExpensesList(): Single<DeliveryExpensesResponse> = networkService.getDeliveryExpensesList(getDriverId().toString(),Constants.SOURCE_MOBILE)

    fun insertNewItemConfig(configItems: Array<TransactionConfigItemEntity>) = couriemateDatabase.transactionConfigItemDao().insertNewItemConfig(*configItems)

    fun insertNewReceiptConfig(configItems: Array<ReceiptConfigEntity>) = couriemateDatabase.receiptConfigDao().insertNewReceiptConfig(*configItems)

    fun getTransactionConfigItems(): LiveData<Array<TransactionConfigItemEntity>> = couriemateDatabase.transactionConfigItemDao().getTransactionConfigItems()

    fun getReceiptConfigItems(): LiveData<Array<ReceiptConfigEntity>> = couriemateDatabase.receiptConfigDao().getReceiptConfigItems()

    fun updateDeliveryExpenses(deliveryUpdateRequest: UpdateDeliveryExpensesRequest): Single<DeliveryExpensesResponse> = networkService.updateDeliveryExpenses(couriemateSharedPreferences.getAuthorizationToken(),deliveryUpdateRequest)

    fun getTransactionConfigValue(codeValue:Int): String? = couriemateDatabase.transactionConfigItemDao().getTransactionConfigValue(codeValue)

    fun getTransactionConfigItem(codeValue: Int): LiveData<TransactionConfigItemEntity> = couriemateDatabase.transactionConfigItemDao().getTransactionConfigItem(codeValue)

    fun getBalanceBeforeTransaction(): Single<BalanceBeforeTransactionResponse> = networkService.getBalanceBeforeTransaction(getDriverId().toString(),Constants.SOURCE_MOBILE)
    //endregion delivery expenses

    /******************************** XMPP Chat related methods *****************************/

    fun saveXMPPConfig(xmppConfig: XMPPConfig) = couriemateDatabase.xmppConfigDao().insertXMPPConfig(xmppConfig)

    /**
     * Returns the XMPP configuration required for chat.
     * this will include username, password, server domain etc.
     * @author Balraj VE00YM023
     */
    fun getXMPPConfig() = couriemateDatabase.xmppConfigDao().getXMPPConfig()
    fun truncateXMPPConfig() = couriemateDatabase.xmppConfigDao().truncate()

    fun setXMPPConfigHistoryRestored() = couriemateDatabase.xmppConfigDao().setHistoryRestored()

    fun getUserChat() = couriemateDatabase.chatDao().getChatLive()

    /**
     * Inserts the received chat message in local Room database.
     * UI can be populated reactively using this source.
     * @author Balraj VE00YM023
     */
    fun saveChatMessage(id: String, senderJid: String,
                        receiverJed: String, body: String, timestamp: Long){
        val message = ChatMessage(id, senderJid, receiverJed,
            body, timestamp, ChatMessage.NOT_DELIVERED)
        couriemateDatabase.chatDao().insert(message)
    }

    fun deleteUserChat() = couriemateDatabase.chatDao().truncateChat()

    /*************************** XMPP Chat notifications related functions ********************/
    fun insertChatNotification(message: ChatNotification) {
        couriemateDatabase.chatNotificationDao().insert(message)
    }

    fun getChatNotification() = couriemateDatabase.chatNotificationDao().getNotification()

    fun deleteChatNotification() = couriemateDatabase.chatNotificationDao().delete()


    fun insertOfflineLocation(locationTrackingRequest: LocationTrackingRequest){
        couriemateDatabase.locationTrackingDao().
        insertOfflineLocation(locationTrackingRequest)
    }

    fun getOfflineLocations(): Array<LocationTrackingRequest> = couriemateDatabase.locationTrackingDao()
        .getOfflineLocations()

    fun deleteOfflineLocation(deviceTimeStamp:String) = couriemateDatabase.locationTrackingDao()
        .removeOfflineLocations(deviceTimeStamp)

}

