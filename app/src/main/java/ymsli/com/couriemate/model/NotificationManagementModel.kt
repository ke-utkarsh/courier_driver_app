package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   Nov 9, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * NotificationManagementModel : This data class is used as a FCM notification data packet.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class NotificationManagementModel (
    @SerializedName("assignToVal")
    val assignToVal: Int? = null,

    @SerializedName("deliveryDistrictId")
    val deliveryDistrictId: Int? = null,

    @SerializedName("dropAddress")
    val dropAddress: String? = null,

    @SerializedName("dropAddressMemo")
    val dropAddressMemo: String? = null,

    @SerializedName("expectedDelivery")
    val expectedDelivery: String? = null,

    @SerializedName("noOfRetries")
    val noOfRetries: Int? = null,

    @SerializedName("oldDropAddress")
    val oldDropAddress: String? = null,

    @SerializedName("oldDropAddressMemo")
    val oldDropAddressMemo: String? = null,

    @SerializedName("oldExpectedDelivery")
    val oldExpectedDelivery: String? = null,

    @SerializedName("oldNoOfRetries")
    val oldNoOfRetries: Int? = null,

    @SerializedName("oldOrderStatusId")
    val oldOrderStatusId: Int? = null,

    @SerializedName("oldQuantity")
    val oldQuantity: Int? = null,

    @SerializedName("orderId")
    val orderId: Long? = null,

    @SerializedName("orderNo")
    val orderNo: String? = null,

    @SerializedName("orderStatusId")
    val orderStatusId: Int? = null,

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("sendTo")
    val sendTo: String? = null,

    @SerializedName("taskInfoFlag")
    val taskInfoFlag: Boolean? = null,

    @SerializedName("taskNo")
    val taskNo: String? = null,

    @SerializedName("taskSequenceNo")
    val taskSequenceNo: Int? = null,

    @SerializedName("taskStatus")
    val taskStatus: Int? = null,

    @SerializedName("taskStatusMessage")
    val taskStatusMessage: String? = null,

    @SerializedName("typeOfOperation")
    val typeOfOperation: String? = null,

    @SerializedName("updatedOn")
    val updatedOn: String? = null,

    @SerializedName("userName")
    val userName: String? = null
)