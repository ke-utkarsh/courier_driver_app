package ymsli.com.couriemate.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ApiMasterEntity : This is the schema of api_master in room library.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Entity(tableName = "api_master")
data class ApiMasterEntity(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "api_id")
    val apiId: Long,

    @NotNull
    @ColumnInfo(name = "api_description")
    val appDescription:String,

    @NotNull
    @ColumnInfo(name = "api_url")
    val appUrl :String,

    @ColumnInfo(name = "last_sync_on")
    val lastSyncOn : String?
)