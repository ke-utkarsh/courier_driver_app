package ymsli.com.couriemate.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ymsli.com.couriemate.BuildConfig
import java.util.concurrent.TimeUnit

object DAPIoTNetworking {

    private fun getClient(): OkHttpClient.Builder? {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(logging).addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .build()
            chain.proceed(request)
        }
    }

    fun createNetworkService(): DAPIoTNetworkService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(
                getClient()!!
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DAPIoTNetworkService::class.java)
    }
}