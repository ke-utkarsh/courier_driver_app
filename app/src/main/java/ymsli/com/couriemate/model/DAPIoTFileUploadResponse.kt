package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

data class DAPIoTFileUploadResponse (
    @SerializedName("SequenceNumber")
    val SequenceNumber: String?,

    @SerializedName("ShardId")
    val ShardId: String?
)