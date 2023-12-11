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
 * DropDownEntity : This data class is used by the drop down adapter on the task detail.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class DropDownEntity(
    @SerializedName("code")
    var code: Int,
    @SerializedName("value")
    var value: String
){
    override fun toString() = value
}