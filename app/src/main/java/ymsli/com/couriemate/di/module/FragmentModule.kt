package ymsli.com.couriemate.di.module

import ymsli.com.couriemate.adapters.NotificationListAdapter
import ymsli.com.couriemate.adapters.TaskHistoryAdapter
import ymsli.com.couriemate.adapters.TaskListAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.ViewModelProviderFactory
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import ymsli.com.couriemate.views.changepassword.ChangePasswordViewModel
import ymsli.com.couriemate.views.notifications.NotificationListViewModel
import ymsli.com.couriemate.views.returntask.list.ReturnTaskListViewModel
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import ymsli.com.couriemate.views.tasksummary.TaskSummaryViewModel
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
 * FragmentModule : This is the fragment module of dagger2 framework. This is
 *                      responsible for providing objects with @Inject annotation
 *                      in the fragment.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Module
class FragmentModule(private val fragment : BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideTaskListAdapter() =
        TaskListAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideTaskHistoryAdapter() =
        TaskHistoryAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideNotificationListAdapter() =
        NotificationListAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideTaskDetailViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                   networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ): TaskDetailViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(TaskDetailViewModel::class) {
                TaskDetailViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(TaskDetailViewModel::class.java)

    @Provides
    fun provideChangePasswordViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                             networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ): ChangePasswordViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(ChangePasswordViewModel::class) {
                ChangePasswordViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(ChangePasswordViewModel::class.java)

    @Provides
    fun provideTaskSummaryViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                       networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ): TaskSummaryViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(TaskSummaryViewModel::class) {
                TaskSummaryViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(TaskSummaryViewModel::class.java)

    @Provides
    fun provideTaskListViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                       networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository,
                                 notificationsHandler: NotificationsHandler
    ): TaskListViewModel =
        ViewModelProviders.of(fragment.activity!!,
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
    fun provideReturnTaskListViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                       networkHelper: NetworkHelper, couriemateRepository: CouriemateRepository
    ): ReturnTaskListViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(ReturnTaskListViewModel::class) {
                ReturnTaskListViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository
                )
            }).get(ReturnTaskListViewModel::class.java)


    @Provides
    fun provideNotificationListViewModel(schedulerProvider: SchedulerProvider,
                                         compositeDisposable: CompositeDisposable,
                                         networkHelper: NetworkHelper,
                                         couriemateRepository: CouriemateRepository,
                                         notificationsHandler: NotificationsHandler
    ): NotificationListViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(NotificationListViewModel::class) {
                NotificationListViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    couriemateRepository,
                    notificationsHandler
                )
            }).get(NotificationListViewModel::class.java)

}