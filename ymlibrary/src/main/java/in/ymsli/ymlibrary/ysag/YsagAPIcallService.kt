/*
package com.ymsli.ymlibrary.ysag

import android.util.Log
import com.ymsli.ymlibrary.client.*
import com.ymsli.ymlibrary.config.YMConstants
import com.ymsli.ymlibrary.errors.YMExceptions
import com.ymsli.ymlibrary.utils.CommonUtils


object YsagAPIcallService {


    */
/**
     * Return success and error on API Call
     *//*

    interface OnYsagAPICallListner {

        // method to return error
        fun onError(ymBaseErrorResponse: YMBaseErrorResponse)
        // method to return success object
        fun onSuccess(ymResultObj: YMBaseResponse)
    }

    */
/**
     * @param username username
     * @param password password
     * @param requestComponent Object to provide information related to ysag request
     * @param listener to provide succes and error response
     *
     *//*

    fun login(username: String, password: String,requestComponent:RequestComponent ,listener: OnYsagAPICallListner) {

        val url = YMConstants.API_URL
        val ymRequestBuilder = YMRequestBuilder(url, "")
        ymRequestBuilder.headermap = YsagRequestUtils.getYSAGHeaders("",requestComponent)

        try {

            val apiCallService = RetrofitClient.getInstance().createClient(ymRequestBuilder,context)
            val call = apiCallService!!.postAsUrlEncoded(YMConstants.END_POINT_LOGIN_URL, YsagRequestUtils.getLoginParams(username, password,requestComponent))

            // calling method asynchronously
            YMServices.sendRequestASynchronous(call, object : YMServiceCallback() {

                override fun onFailure(ymBaseErrorResponse: YMBaseErrorResponse) {
                    listener.onError(ymBaseErrorResponse) //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSuccess(ymBaseResponse: YMBaseResponse) {


                   val data = ymBaseResponse.data as java.util.HashMap<String, String>

                    //responseCode in Int to check equality with standard response code

                    val responseCode:Int=  data.get("responseCode")!!.toInt()

                    val responseMessage:String=  data.get("responseMessage")!!

                    if (responseCode == YMConstants.RESPONSE_OK) {
                        listener.onSuccess(ymBaseResponse)
                    }

                    else {

                        ymBaseResponse.ymBaseErrorResponse=YMBaseErrorResponse(responseMessage,responseCode)
                        listener.onSuccess(ymBaseResponse)
                    }//To change body of created functions use File | Settings | File Templates.
                }


            }, HashMap::class.java)
        }
        catch (ymExceptions: YMExceptions) {
            ymExceptions.printStackTrace()
            CommonUtils.showLog(Log.ERROR,ymExceptions.errorMessage)

        }


    }

    */
/**
     * @param requestComponent object of request component related to ysag request
     * @param listener listener to provide success and failure
     *//*

    fun syncAppVersion(appVersion:Float,requestComponent:RequestComponent,listener: OnYsagAPICallListner) {

        val url = YMConstants.API_URL
        val ymRequestBuilder = YMRequestBuilder(url, "")
        ymRequestBuilder.headermap =YsagRequestUtils.getYSAGHeaders("",requestComponent )

        try {
            val map:HashMap<String,String> = HashMap()
            val apiCallService = RetrofitClient.getInstance().createClient(ymRequestBuilder,context)
            val call = apiCallService!!.postAsUrlEncoded(YMConstants.END_POINT_APP_INFO_URL,map)
            YMServices.sendRequestASynchronous(call, object : YMServiceCallback() {

                override fun onFailure(YmBaseErrorResponse: YMBaseErrorResponse) {
                    listener.onError(YmBaseErrorResponse) //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSuccess(ymBaseResponse: YMBaseResponse) {
                    val data = ymBaseResponse.data as java.util.HashMap<String, String>

                    //responseCode in Int to check equality with standard response code

                    val responseCode:Int=  data.get("responseCode")!!.toInt()
                    val responseMessage:String=  data.get("responseMessage")!!
                    if (responseCode == YMConstants.RESPONSE_OK) {
                        listener.onSuccess(ymBaseResponse)
                        }
                        else{
                        ymBaseResponse.ymBaseErrorResponse= YMBaseErrorResponse(responseMessage,responseCode)
                        listener.onSuccess(ymBaseResponse)
                        }



                    //To change body of created functions use File | Settings | File Templates.
                }


            }, HashMap::class.java)
        }
        catch (ymExceptions: YMExceptions) {
            ymExceptions.printStackTrace()
            CommonUtils.showLog(Log.ERROR,ymExceptions.errorMessage)

        }


    }
}*/
