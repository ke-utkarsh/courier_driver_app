package ymsli.com.couriemate.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.database.CouriemateDatabase
import ymsli.com.couriemate.di.ApplicationContext
import ymsli.com.couriemate.di.module.ApplicationModule
import ymsli.com.couriemate.network.CouriemateNetworkService
import ymsli.com.couriemate.preferences.CouriemateSharedPreferences
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.network.DAPIoTNetworkService
import ymsli.com.couriemate.utils.common.NotificationsHandler
import javax.inject.Singleton

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ApplicationComponent : This is the applicaiton component of dagger2 framework. This is
 *                      responsible for injecting objects in the application.
 *                       The application component will be used by activity component to
 *                       inject dependencies in the activity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: CourieMateApplication)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    fun getSharedPreferences(): SharedPreferences

    fun getNetworkHelper(): NetworkHelper

    fun getCouriemateRepository(): CouriemateRepository

    fun getSchedulerProvider(): SchedulerProvider

    fun getCompositeDisposable(): CompositeDisposable

    fun getCouriemateNetworkService(): CouriemateNetworkService

    fun getDAPIoTNetworkService(): DAPIoTNetworkService

    fun getCouriemateSharedPreferences(): CouriemateSharedPreferences

    fun getCouriemateDatabase(): CouriemateDatabase

    fun getNotificationHandler(): NotificationsHandler

}