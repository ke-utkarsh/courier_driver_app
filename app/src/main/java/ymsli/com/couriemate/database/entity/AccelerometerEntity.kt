package ymsli.com.couriemate.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accel_entity")
class AccelerometerEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long?=null,

    @ColumnInfo(name = "tripId")
    val tripId: String,

    @ColumnInfo(name = "xCoordinate")
    val x: String,

    @ColumnInfo(name = "yCoordinate")
    val y: String,

    @ColumnInfo(name = "zCoordinate")
    val z: String,

    @ColumnInfo(name = "sensorTime")
    val sensorTime: String,

    @ColumnInfo(name = "isFileCreated")
    val isFileCreated: Boolean
)