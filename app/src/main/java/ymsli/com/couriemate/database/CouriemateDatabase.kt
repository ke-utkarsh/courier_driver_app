package ymsli.com.couriemate.database

import ymsli.com.couriemate.database.dao.*
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ymsli.com.couriemate.database.entity.*
import ymsli.com.couriemate.model.LocationTrackingRequest
import ymsli.com.couriemate.views.chat.ChatMessage
import javax.inject.Singleton

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * CouriemateDatabase : This is the database of room library and contains info such as
 *                          version number, exportSchema, etc
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Singleton
@Database(entities = [TaskRetrievalResponse::class, ApiMasterEntity::class,
    TaskHistoryResponse::class, TaskSummaryResponse::class, NotificationResponse::class,
    AccelerometerEntity::class,GyroEntity::class, LatLongEntity::class, TripEntity::class, DAPIoTFileEntity::class,
    TransactionConfigItemEntity::class,ReceiptConfigEntity::class,ProcessedNotification::class, ChatMessage::class,
    ChatNotification::class, XMPPConfig::class, LocationTrackingRequest::class],
    version = 7,
    exportSchema = false)
@TypeConverters(TypeConverterOrderItem::class)
abstract class CouriemateDatabase : RoomDatabase() {
    abstract fun taskRetrievalDao(): TaskRetrievalDao
    abstract fun apiMasterDao(): ApiMasterDao
    abstract fun taskHistoryDao(): TaskHistoryDao
    abstract fun taskSummaryDao(): TaskSummaryDao
    abstract fun notificationsDao(): NotificationDao
    abstract fun accelerometerDao():AccelerometerDao
    abstract fun gyroDao():GyroDao
    abstract fun latLongDao():LatLongDao
    abstract fun tripDao():TripDao
    abstract fun dapIoTFileDao():DAPIoTFileDao
    abstract fun processedNotificationDao(): ProcessedNotificationsDao
    abstract fun transactionConfigItemDao():TransactionConfigItemDao
    abstract fun receiptConfigDao():ReceiptConfigDao
    abstract fun chatDao(): ChatDao
    abstract fun chatNotificationDao(): ChatNotificationDao
    abstract fun xmppConfigDao(): XMPPConfigDao
    abstract fun locationTrackingDao(): LocationTrackingDao


}