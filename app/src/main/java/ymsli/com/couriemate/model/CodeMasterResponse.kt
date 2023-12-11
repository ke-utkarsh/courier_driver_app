package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   Jan 13, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * CodeMasterResponse : Used as the response for the drop down data retrieve API's.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
class CodeMasterResponse(

    @SerializedName("dataId")
    val dataId: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("data")
    val data: Map<String, String>
)
