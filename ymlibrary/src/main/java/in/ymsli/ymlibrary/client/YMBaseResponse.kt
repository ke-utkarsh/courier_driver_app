/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YMBaseResponse : Class provides generic response object for API call having default constructor
 * data as object
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */


package `in`.ymsli.ymlibrary.client


class YMBaseResponse(data: Any, ymBaseErrorResponse: YMBaseErrorResponse?) {

    var data: Any? = null
    var ymBaseErrorResponse: YMBaseErrorResponse? = null

    init {

        this.data = data
        this.ymBaseErrorResponse = ymBaseErrorResponse
    }


}