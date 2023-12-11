package ymsli.com.couriemate.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "transaction_config_items")
data class TransactionConfigItemEntity (
    @PrimaryKey
    @ColumnInfo(name = "codeValue")
    @SerializedName("codeValue")
    val codeValue : Int,

    @SerializedName("dataValue")
    @ColumnInfo(name = "dataValue")
    val dataValue : String?,

    @SerializedName("source")
    @ColumnInfo(name = "source")
    val source : String?
    )