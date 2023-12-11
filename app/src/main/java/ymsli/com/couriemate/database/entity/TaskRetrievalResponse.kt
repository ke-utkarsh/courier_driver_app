package ymsli.com.couriemate.database.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskRetrievalResponse : This is the schema of driver_tasks in room library.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Entity(tableName = "driver_tasks")
data class TaskRetrievalResponse(

    @ColumnInfo(name = "completed")
    @SerializedName("completed")
    val completed: Int? = null,

    @ColumnInfo(name = "createdOn")
    @SerializedName("createdOn")
    val createdOn: String? = null,

    @ColumnInfo(name = "customerAddressLatitude")
    @SerializedName("customerAdressLatitude")
    var customerAdressLatitude: Double? = null,

    @ColumnInfo(name = "customerAddressLongitude")
    @SerializedName("customerAdressLongitude")
    var customerAdressLongitude: Double? = null,

    @ColumnInfo(name = "deliveryDistrictId")
    @SerializedName("deliveryDistrictId")
    val deliveryDistrictId: Int? = null,

    @ColumnInfo(name = "dropAddress")
    @SerializedName("dropAddress")
    var dropAddress: String? = null,

    @ColumnInfo(name = "pickupLocation")
    @SerializedName("pickupLocation")
    var pickupLocation: String? = null,

    @ColumnInfo(name = "deliveryFeeAmount")
    @SerializedName("deliveryFeeAmount")
    var deliveryFeeAmount: String? = null,

    @ColumnInfo(name = "expectedCodAmount")
    @SerializedName("expectedCodAmount")
    var expectedCodAmount: Double? = null,

    @ColumnInfo(name = "failureCount")
    @SerializedName("failureCount")
    var failureCount: Int,

    @ColumnInfo(name = "noOfRetries")
    @SerializedName("noOfRetries")
    val noOfRetries: Int? = null,

    @ColumnInfo(name = "orderId")
    @SerializedName("orderId")
    var orderId: Long? = null,

    @ColumnInfo(name = "orderNo")
    @SerializedName("orderNo")
    val orderNo: String? = null,

    @ColumnInfo(name = "orderStatusId")
    @SerializedName("orderStatusId")
    var orderStatusId: Int? = null,

    @ColumnInfo(name = "orderTypeId")
    @SerializedName("orderTypeId")
    val orderTypeId: Int? = null,

    @ColumnInfo(name = "quantity")
    @SerializedName("quantity")
    val quantity: Int? = null,

    @ColumnInfo(name = "receiverAddressId")
    @SerializedName("receiverAddressId")
    var receiverAddressId: Long? = null,

    @ColumnInfo(name = "receiverId")
    @SerializedName("receiverId")
    var receiverId: Int? = null,

    @ColumnInfo(name = "receiverMobile")
    @SerializedName("receiverMobile")
    val receiverMobile: String? = null,

    @ColumnInfo(name = "receiverName")
    @SerializedName("receiverName")
    val receiverName: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "taskId")
    @SerializedName("taskId")
    var taskId: Long? = null,

    @ColumnInfo(name = "taskNo")
    @SerializedName("taskNo")
    var taskNo: String? = null,

    @ColumnInfo(name = "taskSequenceNo")
    @SerializedName("taskSequenceNo")
    val taskSequenceNo: Int? = null,

    @ColumnInfo(name = "taskStatusId")
    @SerializedName("taskStatusId")
    var taskStatusId: Int? = null,

    @ColumnInfo(name = "totalWeight")
    @SerializedName("totalWeight")
    val totalWeight: Double? = null,

    @ColumnInfo(name = "lastTask")
    @SerializedName("lastTask")
    var lastTask:Boolean? = null,

    @ColumnInfo(name = "source")
    @SerializedName("source")
    var source:String? = null,

    @ColumnInfo(name = "updatedOn")
    @SerializedName("updatedOn")
    var updatedOn:String? = null,

    @ColumnInfo(name = "updatedBy")
    @SerializedName("updatedBy")
    var updatedBy:String? = null,

    @ColumnInfo(name = "refusalComment")
    @SerializedName("refusalComment")
    var refusalComment:String?= null,

    @ColumnInfo(name = "maxTaskSequence")
    @SerializedName("maxTaskSequence")
    var maxTaskSequence:Int? = null,

    @ColumnInfo(name = "endDate")
    @SerializedName("endDate")
    var endDate:String? = null,

    @ColumnInfo(name = "timezoneOffset")
    @SerializedName("timezoneOffset")
    var timezoneOffset:String? = null,

    @ColumnInfo(name = "isSynced")
    var isSynced: Boolean? = null,// parameter to check the rows which are not synced with remote DB

    @ColumnInfo(name = "actualDelivery")
    @SerializedName("actualDelivery")
    var actualDelivery:String? = null,

    @ColumnInfo(name = "driverCodAmount")
    @SerializedName("driverCodAmount")
    var driverCodAmount:Double? = null,

    @ColumnInfo(name = "addressPresent")
    @SerializedName("addressPresent")
    var addressPresent:Boolean? = null,

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    var latitude:Double? = null,

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    var longitude:Double? = null,

    @ColumnInfo(name = "failureComment")
    @SerializedName("failureComment")
    var failureComment:String? = null,

    @ColumnInfo(name = "failureReason")
    @SerializedName("failureReason")
    var failureReason:String? = null,

    @ColumnInfo(name = "expectedDeliverySlot")
    @SerializedName("expectedDeliverySlot")
    var expectedDeliverySlot:String? = null,

    @ColumnInfo(name = "startDate")
    @SerializedName("startDate")
    var startDate:String? = null,

    @ColumnInfo(name = "taskMemo")
    @SerializedName("taskMemo")
    var taskMemo:String? = null,

    @ColumnInfo(name = "receiverOtherPayment")
    @SerializedName("receiverOtherPayment")
    var receiverOtherPayment:Double? = null,

    @ColumnInfo(name = "receiverOtherPaymentMode")
    @SerializedName("receiverOtherPaymentMode")
    var receiverOtherPaymentMode:Int? = null,

    @ColumnInfo(name = "refuseReason")
    @SerializedName("refuseReason")
    var refuseReason:String? = null,

    @ColumnInfo(name = "assignedDate")
    @SerializedName("assignedDate")
    var assignedDate:String? = null,

    @ColumnInfo(name = "deliveryFeePaidBy")
    @SerializedName("deliveryFeePaidBy")
    var deliveryFeePaidBy:String? = null,

    @SerializedName("expectedDelivery")
    var expectedDelivery: String? = null,

    @SerializedName("expectedStart")
    val expectedStart: String? = null,

    @SerializedName("pickupDistrict")
    val pickupDistrict: String? = null,

    @SerializedName("dropDistrict")
    val dropDistrict: String? = null,

    @SerializedName("orderMemo")
    var orderMemo: String? = null,

    @SerializedName("senderDistrict")
    var senderDistrict: String? = null,

    @SerializedName("receiverDistrict")
    var receiverDistrict: String? = null,

    @ColumnInfo(name = "signature")
    @SerializedName("signature")
    var signature:ByteArray? = null,

    @SerializedName("deliveryFeeWithTax")
    var deliveryFeeWithTax: String? = null,

    @SerializedName("items")
    var items : List<OrderItem>?=null

):Serializable
data class OrderItem(
    @SerializedName("orderId")
    val orderId : String,

    @SerializedName("quantity")
    val quantity: String,

    @SerializedName("weight")
    val weight: String,

    @SerializedName("name")
    val name: String
):Serializable