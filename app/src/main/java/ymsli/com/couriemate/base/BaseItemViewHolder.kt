package ymsli.com.couriemate.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerViewHolderComponent
import ymsli.com.couriemate.di.component.ViewHolderComponent
import ymsli.com.couriemate.di.module.ViewHolderModule
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseItemViewHolder : This abstract class is the base item view holder of all the
 *                  view holders of recycler view, contains common code to all
 *                   view holders of recycler view
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
abstract class BaseItemViewHolder<T : Any, VM : BaseItemViewModel<T>>(
    @LayoutRes layoutId: Int, parent: ViewGroup
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)),
    LifecycleOwner {

    init {
        onCreate()
    }

    @Inject
    lateinit var viewModel: VM

    @Inject
    lateinit var lifecycleRegistry: LifecycleRegistry

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    open fun bind(data: T) {
        viewModel.updateData(data)
    }

    protected fun onCreate() {
        injectDependencies(buildViewHolderComponent())
        lifecycleRegistry.markState(Lifecycle.State.INITIALIZED)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
        setupObservers()
        setupView(itemView)
    }

    /**
     * handles view holder lifecycle upon start/resume state
     */
    fun onStart() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        lifecycleRegistry.markState(Lifecycle.State.RESUMED)
    }

    /**
     * handles view holder lifecycle upon start/create state
     */
    fun onStop() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    /**
     * handles view holder lifecycle upon destruction
     */
    fun onDestroy() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    /**
     * used to create object of properties
     * annotated with @Inject
     * The feature is handled by Dagger2 framework
     */
    private fun buildViewHolderComponent() =
        DaggerViewHolderComponent
            .builder()
            .applicationComponent((itemView.context.applicationContext as CourieMateApplication).applicationComponent)
            .viewHolderModule(ViewHolderModule(this))
            .build()

    /**
     * used to ack user of any validation, server response, internet connection, etc
     */
    fun showMessage(message: String) = Toast.makeText(itemView.context,message,Toast.LENGTH_SHORT).show()

    fun showMessage(@StringRes resId: Int) = showMessage(itemView.context.getString(resId))

    /**
     * contains all livedata observers.
     * these parameters are present in
     * BaseViewModel class
     */
    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it?.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it?.data?.run { showMessage(this) }
        })
    }

    protected abstract fun injectDependencies(viewHolderComponent: ViewHolderComponent)

    abstract fun setupView(view: View)

}