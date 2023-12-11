package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

class LocationSharingResponse (
    @SerializedName("responseCode")
    val responseCode: Int,

    @SerializedName("responseMessage")
    val responseMessage: String,

    @SerializedName("responseData")
    val responseData: String

)