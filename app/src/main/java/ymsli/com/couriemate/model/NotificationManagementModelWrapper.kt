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
 * NotificationManagementModelWrapper : This data class is used as a wrapper for
 *                                      NotificationManagementModel to send notifications
 *                                      to the web and other android devices.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class NotificationManagementModelWrapper (
    @SerializedName("notificationManagementModelList")
    val notificationManagementModelList: kotlin.Array<NotificationManagementModel>? = null,
    @SerializedName("source")
    val source: kotlin.String? = null,
    @SerializedName("timezoneOffset")
    val timezoneOffset: kotlin.String? = null,
    @SerializedName("userName")
    val userName: kotlin.String? = null
)