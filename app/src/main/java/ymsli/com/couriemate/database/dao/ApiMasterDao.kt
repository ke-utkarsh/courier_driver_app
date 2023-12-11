package ymsli.com.couriemate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.database.entity.ApiMasterEntity

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ApiMasterDao : This interface is responsible for CRUD operations of api_master
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
interface ApiMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApiData(vararg apiMasterEntity: ApiMasterEntity)

    @Query("UPDATE api_master SET last_sync_on = :syncTime WHERE api_id = :apiId")
    fun updateLastSync(apiId: Long, syncTime: String)
}