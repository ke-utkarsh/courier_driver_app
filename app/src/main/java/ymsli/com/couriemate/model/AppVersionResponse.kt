package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   Oct 15, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * AppVersionResponse : This data class is used as the response app status API
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class AppVersionResponse(
    @SerializedName("apiVersion") val apiVersion: String? = null,
    @SerializedName("appVersion") val appVersion: String? = null,
    @SerializedName("forceUpdate") val forceUpdate: Boolean? = null,
    @SerializedName("isLatestVersion") val isLatestVersion: Boolean? = null,
    @SerializedName("isStable") val isStable: Boolean? = null,
    @SerializedName("springProfile") val springProfile: String? = null
)