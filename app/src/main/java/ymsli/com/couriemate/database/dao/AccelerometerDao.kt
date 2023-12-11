package ymsli.com.couriemate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.database.entity.AccelerometerEntity

@Dao
interface AccelerometerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewAccel(accelerometerEntity: AccelerometerEntity)

    @Query("Select * from accel_entity where isFileCreated = 0 limit 60")
    fun getUnsyncedAccelEntity(): Array<AccelerometerEntity>

    @Query("Delete from accel_entity where id =:id")
    fun removeSyncedAccelData(id: Long)

    @Query("Delete from accel_entity")
    fun clearAccelTable()
}