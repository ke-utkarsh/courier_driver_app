package ymsli.com.couriemate.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.database.CouriemateDatabase
import ymsli.com.couriemate.di.ApplicationContext
import ymsli.com.couriemate.preferences.CouriemateSharedPreferences
import ymsli.com.couriemate.utils.NetworkHelper
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.network.*
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.common.NotificationsHandler
import javax.inject.Singleton
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import ymsli.com.couriemate.utils.rx.RxSchedulerProvider

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ApplicationModule : This is the application module of dagger2 framework. This is
 *                      responsible for providing objects with @Inject annotation
 *                      in the application.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Module
class ApplicationModule (private val application: CourieMateApplication){

    @Provides
    @Singleton
    fun provideApplication():Application = application

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider =
        RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("couriemate-shared-prefs", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper =
        NetworkHelper(application)

    @Provides
    @Singleton
    fun provideCouriemateNetworkService(refreshTokenInterceptor: RefreshTokenInterceptor)
            : CouriemateNetworkService = Networking.createCouriemateNetworkService(refreshTokenInterceptor)

    @Provides
    @Singleton
    fun provideDAPIoTNetworkService()
            : DAPIoTNetworkService = DAPIoTNetworking.createNetworkService()

    @Provides
    @Singleton
    fun provideCouriemateSharedPreferences(): CouriemateSharedPreferences =
        CouriemateSharedPreferences(provideSharedPreferences())

    @Provides
    @Singleton
    fun provideCouriemateDatabase(): CouriemateDatabase =
        Room.databaseBuilder(application, CouriemateDatabase::class.java,
            "couriemate-room-database").fallbackToDestructiveMigration().allowMainThreadQueries().build()

    @Provides
    fun provideRefreshTokenInterceptor():RefreshTokenInterceptor =
            RefreshTokenInterceptor(provideCouriemateSharedPreferences(),provideCouriemateDatabase())

    @Provides
    fun provideNotificationHandler(application: Application,
                                    repository: CouriemateRepository,
                                   schedulerProvider: SchedulerProvider,
                                   compositeDisposable: CompositeDisposable): NotificationsHandler{
        return NotificationsHandler(application, repository, schedulerProvider, compositeDisposable)
    }

}