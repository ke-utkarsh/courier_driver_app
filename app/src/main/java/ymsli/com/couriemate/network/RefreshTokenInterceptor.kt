package ymsli.com.couriemate.network

import ymsli.com.couriemate.database.CouriemateDatabase
import ymsli.com.couriemate.preferences.CouriemateSharedPreferences
import ymsli.com.couriemate.repository.CouriemateRepository
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshTokenInterceptor @Inject constructor(private val prefs: CouriemateSharedPreferences,
                                                  private val database: CouriemateDatabase)
    : Interceptor{

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val respCode = response.code()
        if((respCode==403)||(respCode==401)){
            logoutUser()
        }
        return response
    }

    private fun logoutUser(){
        database.taskHistoryDao().removeDriverHistoryTasks()
        database.taskSummaryDao().clearDriverTaskSummary()
        database.notificationsDao().clearNotifications()
        prefs.setActiveNotificationCount("0")
        prefs.eraseLogoutDataFromSharedPreferences()
    }
}