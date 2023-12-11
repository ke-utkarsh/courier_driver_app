/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 11, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YMLogMessages : This class contains all error or log messages
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */


package `in`.ymsli.ymlibrary.errors


enum class YMLogMessages(logMessage: String)


/**
 * @param errorMessage
 * Error description
 */
{

    ERROR_MAIN_THREAD("Not allowed to establish network connection on UI main thread. Try to run it with Async Task"),
    ERROR_CONNECTION_REFUSED("Connection refused due to timeout for fetching data from server."),
    ERROR_VALID_INPUT("Unable to process your request. Please verify the inputs provided."),
    ERROR_SERVER_NOT_EXIST("Server does not exist or Server URL is not valid."),
    ERROR_SOCKET_TIMEOUT("SocketTimeoutException while executing service call:"),
    ERROR_HOSTNAME("host name may not be null"),
    ERROR_IO_EXCEPTION("IOException while executing service call"),
    ERROR_JSON_SYNTAX_EXCEPTION("Error in json syntax."),
    ERROR_BASE_URL("BASE_URL is not valid.It must ends with '/'");
    val logMessage = logMessage

}
