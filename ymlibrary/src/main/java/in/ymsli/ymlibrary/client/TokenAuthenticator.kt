package `in`.ymsli.ymlibrary.client

import `in`.ymsli.ymlibrary.config.YMConstants
import `in`.ymsli.ymlibrary.errors.YMExceptions
import `in`.ymsli.ymlibrary.errors.YMLogMessages
import `in`.ymsli.ymlibrary.utils.SharedPrefsUtils
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class TokenAuthenticator(context: Context) : Authenticator{
    var token:String="";
    var context:Context;
//    constructor(context:Context) {
//        this.context = context
//    }

    init {
        this.context=context;
    }
    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request {
        token=  TokenHandler().execute().get();
        SharedPrefsUtils.setStringPreference(context,"token",token);
        return response.request().newBuilder()
                .header("Authorization", token)
                .build()
    }


   inner class TokenHandler() : AsyncTask<Void, Void, String>() {
       override fun doInBackground(vararg params: Void?): String? {
          var bodyParams:HashMap<String,String> = HashMap()
           bodyParams["userName"] = "user"
           bodyParams["Password"] = "password"
           bodyParams["grant_type"]="password"

           var result:HashMap<*,*> = HashMap<String,String>();
           val url = "http://192.168.224.14:8888/"
           val contentType = YMConstants.CONTENT_TYPE_URL_ENCODED
           val ymRequestBuilder = YMRequestBuilder(url,contentType)

           try {
               val apiCallService = RetrofitClient.getInstance().createClient(ymRequestBuilder,context)
               val call = apiCallService!!.postAsUrlEncoded("token", bodyParams)


               val response = call.execute()
               val responseCode = response.code()
               val responseMessage = response.message()
               val gson = Gson()
                result=gson.fromJson(response.body().string().toString(), HashMap::class.java)
               Log.e("hashmap-->", result.get("token_type") as String+" "+result.get("access_token") as String )
           }
           catch (ymExceptions: YMExceptions) {
               // syncHelperCallback.onResponse(ymExceptions.errorCode, ymArray.Data, ymExceptions.errorMessage)
               ymExceptions.printStackTrace()
               Log.e("error-->", ymExceptions.message)
               Log.e("logmessage-->", YMLogMessages.ERROR_MAIN_THREAD.name)

           }
           return result.get("token_type") as String+" "+result.get("access_token") as String
       }

       override fun onPreExecute() {
           super.onPreExecute()
           // ...
       }

       override fun onPostExecute(result: String?) {
           super.onPostExecute(result)
           // ...
       }
   }


}
