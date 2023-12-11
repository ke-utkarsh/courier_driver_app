package ymsli.com.couriemate.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.views.chat.ChatMessage

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(message: ChatMessage): Long

    @Query("SELECT * FROM chatmessage ORDER BY timestamp")
    fun getChatLive(): LiveData<List<ChatMessage>>

    @Query("UPDATE chatmessage SET status = :status WHERE id = :id")
    fun updateMessageStatus(id: Long, status: Int)

    @Query("UPDATE chatmessage SET status=${ChatMessage.SEEN} WHERE status = ${ChatMessage.DELIVERED}")
    fun updateSeenStatus()

    @Query("DELETE FROM chatmessage WHERE id in (:messageIds)")
    fun deleteMessages(messageIds: List<Long>)

    @Query("DELETE FROM chatmessage")
    fun truncateChat()
}