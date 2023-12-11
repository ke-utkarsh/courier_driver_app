/**
 * Project Name :YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 11, 2018

 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.

 * Description
 * -----------------------------------------------------------------------------------

 * RetrofitClient : Provides request body for some predefined type
 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description

 * -----------------------------------------------------------------------------------

 */

package `in`.ymsli.ymlibrary.client

import `in`.ymsli.ymlibrary.errors.YMLogMessages
import `in`.ymsli.ymlibrary.utils.CommonUtils
import `in`.ymsli.ymlibrary.utils.SharedPrefsUtils
import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class RetrofitClient {


    companion object {

        // Singleton Object to provide Retrofit Client object
        private var mInstance: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient {
            if (mInstance == null) {
                mInstance = RetrofitClient()
            }
            return mInstance as RetrofitClient
        }
    }


    /**
     * To create client based on YMRequestBuilder object fields
     * @param ymRequestBuilder Object of YMRequestBuilder for creating request client
     * @return object of APICallService class to call its different methods
     */
    fun createClient(ymRequestBuilder: YMRequestBuilder,context: Context): APICallService? {
      var token= SharedPrefsUtils.getStringPreference(context,"token");
        if(token==null){
            token=""
        }

        if (ymRequestBuilder.baseUrl == null || ymRequestBuilder.baseUrl!!.isEmpty() || !ymRequestBuilder.baseUrl!!.endsWith("/")) {

            CommonUtils.showLog(Log.ERROR, YMLogMessages.ERROR_BASE_URL.logMessage)
        } else {
            val httpClient = OkHttpClient.Builder()
            var tokenAuthenticator=TokenAuthenticator(context)

            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val originalRequest = chain.request()

                    val builder = originalRequest.newBuilder()
                    //add headers from given header map in requestbuilder
                    for ((key, value) in ymRequestBuilder.headermap!!) {
                        CommonUtils.showLog(Log.INFO, "headers---->"+ymRequestBuilder.headermap)
                        builder.addHeader(key, value)
                    }

                    builder.addHeader("Authorization", token)
                    val newRequest = builder.build()
                    return chain.proceed(newRequest)
                }
            })

            httpClient.authenticator(tokenAuthenticator)
            val okHttpClient=httpClient.build()
            //Time out based on given timeout unit and time
            httpClient.connectTimeout(ymRequestBuilder.connectTimeOut!!, ymRequestBuilder.timeOutUnit)
                    .writeTimeout(ymRequestBuilder.writeTimeOut!!, ymRequestBuilder.timeOutUnit)
                    .readTimeout(ymRequestBuilder.readTimeOut!!, ymRequestBuilder.timeOutUnit)

            // retrofit client creation
            val retrofit = Retrofit.Builder()
                    .baseUrl(ymRequestBuilder.baseUrl).addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()




            val onlineOCRApi = retrofit.create(APICallService::class.java)

            return onlineOCRApi
        }
        return null

    }
}





