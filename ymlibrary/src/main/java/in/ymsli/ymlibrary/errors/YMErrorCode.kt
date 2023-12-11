/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 11, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YMErrorCode : This class contains common error pre-defined error codes with error messages
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */


package `in`.ymsli.ymlibrary.errors


enum class YMErrorCode(errorCode: Int, errorMessage: String)
/**
 * @param errorCode
 * Integer value of error code
 * @param errorMessage
 * Error description
 */
{

    IMAGE_CONTENT_INVALID(101, "Bitmap of image has invalid content to process."),
    PATH_NOT_EXISTS(102, "Given path not exist"),
    PATH_FORMAT_WRONG(103, "Given path format is wrong."),
    MEMORY_EXCEEDS_LIMIT(104, "Memory limit exceeds."),
    ARRAY_NULL_OR_EMPTY(105, "Array is null or empty."),
    FILE_EXTENSION_INVALID(106, "File Extension is invalid."),
    INPUT_INVALID(107, "Given input invalid."),
    USERNAME_PASSWORD_EMPTY(108, "Username or password is empty."),
    USERNAME_PASSWORD_INVALID(109, "Username or password invalid."),
    NO_INTERNET_CONNECTION(110, "Internet connection not available."),
    TIMEOUT_EXCEPTION(111, "Socket timeout."),
    SERVER_ERROR(112, "There is some server error occured."),
    FILE_NOT_SAVED(113, "File is unable to save."),
    NETWORK_AT_MAIN_THREAD(114, "Network operation in main thread not able to execute."),
    CONTEXT_VALUE_NULL(115, "Context value is null or empty."),
    FILE_NAME_INVALID(116, "File name not valid."),
    IO_EXCEPTION(117, "I/O Exception."),
    NOT_FOUND_EXCEPTION(118, "File not found."),
    JSON_EXCEPTION_REQUEST_BODY(119, "Unable to parse request as JSON body."),
    REQUEST_FAILURE(120, "Request failed."),
    BASE_URL_NOT_CORRECT(121, "BASE_URL is not valid.It must ends with '/'"),
    EXCEPTION_REQUEST_BODY(123, "Unable to parse request as  body."),
    PARSING_DAT_TIME_FORMAT(124, "Parsing error in date-time format '/'"),
    PARENT_DIRETORY_INVALID(125, "Parent directory can not be created '/'"),
    FILE_NOT_FOUND(126, "File not found '/'"),
    JSON_EXCEPTION_RESPONSE_BODY(127, "Unable to parse response as JSON body."),
    PHONE_NUMBER_OR_COUNTRY_CODE_NOT_VALID(128, "Unable to parse phone number"),
    ERROR_SQLITE_DB_READ(129, "Could not open database to read"),
    ERROR_SQLITE_DB_WRITE(130, "Could not open database to write"),
    ERROR_BAD_REQUEST(400,"Bad Request"),
    ErrorUnauthorizedAccess(401,"Unauthorized access"),
    ErrorForbidden(403,"Bad Request"),
    ERROR_PAGE_NOT_FOUND(404,"Page not found"),
    ERROR_TOO_MANY_REQUST(429,"ErrorTooManyRequests"),
    ERROR_INTERNAL_SERVER_EROR(500,"Internal server error"),
    ERROR_BAD_GATEWAY(502,"Bad Gateway"),
    ErrorServiceUnavailable(503,"Server Unavailable"),
    ErrorGatewayTimeout(504,"Gat way timeout");



    val errorCode = errorCode
    val erroMessages = errorMessage
}
