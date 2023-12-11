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
 * YMServices :
 * This class is used for sending request synchronously/asynchronously
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */


package `in`.ymsli.ymlibrary.client

import `in`.ymsli.ymlibrary.config.YMConstants
import `in`.ymsli.ymlibrary.config.YMConstants.Companion.EMPTY_STRING
import `in`.ymsli.ymlibrary.errors.YMErrorCode
import `in`.ymsli.ymlibrary.errors.YMExceptions
import `in`.ymsli.ymlibrary.errors.YMLogMessages
import `in`.ymsli.ymlibrary.utils.CommonUtils
import `in`.ymsli.ymlibrary.utils.validator.YMValidator
import android.os.NetworkOnMainThreadException
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

object YMServices {

    /**
     * method for synchronous call
     * @param call Retrofit call object with any type
     */

    fun <T> sendRequestSynchronous(call: Call<ResponseBody>?, clazz: Class<T>): YMBaseResponse? {

        var ymBaseResponse: YMBaseResponse?
        var ymBaseErrorResponse: YMBaseErrorResponse? = null
        var responseObject:String?=null
        try {
            val response = call!!.execute()
            val responseCode = response.code()
            val responseMessage = response.message()

            try {
                // validate response code
                validateResponseCode(responseCode)

            } catch (ymExceptions: YMExceptions) {
                throw ymExceptions
            }

            if (response.isSuccessful) {
                try {

                     responseObject = response.body().string()

                    CommonUtils.showLog(Log.ERROR, "Response - $responseObject")

                    //converting to Object from JSON string
                    val gson = GsonBuilder()
                            .setLenient().serializeNulls().create()
                    ymBaseResponse = YMBaseResponse(responseObject, ymBaseErrorResponse)
                    val resultobject = gson.fromJson(response.body().toString(), clazz)
                    ymBaseResponse.data = resultobject

                } catch (malformedJSONException: Exception) {
                    //to handle malformed json exception

                    val message = if (YMValidator.validateException(malformedJSONException))
                        malformedJSONException.message
                    else
                        YMLogMessages.ERROR_JSON_SYNTAX_EXCEPTION.logMessage

                    CommonUtils.showLog(Log.ERROR, message)
                    ymBaseErrorResponse = YMBaseErrorResponse(responseMessage, responseCode)
                }

            } else {

                ymBaseErrorResponse = YMBaseErrorResponse(responseMessage, responseCode)

            }

        } catch (networkonMainThreadException: NetworkOnMainThreadException) {
            val message = if (YMValidator.validateException(networkonMainThreadException))
                networkonMainThreadException.message
            else
                YMLogMessages.ERROR_MAIN_THREAD.logMessage
            ymBaseErrorResponse = YMBaseErrorResponse(message!!, YMConstants.RESPONSE_FAIL_300)

            CommonUtils.showLog(Log.ERROR, message)
         //   throw YMExceptions(YMErrorCode.NETWORK_AT_MAIN_THREAD, message)
        } catch (socketTimeOutException: SocketTimeoutException) {
            val message = if (YMValidator.validateException(socketTimeOutException))
                socketTimeOutException.message
            else
                YMLogMessages.ERROR_SOCKET_TIMEOUT.logMessage
            ymBaseErrorResponse = YMBaseErrorResponse(message!!, YMConstants.RESPONSE_FAIL_300)

            CommonUtils.showLog(Log.ERROR, message)
          //  throw YMExceptions(YMErrorCode.TIMEOUT_EXCEPTION, message!!)
        } catch (illegalStateException: IllegalStateException) {
            var message = if (YMValidator.validateException(illegalStateException))
                illegalStateException.message
            else
                YMLogMessages.ERROR_VALID_INPUT.logMessage
            ymBaseErrorResponse = YMBaseErrorResponse(message!!, YMConstants.RESPONSE_FAIL_300)

            CommonUtils.showLog(Log.ERROR, message)
         //   throw YMExceptions(YMErrorCode.SERVER_ERROR, message)
        } catch (ioException: IOException) {
            val message = if (YMValidator.validateException(ioException))
                ioException.message
            else
                YMLogMessages.ERROR_IO_EXCEPTION.logMessage
            CommonUtils.showLog(Log.ERROR, message)
            ymBaseErrorResponse = YMBaseErrorResponse(message!!, YMConstants.RESPONSE_FAIL_300)

         //   throw YMExceptions(YMErrorCode.SERVER_ERROR, message)
        } catch (exception: Exception) {
            var message = if (YMValidator.validateException(exception))
                exception.message
            else
                YMLogMessages.ERROR_VALID_INPUT.logMessage
            ymBaseErrorResponse = YMBaseErrorResponse(message!!, YMConstants.RESPONSE_FAIL_300)

            CommonUtils.showLog(Log.ERROR, "Exception: $message")

//            throw YMExceptions(YMErrorCode.SERVER_ERROR, exception.message)
        }
        finally {
            ymBaseResponse = YMBaseResponse(responseObject!!, ymBaseErrorResponse)
        }

        return ymBaseResponse
    }


    /**
     * method for asynchronous call
     * @param call Retrofit call object with any type
     * @param ymServiceCallback service callback object with success and failure response
     *
     */



    fun <T> sendRequestASynchronous(call: Call<ResponseBody>?, ymServiceCallback: YMServiceCallback, clazz: Class<T>) {
        try {
            call?.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {


                    val ymBaseResponse: YMBaseResponse
                    var ymBaseErrorResponse: YMBaseErrorResponse? = null
                    // Request server with request data
                    val responseCode = response!!.code()
                    var responseMessage = response.message()

                    // validate response code
                    validateResponseCode(responseCode)
                    if (response.isSuccessful) {
                        try {
                            val responseObject = response.body().string()
                            CommonUtils.showLog(Log.INFO, "response-->" + responseObject)

                            //converting to Object from JSON string
                            val gson = Gson()

                            ymBaseResponse = YMBaseResponse(responseObject.toString(), ymBaseErrorResponse)
                            val resultobject = gson.fromJson(responseObject.toString(), clazz)
                            ymBaseResponse.data = resultobject
                            CommonUtils.showLog(Log.INFO, "data-->$responseObject")
                            ymServiceCallback.onSuccess(ymBaseResponse)

                        } catch (malformedJSONException: Exception) {
                            //to handle malformed json exception
                            val message = if (YMValidator.validateException(malformedJSONException))
                                malformedJSONException.message
                            else
                                YMLogMessages.ERROR_JSON_SYNTAX_EXCEPTION.logMessage

                            CommonUtils.showLog(Log.ERROR, message)

                            // returning error message in case of json exception
                            ymBaseErrorResponse = YMBaseErrorResponse(message!!, responseCode)
                            ymServiceCallback.onFailure(ymBaseErrorResponse)
                        }

                    } else {

                        var ymbaseErrorResponse = YMBaseErrorResponse(responseMessage, responseCode)
                        ymServiceCallback.onFailure(ymbaseErrorResponse)

                    }

                }

                //Method to provide result on failure
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                    CommonUtils.showLog(Log.ERROR, t!!.message!!)
                    val ymBaseErrorResponse = YMBaseErrorResponse(t.message!!, YMConstants.RESPONSE_FAIL_300)
                    ymServiceCallback.onFailure(ymBaseErrorResponse)
                }


            })


        } catch (networkOnMainThread: NetworkOnMainThreadException) {

            val message = if (YMValidator.validateException(networkOnMainThread))
                networkOnMainThread.message
            else
                YMLogMessages.ERROR_MAIN_THREAD.logMessage
            CommonUtils.showLog(Log.ERROR, message)
            val ymBaseErrorResponse = YMBaseErrorResponse(message!!, YMConstants.RESPONSE_FAIL_300)
            ymServiceCallback.onFailure(ymBaseErrorResponse)

            //throw YMExceptions(YMErrorCode.NETWORK_AT_MAIN_THREAD, networkOnMainThread.message)
        } catch (isException: IllegalStateException) {
            var message = if (YMValidator.validateException(isException))
                isException.message
            else
                YMLogMessages.ERROR_VALID_INPUT.logMessage

            CommonUtils.showLog(Log.ERROR, message)

           // throw YMExceptions(YMErrorCode.SERVER_ERROR, isException.message)
            val ymBaseErrorResponse = YMBaseErrorResponse(message!!,YMConstants.RESPONSE_FAIL_300 )
            ymServiceCallback.onFailure(ymBaseErrorResponse)

        } catch (exception: Exception) {
            var message = if (YMValidator.validateException(exception))
                exception
                        .message
            else
                YMLogMessages.ERROR_VALID_INPUT.logMessage

            CommonUtils.showLog(Log.ERROR, "Exception: $message")
            val ymBaseErrorResponse = YMBaseErrorResponse(message!!,YMConstants.RESPONSE_FAIL_300 )
            ymServiceCallback.onFailure(ymBaseErrorResponse)

            //throw YMExceptions(YMErrorCode.SERVER_ERROR, exception.message)
        }


    }


    /**
     * Validate Response code. If response code is 400,401 OR 500 , it throws exception
     * @param responseCode Response Code to validate
     * @throws YMExceptions
     */
    @Throws(YMExceptions::class)
    internal fun validateResponseCode(responseCode: Int) {
        /** Compare response code and throw exceptions accordingly  */
        when (responseCode) {
            YMConstants.RESPONSE_CODE_401 ->
                // Check whether provided inputs - UserName/Password combination is correct or not
                throw YMExceptions(YMErrorCode.USERNAME_PASSWORD_INVALID, EMPTY_STRING) // Error code - 111
            YMConstants.RESPONSE_CODE_500 ->
                // Internal Server error
                throw YMExceptions(YMErrorCode.SERVER_ERROR, EMPTY_STRING)

            YMConstants.RESPONSE_CODE_400 ->
                // Internal Server error
                throw YMExceptions(YMErrorCode.SERVER_ERROR, EMPTY_STRING)
        }
    }

}
