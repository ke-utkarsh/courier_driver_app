package ymsli.com.couriemate.database.dao

import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import androidx.lifecycle.LiveData
import androidx.room.*
import ymsli.com.couriemate.model.LocationTrackingRequest

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskHistoryDao : This interface is responsible for CRUD operations of tasks_history
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
interface LocationTrackingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOfflineLocation(vararg tasks : LocationTrackingRequest)

    @Query("Select * FROM locationtrackingrequest")
    fun getOfflineLocations(): Array<LocationTrackingRequest>

    @Query("Delete FROM locationtrackingrequest where deviceTimestamp=:deviceTimeStamp")
    fun removeOfflineLocations(deviceTimeStamp:String)

}