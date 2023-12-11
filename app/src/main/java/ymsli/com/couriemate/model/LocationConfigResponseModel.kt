package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

data class LocationConfigResponseModel (
    @SerializedName("message") val message: String? = null,
    @SerializedName("responseCode") val responseCode: Int? = null,
    @SerializedName("responseData") val responseData: LocationConfigResponseData? = null
)

data class LocationConfigResponseData(
    @SerializedName("send_location") val send_location: Int? = null,
    @SerializedName("interval") val interval: Int? = null,
    @SerializedName("speed") val speed: Int? = null,
    @SerializedName("sleepModeTime") val sleepModeTime: Int = 30
)
 