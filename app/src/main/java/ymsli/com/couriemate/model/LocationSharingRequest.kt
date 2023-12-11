package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

class LocationSharingRequest (
    @SerializedName("companyCode")
    val companyCode: String,

    @SerializedName("taskId")
    val taskId: Long,

    @SerializedName("templateId")
    val templateId: Int,

    @SerializedName("userId")
    val userId: String

)