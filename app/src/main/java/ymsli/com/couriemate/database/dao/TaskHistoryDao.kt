package ymsli.com.couriemate.database.dao

import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import androidx.lifecycle.LiveData
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
interface TaskHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverHistoryTasksInRoom(vararg tasks : TaskHistoryResponse)

    @Query("Select * FROM tasks_history")
    fun getDriverHistoryTasks(): LiveData<Array<TaskHistoryResponse>>

    @Query("Delete FROM tasks_history")
    fun removeDriverHistoryTasks()
}