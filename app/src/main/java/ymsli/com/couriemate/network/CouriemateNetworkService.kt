package ymsli.com.couriemate.network

import ymsli.com.couriemate.database.entity.*
import ymsli.com.couriemate.model.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface CouriemateNetworkService {

    @GET("appversion/getstatus")
    fun fetchAppStatusAndApiInfo(@Query("appVersion") appVersion:String): Single<AppVersionResponse>

    @Headers("Accept: */*")
    @POST("login")
    fun doLogin(@Body user: UserMaster):Single<Response<APIResponse>>

    @GET("user/usermanagement/get-user-info")
    fun userInfo(@Query("userId") userId:Long):Single<List<UserMaster>>

    @Headers("Content-Type: application/json")
    @POST("task/taskmanagement/retrieve")
    fun getDriverTasks(@Body taskRetrievalRequest: TaskRetrievalRequest, @Header("Authorization") jwtToken : String?):Single<Array<TaskRetrievalResponse>>

    @POST("order/ordermanagement/updateorderandtask")
    fun syncTasks(@Body taskEntity: Array<TaskRetrievalResponse>, @Header("Authorization") jwtToken : String?):Single<Array<TaskRetrievalResponse>>


    @POST("notification/notificationmanagement/generate-packets-to-send-notification")
    //@POST("notification/notificationmanagement/generatePacketsToSendNotification")
    fun sendNotification(@Body notificationPacket: NotificationManagementModelWrapper, @Header("Authorization") jwtToken : String?): Single<String>

    @PUT("user/usermanagement/change-password")
    //@PUT("user/usermanagement/changePassword")
    fun changePassword(@Body changePasswordRequestRequest: ChangePasswordRequestDTO,
                       @Header("Authorization") jwtToken : String?)
            : Single<ChangePasswordResponseDTO>

    @GET("common/get-company-details")
    //@GET("common/getCompanyDetails")
    fun getCompanyDetails():Single<CompanyDetails>

    @POST("common/store-device-token")
    //@POST("common/storeDeviceToken")
    fun updateFCMToken(@Body request: UserTokenMapping,
                       @Header("Authorization") jwtToken : String?): Single<Int>

    @POST("common/remove-device-token")
    //@POST("common/removeDeviceToken")
    fun removeDeviceToken(@Body request: UserTokenMapping,
                          @Header("Authorization") jwtToken : String?): Single<Int>

    @GET("task/taskmanagement/get-driver-task-history")
    //@GET("task/taskmanagement/getDriverTaskHistory")
    fun getDriverTasksHistory(@Query("driverId") driverId: Int?,@Query("timezoneOffset")timezoneOffset: String?,
                              @Header("Authorization")jwtToken: String?):Single<Array<TaskHistoryResponse>>

    @GET("task/taskmanagement/get-driver-task-summary")
    //@GET("task/taskmanagement/getDriverTaskSummary")
    fun getDriverTaskSummary(@Query("driverId") driverId: Int,
                             @Header("Authorization")jwtToken: String?):Single<Array<TaskSummaryResponse>>

    @GET("common/get-code-master")
    //@GET("common/getCodeMaster")
    fun getDropDownData(@Query("dataId") dataId: Int,
                        @Header("Authorization")jwtToken: String?):Single<CodeMasterResponse>

    @GET("notification/notificationmanagement/get-agent-notifications")
    //@GET("notification/notificationmanagement/getAgentNotifications")
    fun getAgentNotifications(@Query("driverId") driverId: Int,
                              @Query("timezoneOffset") timezoneOffset: String,
                              @Query("receivedOn") receivedOn: String?,
                              @Header("Authorization")jwtToken: String?)
            :Single<Array<NotificationResponse>>

    @POST("aws/store-payment-receipt")
    //@POST("aws/storePaymentReceipt")
    fun uploadPaymentRegistrationReceipt(@Body paymentReg: PaymentRegistrationRequest,@Header("Authorization") jwtToken : String?): Single<PaymentRegResponse>


    @POST
    fun sendLocationToServer(@Url url: String,@Body location: LocationTrackingRequest): Single<Any>

    @GET
    fun getLocationConfiguration(@Url serverUrl: String,@Query("companyCode") companyCode:String,@Query("thirdPartySysRiderId") thirdPartySysRiderId:String): Single<LocationConfigResponseModel>

    @POST("order/ordermanagement/get-location-link")
    fun getSMSBody(@Body locationShareRequest:LocationSharingRequest,@Header("Authorization") jwtToken : String?):Single<LocationSharingResponse>

    @GET("report/get-delivery-expenditure")
    fun getDeliveryExpensesList(@Query("personId") personId: String,@Query("source") source: String): Single<DeliveryExpensesResponse>

    @POST("report/update-delivery-expenditure")
    fun updateDeliveryExpenses(@Header("Authorization") jwtToken : String?,@Body deliveryUpdateRequest: UpdateDeliveryExpensesRequest): Single<DeliveryExpensesResponse>

    @GET("report/driver-current-balance")
    fun getBalanceBeforeTransaction(@Query("driverId") personId: String,@Query("source") source: String): Single<BalanceBeforeTransactionResponse>



}