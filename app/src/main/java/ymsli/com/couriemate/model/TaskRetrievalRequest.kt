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
 * TaskRetrievalRequest : Response model for the getDriver tasks API
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class TaskRetrievalRequest(
    @SerializedName("driverId") var driverId: Int? = null,
    @SerializedName("expectedStart") val expectedStart: String? = null,
    @SerializedName("fromDate") val fromDate: java.sql.Timestamp? = null,
    @SerializedName("returned") val returned: Boolean? = null,
    @SerializedName("source") val source: String? = null,
    @SerializedName("taskStatusId") val taskStatusId: Int? = null,
    @SerializedName("timezoneOffset") val timezoneOffset: String? = null,
    @SerializedName("toDate") val toDate: java.sql.Timestamp? = null,
    @SerializedName("updatedOn") val updatedOn: String? = null,
    @SerializedName("userName") val userName: String? = null


)