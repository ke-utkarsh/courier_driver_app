package pymidol.com.ymsli.ymlibrary.testing

import com.google.gson.annotations.SerializedName

/**
 * Created by Dinesh on 08/07/17.
 */

class YMObjectResponse {

    @SerializedName("responseCode")
    var responseCode: Int? = null
    @SerializedName("responseMessage")
    var responseMessage: String? = null
    @SerializedName("responseType")
    var responseType: String? = null

    @SerializedName("data")
    var data: Map<String, Object>? = null

    override fun toString(): String {
        return "YMObjectResponse{" +
                "responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\''.toString() +
                ", responseType='" + responseType + '\''.toString() +
                ", data=" + data +
                '}'.toString()
    }
}
