package ymsli.com.couriemate.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.database.entity.ChatNotification

@Dao
interface ChatNotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(message: ChatNotification): Long

    @Query("SELECT * FROM chatnotification")
    fun getNotification(): LiveData<List<ChatNotification>>

    @Query("DELETE FROM chatnotification")
    fun delete(): Int
}