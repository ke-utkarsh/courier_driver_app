/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YMBaseErrorResponse : class provides generic Error response object for API call having default constructor
 * for response message and error code
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */


package `in`.ymsli.ymlibrary.client


class YMBaseErrorResponse(ymMessage: String, ymErorrCode: Int) {

    var ymMessage: String? = null  //Error message
    var ymErorrCode: Int? = null //Error code


    init {

        this.ymMessage = ymMessage
        this.ymErorrCode = ymErorrCode

    }

}