/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 27, 2018
 * Copyright (c) 2017, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * RequestComponent : responsible for providing values for ysag request
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
package `in`.ymsli.ymlibrary.ysag

import `in`.ymsli.ymlibrary.config.YMConstants

object RequestComponent {

    var xkey: String = YMConstants.EMPTY_STRING!! // xkey
    var ykey: String = YMConstants.EMPTY_STRING!! // ykey
    var projectkey: String = YMConstants.EMPTY_STRING!! // project key
    var appVersion: String = YMConstants.EMPTY_STRING!! // app version
    var versionId: String = YMConstants.EMPTY_STRING!! // version id
    var deviceId: String = YMConstants.EMPTY_STRING!!  // device id
    var fcmToken: String = YMConstants.EMPTY_STRING!!  // fcm token


}
