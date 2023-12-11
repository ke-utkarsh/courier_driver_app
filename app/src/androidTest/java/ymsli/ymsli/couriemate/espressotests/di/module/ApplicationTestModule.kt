package ymsli.com.couriemate.di.module

import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.database.CouriemateDatabase
import ymsli.com.couriemate.di.ApplicationContext
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.RxSchedulerProvider
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.network.*
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.common.NotificationsHandler
import javax.inject.Singleton

@Module
class ApplicationTestModule(private val application: CourieMateApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("couriemate-shared-prefs", Context.MODE_PRIVATE)

    /**
     * We need to write @Singleton on the provide method if we are create the instance inside this method
     * to make it singleton. Even if we have written @Singleton on the instance's class
     */
    @Provides
    @Singleton
    fun provideDatabaseService(): CouriemateDatabase =
        Room.databaseBuilder(
            application, CouriemateDatabase::class.java,
            "couriemate-room-database"
        ).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelper(application)

    @Provides
    @Singleton
    fun provideCouriemateNetworkService(refreshTokenInterceptor: RefreshTokenInterceptor)
            : CouriemateNetworkService = Networking.createCouriemateNetworkService(refreshTokenInterceptor)

    @Provides
    @Singleton
    fun provideDAPIoTNetworkService()
            : DAPIoTNetworkService = DAPIoTNetworking.createNetworkService()

    @Provides
    fun provideNotificationHandler(application: Application,
                                   repository: CouriemateRepository,
                                   schedulerProvider: SchedulerProvider,
                                   compositeDisposable: CompositeDisposable): NotificationsHandler {
        return NotificationsHandler(application, repository, schedulerProvider, compositeDisposable)
    }

}