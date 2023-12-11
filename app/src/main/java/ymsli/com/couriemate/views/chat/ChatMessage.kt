package ymsli.com.couriemate.views.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ChatMessage(
    @PrimaryKey
    val id: String,
    val senderJid: String,
    val receiverJid: String,
    val text: String,
    val timestamp: Long,
    val status: Int
){
    companion object {
        const val NOT_DELIVERED = 1
        const val DELIVERED = 2
        const val SEEN = 3
    }
}