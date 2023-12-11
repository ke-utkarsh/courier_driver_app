package ymsli.com.couriemate.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * NotificationResponse : This is the schema of notification_master in room library.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Entity(tableName = "notification_master")
data class NotificationResponse(

    @PrimaryKey
    @ColumnInfo(name = "notificationId")
    @SerializedName("notificationId")
    val notificationId : Long,

    @ColumnInfo(name = "orderId")
    @SerializedName("orderId")
    val orderId : Long,

    @ColumnInfo(name = "orderNo")
    @SerializedName("orderNo")
    val orderNo : String,

    @ColumnInfo(name = "receivedOn")
    @SerializedName("receivedOn")
    val receivedOn : String,

    @ColumnInfo(name = "eventHeader")
    @SerializedName("eventHeader")
    val eventHeader : String,

    @ColumnInfo(name = "eventBody")
    @SerializedName("eventBody")
    val eventBody : String,

    @ColumnInfo(name = "requestData")
    @SerializedName("requestData")
    val requestData : String,

    @ColumnInfo(name = "pushType")
    @SerializedName("pushType")
    val pushType : String
)