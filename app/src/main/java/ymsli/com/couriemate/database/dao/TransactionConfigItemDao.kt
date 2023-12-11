package ymsli.com.couriemate.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.database.entity.TransactionConfigItemEntity

@Dao
interface TransactionConfigItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewItemConfig(vararg trip: TransactionConfigItemEntity)

    @Query("Select * from transaction_config_items")
    fun getTransactionConfigItems(): LiveData<Array<TransactionConfigItemEntity>>

    @Query("Select dataValue from transaction_config_items where codeValue =:codeValue")
    fun getTransactionConfigValue(codeValue:Int): String?

    @Query("Select * from transaction_config_items Where codeValue =:codeValue ")
    fun getTransactionConfigItem(codeValue: Int): LiveData<TransactionConfigItemEntity>
}