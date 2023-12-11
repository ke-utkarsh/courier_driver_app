package ymsli.com.couriemate.database.dao

import ymsli.com.couriemate.database.entity.NotificationResponse
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * NotificationDao : This interface is responsible for CRUD operations of notification_master
 *                  SQLite table. The DAO is part of Room persistence library
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationsInRoom(vararg notifications : NotificationResponse)

    @Query("Select * FROM notification_master ORDER BY notificationId desc")
    fun getNotifications(): Array<NotificationResponse>

    @Query("DELETE FROM notification_master")
    fun clearNotifications()

    @Query("DELETE FROM notification_master WHERE notificationId NOT IN (SELECT notificationId FROM notification_master ORDER BY notificationId DESC LIMIT 30)")
    fun truncate()
}