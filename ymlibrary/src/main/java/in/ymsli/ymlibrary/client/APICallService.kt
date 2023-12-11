/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * APICallService : class provides request mthods with diffrent type of request
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.client

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface APICallService {

    /** Post request with endpoint url and body, it can be any object
     * @param url dynamic end-point url for api
     * @param params   body is raw-body as json
     * @return  response of passed object type from api
     */

    @POST
    fun postJson(@Url url: String, @Body body: Any): Call<ResponseBody>


    /** Post request as url encoded form,
     * @param url dynamic end-point url for api
     * @param params   hashmap is k,v pair of inputs
     * @return  response of passed object type from api
     */
    @FormUrlEncoded
    @POST
    fun postAsUrlEncoded(@Url url: String, @FieldMap hashmap: Map<String, String>): Call<ResponseBody>


    /** Get request with query parametrs
     * @param url dynamic end-point url for api
     * @param params   Map of parameteres which need to send as body
     * @return raw response from api
     */
    @GET
    fun getJSON(@Url url: String, @QueryMap querymap: Map<String, String>): Call<ResponseBody>


    /**
     * Post request with multipart data
     * @param params   Map of parameteres which need to send as body
     * @param filePart  Multipart data
     * @return raw response from api
     */
    @Multipart
    @POST
    fun uploadFile(@Url url: String, @PartMap params: Map<String, Any>, @Part vararg filePart: MultipartBody.Part): Call<ResponseBody>


    /** Put request with endpoint url and hashmap as raw body
     * @param url dynamic end-point url for api
     * @param params   body is raw-body as json
     * @return  response of passed object type from api
     */

    @PUT
    fun putJson(@Url url: String, @Body body: Any): Call<ResponseBody>


    /** Put request with query parametrs
     * @param url dynamic end-point url for api
     * @param params   Map of parameteres which need to send as body
     * @return raw response from api
     */
    @FormUrlEncoded
    @PUT()
    fun putAsUrlEncoded(@Url url: String, @FieldMap fields: Map<String, String>): Call<ResponseBody>

}
