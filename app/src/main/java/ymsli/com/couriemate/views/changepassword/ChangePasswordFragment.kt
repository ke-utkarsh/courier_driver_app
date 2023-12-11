package ymsli.com.couriemate.views.changepassword

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.utils.common.ViewUtils
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import kotlinx.android.synthetic.main.activity_password_change.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Oct 15, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * ChangePasswordFragment : Provides the change password feature
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class ChangePasswordFragment : BaseFragment<ChangePasswordViewModel>() {

    companion object {
        const val TAG = "ChangePasswordFragment"
        fun newInstance(): ChangePasswordFragment {
            val args = Bundle()
            val fragment =
                ChangePasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.activity_change_password

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    /**
     * Configures all the User Interaction elements such as text input fields and buttons.
     * sets up text change listeners on the text input fields and click listener on the
     * password change button.
     * @author Balraj VE00YM023
     */
    override fun setupView(view: View) {
        setHasOptionsMenu(true)
        setInputFilters()
        btn_password_change_activity_ok.setOnClickListener { viewModel.changePassword() }

        /** Add text change listener to the current password input field */
        input_password_change_activity_current_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onCurrentPasswordChange(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        /** Add text change listener to the new password input field */
        input_password_change_activity_new_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onNewPasswordChange(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        /** Add text change listener to the retype new password input field */
        input_password_change_activity_retype_new_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onRetypeNewPasswordChange(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /** Hide the task bar menu items */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.findItem(R.id.search_task_list_toolbar)?.isVisible = false
    }


    override fun setupObservers() {
        super.setupObservers()

        /**
         * Set observer to show/hide progress bar and enable/disable user interaction.
         */
        viewModel.apiRequestActive.observe(this, Observer{
            if(it){
                progress_bar_password_change_activity.visibility = View.VISIBLE
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
            else{
                progress_bar_password_change_activity.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
    }

    /**
     * Sets the input filters for all the edit text's.
     * this particular applyReturnListFilter disallows any space character input.
     * @author Balraj VE00YM023
     */
    private fun setInputFilters(){
        val filters = arrayOf(ViewUtils.getSpaceFilter())
        input_password_change_activity_current_password.filters = filters
        input_password_change_activity_new_password.filters = filters
        input_password_change_activity_retype_new_password.filters = filters
    }
}