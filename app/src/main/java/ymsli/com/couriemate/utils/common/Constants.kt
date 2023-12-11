/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   July 2, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * Constants : This class contains all constants value for couriemate application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.couriemate.utils.common

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * Constants : String constants used throughtout the application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class Constants {
    object NOTIFICATION_TYPE {
        const val SILENT = "Silent"
        const val ALERT = "ALERT"
    }

    companion object {
        const val LOCATION_REQUEST_INTERVAL = 600000L
        const val LOCATION_REQUEST_FASTEST_INTERVAL = 600000L
        const val SHARED_PREF_KEY_NOTIFICATION_COUNT = "notificationCount"
        const val SHARED_PREF_KEY_USER_DISPLAY_NAME = "userDisplayName"
        const val CCUID_KEY = "CCUID_KEY"
        const val APK_VERSION_PREFIX = "APK Version: "
        const val API_VERSION_PREFIX = "API Version: "

        const val COUNTRY_CODE_UGANDA = "+256-"
        const val SOURCE_MOBILE = "mobile"
        const val WIFI_CONNECTED = "wifi"
        const val MOBILE_CONNECTED = "mobile_data"

        const val EMPTY_STRING = ""
        const val RETURN_TASK_PATTERN = "R%"
        const val FORWARD_TASK_PATTERN = "T%"
        const val SPLASH_DELAY = 2000L

        const val COURIEMATE_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        const val NOTIFICATION_TIME_FORMAT = "MM/dd/yyyy hh:mm a"
        const val FORMAT_COURIEMATE_DATE = "d MMMM, HH:mm 'Hrs'"
        const val FORMAT_COURIEMATE_DATE_HEADER = "E, MMM d yyyy"
        const val FORMAT_COURIEMATE_DATE_FILTER = "YYYY/MM/dd"
        const val FORMAT_COURIEMATE_CURRENCY = "#,###.00"
        const val DATE_PLACE_HOLDER = "____/__/__"

        const val SHARED_PREF_KEY_IS_LOGGED = "isLoggedIn"
        const val SHARED_PREF_KEY_USER_TYPE = "userType"
        const val SHARED_PREF_KEY_API_AUTH_TOKEN = "apiAuthToken"
        const val SHARED_PREF_KEY_USER_NAME = "userName"
        const val SHARED_PREF_KEY_PASSWORD = "password"
        const val SHARED_PREF_KEY_DEVICE_ID = "deviceId"
        const val SHARED_PREF_KEY_DRIVER_ID = "driverId"
        const val SHARED_PREF_KEY_USER_PHONE = "userPhone"
        const val SHARED_PREF_KEY_COMPANY_CONTACT = "companyContact"
        const val SHARED_PREF_KEY_COMPANY_ADDRESS = "companyAddress"
        const val SHARED_PREF_KEY_CUSTOMER_NOT_REACHABLE = "customerUnreachable"
        const val SHARED_PREF_KEY_INIT_STATUS = "init"
        const val SHARED_PREF_KEY_API_VERSION = "apiVersion"
        const val SHARED_PREF_KEY_API_PROFILE = "apiProfile"
        const val UPDATED_ON_SHARED_PREF_KEY = "updatedOn"
        const val SHARED_PREF_KEY_FCM_TOKEN = "fcmToken"
        const val SHARED_PREF_KEY_SYNC_WORKER_ITERATION = "syncWorkerIteration"
        const val COLOR_DEFAULT = "#AAAAAA"
        const val COLOR_ASSIGNED = "#2079E5"
        const val COLOR_FAILED = "#ED1D0E"
        const val COLOR_DELIVERED = "#46A853"
        const val COLOR_DELIVERING = "#FF9400"
        const val DAY_1_SUFFIX = "st"
        const val DAY_2_SUFFIX = "nd"
        const val DAY_3_SUFFIX = "rd"
        const val DAY_DEFAULT_SUFFIX = "th"
        const val WEB="Web"
        const val ANDROID="Android"
        const val FAIL_MSG="Failed"
        const val REFUSED_MSG="Refused By "
        const val HYPHEN="-"
        const val PREVIOUS_DAY="Previous Day"
        const val PASSWORD_LENGTH_MINIMUM = 4
        const val SHARED_PREF_KEY_REFUSE_REASONS = "refuseReasons"
        const val SHARED_PREF_KEY_MOBILE_MONEY_TYPES = "mobileMoneyTypes"
        const val REARRANGE_MODE_TITLE = "Rearrange Current Task's"

        const val WORKMANAGER_THREADS = 1

        const val DIRECTORY = "Couriemate_DATA"
        const val REQUEST_MEDIA_TYPE_VALUE = "application/octet-stream"
        const val UNIQUE_DEVICE_ID_KEY = "UNIQUE_DEVICE_ID_KEY"
        const val NA_KEY = "NA"
        const val IS_TRIP_RUNNING_STATUS = "TRIP_RUNNING_STATUS"
        const val DEFAULT_TRIP_ID = "user_2021:01:22 12:00"

        //region trip config key
        const val MIN_TRIP_DURATION = "MIN_TRIP_DURATION"
        const val MIN_TRIP_DISTANCE = "MIN_TRIP_DISTANCE"
        const val LOCATION_HISTORY_SERVER = "LOCATION_HISTORY_SERVER"
        const val MIN_TRIP_DURATION_VALUE = 30
        const val MIN_TRIP_DISTANCE_VALUE = 50
        //endregion trip config key

        //region location configuration
        const val SEND_LOCATION_KEY = "SEND_LOCATION_KEY"
        const val LOCATION_INTERVAL_KEY = "LOCATION_INTERVAL_KEY"
        //last api calling time
        const val LAST_API_CALLING_TIME_KEY = "LAST_API_CALLING_TIME_KEY"
        const val LOCATION_SPEED_KEY = "LOCATION_SPEED_KEY"
        const val SLEEP_MODE_TIME_KEY = "SLEEP_MODE_TIME_KEY"
        const val LAST_LOCATION_TIMESTAMP_KEY = "LAST_LOCATION_TIMESTAMP_KEY"
        const val LOCATION_SERVER_URL_KEY = "LOCATION_SERVER_URL_KEY"
        const val LOCATION_CONFIG_SERVER_END_POINT = "/locationSharing/get-rider-location-share-setting" //DEV
        //const val LOCATION_CONFIG_SERVER_END_POINT = "/locationsharing/get-rider-location-share-setting" //UAT
//        const val LOCATION_CONFIG_SERVER_END_POINT = "/locationSharing/get-rider-location-share-setting" //LIVE
        const val LOCATION_STORE_SERVER_END_POINT = "/save-location-history"
        //endregion location configuration

        private const val MILLIS_DAY = (24 * 60 * 60 * 1000)
        const val TWO_MONTH_MILLIS = 60L * MILLIS_DAY

        const val EXPENSES_UPDATE = "!DATAMODIFY"
        const val EXPENSES_INSERT = "!NEWMODIFY"
    }
}
