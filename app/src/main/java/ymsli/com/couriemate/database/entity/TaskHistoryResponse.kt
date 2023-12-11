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
 * TaskHistoryResponse : This is the schema of tasks_history in room library.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Entity(tableName = "tasks_history")
data class TaskHistoryResponse (

    @ColumnInfo(name = "orderId")
    @SerializedName("orderId")
    val orderId : Long,

    @ColumnInfo(name = "orderNo")
    @SerializedName("orderNo")
    val orderNo : String,

    @ColumnInfo(name = "orderStatusId")
    @SerializedName("orderStatusId")
    val orderStatusId : Int,

    @PrimaryKey
    @ColumnInfo(name = "taskId")
    @SerializedName("taskId")
    val taskId : Long,

    @ColumnInfo(name = "taskNo")
    @SerializedName("taskNo")
    val taskNo : String,

    @ColumnInfo(name = "taskStatusId")
    @SerializedName("taskStatusId")
    val taskStatusId : Int,

    @ColumnInfo(name = "taskSequenceNo")
    @SerializedName("taskSequenceNo")
    val taskSequenceNo : Int,

    @ColumnInfo(name = "deliveryLocation")
    @SerializedName("deliveryLocation")
    val deliveryLocation : String,

    @ColumnInfo(name = "endDate")
    @SerializedName("endDate")
    val endDate : String
)