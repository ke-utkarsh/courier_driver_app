package ymsli.com.couriemate.database.dao

import ymsli.com.couriemate.database.entity.TaskSummaryResponse
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
 * TaskSummaryDao : This interface is responsible for CRUD operations of tasks_summary
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
interface TaskSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverTaskSummary(vararg summary : TaskSummaryResponse)

    @Query("Select * FROM tasks_summary where `key` = :periodId")
    fun getDriverTaskSummaryByPeriod(periodId: Int): TaskSummaryResponse

    @Query("Delete FROM tasks_summary")
    fun clearDriverTaskSummary()

}