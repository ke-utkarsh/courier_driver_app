package ymsli.com.couriemate.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ChatNotification (
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,

    @ColumnInfo(name = "message")
    val message: String
)