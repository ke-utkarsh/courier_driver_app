package ymsli.com.couriemate.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   July 7, 2021
 * Copyright (c) 2021, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ProcessedNotifications : This entity stores notificationIds of all the notifications
 * that have been processed. See NotificationHandler
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

@Entity
class ProcessedNotification (
    @PrimaryKey
    val notificationId: Long,

    @ColumnInfo(name = "createdOn")
    val createdOn: Long
)