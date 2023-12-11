package ymsli.com.couriemate.di.component

import ymsli.com.couriemate.di.FragmentScope
import ymsli.com.couriemate.di.module.FragmentModule
import ymsli.com.couriemate.views.changepassword.ChangePasswordFragment
import ymsli.com.couriemate.views.notifications.NotificationListFragment
import ymsli.com.couriemate.views.returntask.fragment.BaseReturnTasksFragment
import ymsli.com.couriemate.views.returntask.fragment.TabContainerReturnTaskList
import ymsli.com.couriemate.views.taskdetail.taskdetailfragments.*
import ymsli.com.couriemate.views.taskhistory.TaskHistoryFragment
import ymsli.com.couriemate.views.tasklist.fragment.BaseTasksFragment
import ymsli.com.couriemate.views.tasklist.fragment.TabbedTaskListContainerFragment
import ymsli.com.couriemate.views.tasksummary.TaskSummaryFragment
import dagger.Component
import ymsli.com.couriemate.views.chat.ChatFragment
import ymsli.com.couriemate.views.payment.PaymentRegistrationFragment
import ymsli.com.couriemate.views.transaction.TransactionHistoryFragment
import ymsli.com.couriemate.views.transaction.TransactionRegistrationFragment

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * FragmentComponent : This is the fragment component of dagger2 framework. This is
 *                      responsible for injecting objects in the fragment.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@FragmentScope
@Component(dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(deliverFragment: DeliverFragment)

    fun inject(failFragment: FailCustomerFragment)

    fun inject(refuseFragment: RefuseFragment)

    fun inject(failCarrierFragment: FailCarrierFragment)

    fun inject(carrierDeliveryFragment: CarrierDeliveryFragment)

    fun inject(changePasswordFragment: ChangePasswordFragment)

    fun inject(tabbedTaskListContainerFragment: TabbedTaskListContainerFragment)

    fun inject(tabContainerReturnTaskList: TabContainerReturnTaskList)

    fun inject(baseTasksFragment: BaseTasksFragment)

    fun inject(baseReturnTasksFragment: BaseReturnTasksFragment)

    fun inject(taskSummaryFragment: TaskSummaryFragment)

    fun inject(taskHistoryFragment: TaskHistoryFragment)

    fun inject(notificationListFragment: NotificationListFragment)

    fun inject(paymentRegistrationFragment: PaymentRegistrationFragment)

    fun inject(transactionRegistrationFragment: TransactionRegistrationFragment)

    fun inject(transactionHistoryFragment: TransactionHistoryFragment)

    fun inject(chatFragment: ChatFragment)

}