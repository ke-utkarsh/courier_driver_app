package ymsli.com.couriemate.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.database.entity.ReceiptConfigEntity

@Dao
interface ReceiptConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewReceiptConfig(vararg trip: ReceiptConfigEntity)

    @Query("Select * from receipt_config_items")
    fun getReceiptConfigItems(): LiveData<Array<ReceiptConfigEntity>>
}