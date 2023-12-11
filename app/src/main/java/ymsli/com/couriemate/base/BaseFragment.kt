package ymsli.com.couriemate.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerFragmentComponent
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.di.module.FragmentModule
import android.view.*
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
 * BaseFragment : This abstract class is the base fragment of all the fragments
 *                  and contains common code of all the fragments
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    @Inject
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
        //setupObservers()
        viewModel.onCreate()
    }

    /**
     * used to create object of properties
     * annotated with @Inject
     * The feature is handled by Dagger2 framework
     */
    private fun buildFragmentComponent() =
        DaggerFragmentComponent
            .builder()
            .applicationComponent((context!!.applicationContext as CourieMateApplication).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(provideLayoutId(), container, false)

    /**
     * contains all livedata observers.
     * these parameters are present in
     * BaseViewModel class
     */
    protected open fun setupObservers() {
        viewModel.messageString.observe(viewLifecycleOwner, Observer {
            it?.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(viewLifecycleOwner, Observer {
            it?.data?.run { showMessage(this) }
        })
    }

    /**
     * used to ack user of any validation, server response, internet connection, etc
     */
    fun showMessage(message: String) = context?.let {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupObservers()
    }

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)

    protected abstract fun setupView(view: View)
}