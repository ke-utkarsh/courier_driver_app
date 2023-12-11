package ymsli.com.couriemate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.database.entity.ProcessedNotification

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   July 7, 2021
 * Copyright (c) 2021, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ProcessedNotificationsDao :
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

@Dao
interface ProcessedNotificationsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProcessedNotificationId(vararg notifications : ProcessedNotification)

    @Query("Select notificationId FROM processednotification")
    fun getAllProcessedNotifications(): Array<Long>

    @Query("SELECT 1 FROM processednotification WHERE notificationId = :notificationId")
    fun isProcessed(notificationId: Long): Boolean

    @Query("DELETE FROM processednotification WHERE createdOn < :createdOn")
    fun truncateByCreatedOn(createdOn: Long)
}
