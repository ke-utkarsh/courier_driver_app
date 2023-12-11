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
 * TaskSummaryResponse : This is the schema of tasks_summary in room library.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Entity(tableName = "tasks_summary")
data class TaskSummaryResponse (

    @PrimaryKey
    @ColumnInfo(name = "key")
    @SerializedName("key")
    val key : Int,

    @ColumnInfo(name = "assigned")
    @SerializedName("assigned")
    val assigned : Int,

    @ColumnInfo(name = "delivering")
    @SerializedName("delivering")
    val delivering : Int,

    @ColumnInfo(name = "delivered")
    @SerializedName("delivered")
    val delivered : Int,

    @ColumnInfo(name = "failed")
    @SerializedName("failed")
    val failed : Int,

    @ColumnInfo(name = "refused")
    @SerializedName("refused")
    val refused : Int,

    @ColumnInfo(name = "returned")
    @SerializedName("returned")
    val returned : Int,

    @ColumnInfo(name = "total")
    @SerializedName("total")
    val total : Int,

    @ColumnInfo(name = "totalCashReceived")
    @SerializedName("totalCashReceived")
    val totalCashReceived: Double,

    @ColumnInfo(name = "totalOtherPaymentReceived")
    @SerializedName("totalOtherPaymentReceived")
    val totalOtherPaymentReceived: Double,

    @ColumnInfo(name = "totalDeliveryFee")
    @SerializedName("totalDeliveryFee")
    val totalDeliveryFee: Double
)