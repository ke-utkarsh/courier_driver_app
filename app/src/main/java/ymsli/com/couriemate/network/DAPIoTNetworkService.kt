package ymsli.com.couriemate.network

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ymsli.com.couriemate.model.DAPIoTFileUploadResponse
import javax.inject.Singleton

@Singleton
interface DAPIoTNetworkService {

    @POST("A000/iot-data/")
    fun sentLocationDataToDAPIoTServer(@Header("Content-Type")contentHeader: String,
                                       @Header("Accept")acceptHeader: String,
                                       @Header("x-uid")xuid: String,
                                       @Header("Content-Disposition")contentDisposition: String,
                                       @Body body: RequestBody
    ): Observable<DAPIoTFileUploadResponse>
}