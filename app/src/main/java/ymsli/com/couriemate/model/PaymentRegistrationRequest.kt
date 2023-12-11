package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   jan 27, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * PaymentRegistrationRequest : This data class is used as request model for the
 *                              payment receipt upload API.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class PaymentRegistrationRequest(
    @SerializedName("driverId")
    val driverId : Int,

    @SerializedName("driverName")
    val driverName : String,

    @SerializedName("amount")
    val amount : Double,

    @SerializedName("bank")
    val bank : String,

    @SerializedName("note")
    val note : String? = null,

    @SerializedName("imageData")
    val imageData : String, //send base64 encoded image

    @SerializedName("timezoneOffset")
    val timezoneOffset : String
)