/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 11, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YMException : Class to handle Exceptions generated while
 * performing methods target.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.errors

import `in`.ymsli.ymlibrary.config.YMConstants

class YMExceptions(ymErrorCode: YMErrorCode, message: String?) : Exception() {

    /** Unique error code, assigned to different type of errors  */
    var errorCode: Int = 0

    /* Error message, which makes the error clear and understandable to user  */
    var errorMessage: String? = null


    init {
        this.errorCode = ymErrorCode.errorCode
        if (message == null || message.equals(YMConstants.EMPTY_STRING)) {
            this.errorMessage = ymErrorCode.erroMessages
        } else {
            this.errorMessage = message
        }
    }

}
