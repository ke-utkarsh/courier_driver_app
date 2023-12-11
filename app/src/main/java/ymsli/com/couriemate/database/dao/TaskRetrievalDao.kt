package ymsli.com.couriemate.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskRetrievalDao : This interface is responsible for CRUD operations of driver_tasks
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
interface TaskRetrievalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverTasksInRoom(vararg tasks : TaskRetrievalResponse)

    @Query("Select * FROM driver_tasks where completed = 1 and taskNo like :taskNoPattern order by taskStatusId,orderNo")
    fun getDriverTasks(taskNoPattern: String): LiveData<Array<TaskRetrievalResponse>>

    @Query("Delete FROM driver_tasks")
    fun deleteDriverTasks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun changeTaskStatus(task : TaskRetrievalResponse)

    @Query("UPDATE driver_tasks set isSynced = :isSynced where taskId = :taskId")
    fun updateSyncStatus(taskId: Long, isSynced: Boolean)

    @Query("DELETE FROM driver_tasks where taskId = :taskId and orderId = :orderId")
    fun deleteAssignedTask(taskId: Int, orderId: Int)

    @Query("UPDATE driver_tasks set completed = 1 where orderId = :orderId")
    fun setTaskPickable(orderId: Int)

    @Query("UPDATE driver_tasks set orderStatusId = :orderStatusId where orderId = :orderId")
    fun updateOrderStatusId(orderStatusId: Int, orderId: Int)

    @Query("UPDATE driver_tasks set noOfRetries = :noOfRetries where orderId = :orderId")
    fun updateNoOfRetries(noOfRetries: Int, orderId: Int)

    @Query("UPDATE driver_tasks set quantity = :itemQuantity where orderId = :orderId")
    fun updateItemQuantity(itemQuantity: Int, orderId: Int)

    @Query("UPDATE driver_tasks set dropAddress = :dropAddress where orderId = :orderId AND taskSequenceNo = (SELECT maxTaskSequence FROM driver_tasks where orderId = :orderId) and taskNo like 'T%' ")
    fun updateLastTaskDeliveryAddress(dropAddress: String, orderId: Int)


    @Query("SELECT * FROM driver_tasks where isSynced = :isSynced")
    fun getNotSyncedRecords(isSynced: Boolean = false): Array<TaskRetrievalResponse>

    /************************ Changes for tabbed task list *****************************/
    @Query("Select * FROM driver_tasks where completed = 1 and taskStatusId in (:taskStatusIds) ORDER BY startDate ASC")
    fun getTasksByStatusAndPickupDate(taskStatusIds: List<Int>): LiveData<Array<TaskRetrievalResponse>>

    @Query("Select * FROM driver_tasks where completed = 1 and taskStatusId in (:taskStatusIds) ORDER BY endDate DESC")
    fun getDoneTasksByStatusAndPickupDate(taskStatusIds: List<Int>): LiveData<Array<TaskRetrievalResponse>>

    @Query("Select * FROM driver_tasks where taskStatusId = :taskStatusId and orderStatusId = :orderStatusId")
    fun getReturnTasksByStatus(taskStatusId: Int,orderStatusId: Int): LiveData<Array<TaskRetrievalResponse>>

    @Query("UPDATE driver_tasks SET orderMemo = :orderMemo where orderId = :orderId")
    fun updateOrderMemoByOrderId(orderId: Long, orderMemo: String)

}