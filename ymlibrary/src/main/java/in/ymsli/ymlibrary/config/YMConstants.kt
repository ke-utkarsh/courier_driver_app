/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 *
 * YMConstants : This class contins all constants value for app
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.config

import java.util.concurrent.TimeUnit

class YMConstants {

    companion object {

        val WIFI_CONNECTED = "wifi"
        val MOBILE_CONNECTED = "mobile_data"

        val RESPONSE_OK = 200//responseok
        val RESPONSE_CODE_401 = 401// username or password invaid
        val RESPONSE_CODE_500 = 500//internl server error
        val RESPONSE_CODE_400 = 400//Bad request
        val RESPONSE_FAIL_300 = 300//response fail


        val EMPTY_STRING: String? = ""
        val IS_SHOW_LOG = true// if want to show logs of library

        val MIMEPDF: String = "application/pdf"// application type


        //Y-sag default headers
        val PARAM_KEY_Y_KEY_VALUE = "4AN3tEF1eJiCxBYI1+PfUaC12krkX4N2kc6TqbpBNKY="  //default Y-KEY for YSAG
        // value project key
        val PARAM_KEY_PROJECT_KEY = "projectkey"     //key project key
        val PARAM_KEY_X_API_KEY = "x-api-key"       // key X API Key for ysag
        val PARAM_KEY_API_NAME = "apiname"          // key api name for ysag
        val PARAM_KEY_Y_KEY = "y-key"
        val PARAM_VALUE_X_API_KEY = "H9Dld5dosw8eoSruRsdsR9GbuSY6btqy8i1EYu27"// value X-key ysag
        val PARAM_DEVICE_VALUE = "android"
        val PARAM_DEVICE_APP = "devicetype"
        val PARAM_DEVICE_ID = "deviceid"
        val PARAM_APP_VERSION = "appversion"
        val VERSION_ID = "version_id"
        val PARAM_USER_ID = "userid"
        val PARAM_KEY_PASSWORD = "password"
        val FCM_TOKEN = "token"
        val AUTH_TOKEN = "authorization"
        val CONTENT_TYPE_URL_ENCODED = "application/x-www-form-urlencoded; charset=UTF-8"
        val CONTENT_TYPE_JSON = "application/json"

        val API_URL = "https://groutveujk.execute-api.ap-southeast-1.amazonaws.com/"
        val END_POINT_LOGIN_URL = "/prod/login/"
        val END_POINT_APP_INFO_URL = "/prod/appinfo/"
        val TIMOUT_UNIT: TimeUnit = TimeUnit.MILLISECONDS  //default Y-KEY for YSAG
        val TIMEOUT_READ: Long = 5  //default Y-KEY for YSAG
        val TIMEOUT_WRITE: Long = 5  //default Y-KEY for YSAG
        val TIMEOUT_CONNECT: Long = 1  //default Y-KEY for YSAG

        val DEFAULT_DATE_FORMAT: String = "dd MMM yyyy"

        //Login Screen constants
        val PATTERN_USER_NAME = "[\\sA-Za-z0-9-.]+"

        val LOGGING_ENABLE="logging_enable"


    }

}