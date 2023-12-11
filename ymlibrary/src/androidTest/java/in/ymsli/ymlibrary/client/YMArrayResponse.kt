package pymidol.com.ymsli.ymlibrary.testing

import java.util.*

/**
 * Created by dinesh on 07/07/17.
 */

class YMArrayResponse {
    var responseCode: Int? = null
    var responseMessage: String? = null
    var responseType: String? = null
    var data: List<HashMap<String, String>>? = null

    override fun toString(): String {
        return "YMArrayResponse{" +
                "responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\''.toString() +
                ", responseType='" + responseType + '\''.toString() +
                ", data=" + data +
                '}'.toString()
    }
}
