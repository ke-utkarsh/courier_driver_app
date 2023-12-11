package ymsli.com.couriemate.di.component

import ymsli.com.couriemate.di.ViewModelScope
import ymsli.com.couriemate.di.module.ViewHolderModule
import dagger.Component
import ymsli.com.couriemate.adapters.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewHolderComponent : This is the viewholder component of dagger2 framework. This is
 *                      responsible for injecting objects in the viewholder of RV.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    fun inject(viewHolder : TaskListViewHolder)

    fun inject(notificationListViewHolder: NotificationListViewHolder)

    fun inject(viewHolder: TaskHistoryViewHolder)

    fun inject(viewHolder: TransactionHistoryViewHolder)
}