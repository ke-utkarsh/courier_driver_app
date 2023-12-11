package ymsli.com.couriemate.di.module

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class ForegroundServiceModule (private val service: Service){

    @Provides
    fun provideNotificationManager(): NotificationManager =
        service.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun provideLocationRequest(): LocationRequest =
        LocationRequest()

    @Provides
    fun provideFusedLocationProviderClient(): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(service)
}