package ymsli.com.couriemate.di.module

import android.content.Context
import android.location.LocationManager
import ymsli.com.couriemate.R
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ymsli.com.couriemate.adapters.TaskListAdapter
import ymsli.com.couriemate.base.BaseActivity
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.ViewModelProviderFactory
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import ymsli.com.couriemate.views.login.LoginViewModel
import ymsli.com.couriemate.views.main.MainViewModel
import ymsli.com.couriemate.views.notifications.NotificationListViewModel
import ymsli.com.couriemate.views.returntask.detail.ReturnTaskDetailViewModel
import ymsli.com.couriemate.views.splash.SplashViewModel
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.utils.common.NotificationsHandler

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ActivityModule : This is the activity module of dagger2 framework. This is
 *                      responsible for providing objects with @Inject annotation
 *                      in the activity.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideLoginViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                             networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ): LoginViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(LoginViewModel::class) {
                LoginViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(LoginViewModel::class.java)

    @Provides
    fun provideSplashViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                               networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository,
                               notificationsHandler: NotificationsHandler
    ): SplashViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(SplashViewModel::class) {
                SplashViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository,
                    notificationsHandler
                )
            }).get(SplashViewModel::class.java)

    @Provides
    fun provideTaskDetailViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                   networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ):TaskDetailViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(TaskDetailViewModel::class) {
                TaskDetailViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(TaskDetailViewModel::class.java)

    @Provides
    fun provideTaskListViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                   networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository,
                                 notificationsHandler: NotificationsHandler
    ):TaskListViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(TaskListViewModel::class) {
                TaskListViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository,
                    notificationsHandler
                )
            }).get(TaskListViewModel::class.java)

    @Provides
    fun provideNotificationListViewModel(schedulerProvider: SchedulerProvider,
                                         compositeDisposable: CompositeDisposable,
                                         networkHelper: NetworkHelper,
                                         couriemateRepository: CouriemateRepository,
                                         notificationsHandler: NotificationsHandler
    ): NotificationListViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(NotificationListViewModel::class) {
                NotificationListViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository,
                    notificationsHandler
                )
            }).get(NotificationListViewModel::class.java)


    @Provides
    fun provideReturnTaskDetailViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                         networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ): ReturnTaskDetailViewModel =

        ViewModelProviders.of(activity,
            ViewModelProviderFactory(ReturnTaskDetailViewModel::class) {
                ReturnTaskDetailViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(ReturnTaskDetailViewModel::class.java)

    @Provides
    fun provideTaskListAdapter() =
        TaskListAdapter(activity.lifecycle, ArrayList())

    @Provides
    fun provideLinearLayoutManager():LinearLayoutManager = LinearLayoutManager(activity.applicationContext)

    @Provides
    fun provideAlertDialogBuilder():AlertDialog.Builder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)

    @Provides
    fun provideLocationManager(): LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    fun provideMainViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                       networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ): MainViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(MainViewModel::class) {
                MainViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(MainViewModel::class.java)
}