package ymsli.com.couriemate.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.couriemate.database.entity.XMPPConfig

@Dao
interface XMPPConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertXMPPConfig(xmppConfig: XMPPConfig)

    @Query("SELECT * FROM xmppconfig LIMIT 1")
    fun getXMPPConfig(): XMPPConfig

    @Query("DELETE FROM xmppconfig")
    fun truncate()

    @Query("UPDATE xmppconfig set historyRestored = 1")
    fun setHistoryRestored()
}