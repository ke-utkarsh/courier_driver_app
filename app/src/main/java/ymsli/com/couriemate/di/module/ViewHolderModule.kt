package ymsli.com.couriemate.di.module

import androidx.lifecycle.LifecycleRegistry
import ymsli.com.couriemate.base.BaseItemViewHolder
import ymsli.com.couriemate.di.ViewModelScope
import dagger.Module
import dagger.Provides

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewHolderModule : This is the view holder module of dagger2 framework. This is
 *                      responsible for providing objects with @Inject annotation
 *                      in the view holder.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>)  {

    @Provides
    @ViewModelScope
    fun provideLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)
}