package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   Nov 14, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * UserTokenMapping : Request model for the update FCM token API.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class UserTokenMapping (
    @SerializedName("createdOn")
    val createdOn: String? = null,
    @SerializedName("deviceToken")
    val deviceToken: String? = null,
    @SerializedName("updatedOn")
    val updatedOn: String? = null,
    @SerializedName("userName")
    val userName: String? = null,
    @SerializedName("userTokenId")
    val userTokenId: Long? = null
)