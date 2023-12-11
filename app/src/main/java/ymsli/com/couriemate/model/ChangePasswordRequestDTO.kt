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
 * ChangePasswordRequestDTO : Used for the change password API request
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class ChangePasswordRequestDTO (
    @SerializedName("currentPassword")
    val currentPassword: String? = null,
    @SerializedName("newPassword")
    val newPassword: String? = null,
    @SerializedName("passwordResetFlag")
    val passwordResetFlag: Boolean? = null,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("timezoneOffset")
    val timezoneOffset: String? = null,
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("userName")
    val userName: String? = null
) 
