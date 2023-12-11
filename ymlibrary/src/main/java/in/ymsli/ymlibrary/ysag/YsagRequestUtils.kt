/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 27, 2018
 * Copyright (c) 2017, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YsagRequestUtils : responsible for providing parameters for ysag related module
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
package `in`.ymsli.ymlibrary.ysag

import `in`.ymsli.ymlibrary.config.YMConstants
import java.util.HashMap

object YsagRequestUtils {


    /**
     * Method to provide default headers
     * @param  apiName api name
     * @param  projectkey project key
     * @param  xkey xkey
     * @param  ykey ykey
     */
    fun getYSAGHeaders(apiName: String,requestComponent:RequestComponent): HashMap<String, String> {
        var map: Map<String, String>
        map = HashMap()
        map.put(YMConstants.PARAM_DEVICE_APP, YMConstants.PARAM_DEVICE_VALUE)
        map.put(YMConstants.PARAM_KEY_PROJECT_KEY, requestComponent.projectkey)
        map.put(YMConstants.PARAM_KEY_X_API_KEY, requestComponent.xkey)
        map.put(YMConstants.PARAM_KEY_API_NAME, apiName)
        map.put(YMConstants.PARAM_KEY_Y_KEY, requestComponent.ykey)

        return map
    }

    /**
     * Method to provide all params for login
     * @param username username
     * @param password password
     * @param requestComponent to set reqired fields of ysag
     * @return hashmap for login params
     */
    fun getLoginParams(username: String, password: String,requestComponent:RequestComponent): java.util.HashMap<String, String> {

        val bodyParams = java.util.HashMap<String, String>()
        bodyParams[YMConstants.PARAM_USER_ID] = username
        bodyParams[YMConstants.PARAM_KEY_PASSWORD] = password
        bodyParams[YMConstants.PARAM_KEY_PROJECT_KEY] = requestComponent.projectkey
        bodyParams[YMConstants.PARAM_DEVICE_APP] = YMConstants.PARAM_DEVICE_VALUE
        bodyParams[YMConstants.PARAM_DEVICE_ID] = requestComponent.deviceId
        bodyParams[YMConstants.PARAM_APP_VERSION] = requestComponent.appVersion
        bodyParams[YMConstants.VERSION_ID] = requestComponent.versionId
        bodyParams[YMConstants.FCM_TOKEN] = requestComponent.fcmToken

        return bodyParams
    }
}