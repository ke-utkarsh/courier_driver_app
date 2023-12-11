/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 *
 * YMServiceCallback :
 * This class is used to call web based services in order to contact server
 * 1. onFailure - Collects information of any failure occurred while processing.
 * 2. onSuccess - Collects  result.

 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.client

abstract class YMServiceCallback {


    /** Abstract method to handle all Failure instances of the WebService  */
    abstract fun onFailure(ymBaseErrorResponse:YMBaseErrorResponse)

    /** Abstract method to handle Success instances of the WebService  */
    abstract fun onSuccess(ymBaseResponse: YMBaseResponse)


}

