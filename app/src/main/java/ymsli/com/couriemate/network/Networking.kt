package ymsli.com.couriemate.network

import ymsli.com.couriemate.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Networking{

    var couriemateNetworkService = CouriemateNetworkService::class.java
    lateinit var cns:CouriemateNetworkService

    fun createCouriemateNetworkService(refreshTokenInterceptor: RefreshTokenInterceptor)
            : CouriemateNetworkService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(
                        OkHttpClient.Builder()
                                .addInterceptor(loggingInterceptor)
                                .addInterceptor(refreshTokenInterceptor)
                                .connectTimeout(60, TimeUnit.SECONDS)
                                .readTimeout(60, TimeUnit.SECONDS)
                                .writeTimeout(60, TimeUnit.SECONDS)
                                .retryOnConnectionFailure(false)
                                .build()
                )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(couriemateNetworkService)
            .apply {
                cns = this
            }
    }
}