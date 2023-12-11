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
 * ChangePasswordValidationModel : This model is used by the validator to perform the
 *                                 validations before the change password API request.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
data class ChangePasswordValidationModel(
    @SerializedName("currentPassword")
    var currentPassword: String,
    @SerializedName("enteredCurrentPassword")
    var enteredCurrentPassword: String?,
    @SerializedName("newPassword")
    var newPassword: String?,
    @SerializedName("retypeNewPassword")
    var retypeNewPassword: String?)