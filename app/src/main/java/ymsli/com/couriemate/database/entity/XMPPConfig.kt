package ymsli.com.couriemate.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class XMPPConfig(
    @PrimaryKey
    val userName: String,
    val password: String,
    val serverDomain: String,
    val roomName: String,
    val historySize: Int,
    var historyRestored: Boolean
)