package ymsli.com.couriemate.views.chat

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_chat.*
import org.jivesoftware.smack.packet.Presence
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.utils.common.Constants.Companion.EMPTY_STRING
import ymsli.com.couriemate.utils.common.isValid
import ymsli.com.couriemate.views.tasklist.TaskListViewModel


/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj (VE00YM023)
 * @date    January 12, 2021
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ChatFragment : Implements the user chat feature UI element
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

class ChatFragment : BaseFragment<TaskListViewModel>() {

    companion object {
        const val TAG = "ChatFragment"
        fun newInstance(): ChatFragment {
            val args = Bundle()
            val fragment = ChatFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId() = R.layout.fragment_chat
    override fun injectDependencies(fc: FragmentComponent) = fc.inject(this)
    override fun setupView(view: View) {
        setHasOptionsMenu(true)
        when(viewModel.xmppConfig.isValid()){
            true -> btn_send_message.setOnClickListener{sendMessageListener()}
            else -> showMessage("Failed to start chat, Please try again")
        }
    }

    private fun sendMessageListener(){
        hideSoftInput()
        if (viewModel.message.isNotEmpty()) {
            viewModel.sendChatMessage()
            et_message.setText(EMPTY_STRING)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        val userId = "${viewModel.xmppConfig?.userName}@${viewModel.xmppConfig?.serverDomain}"
        et_message.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.message = s?.toString()?.trim() ?: EMPTY_STRING
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.getUserChat().observe(this, Observer {
            tv_no_data.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            val adapter = ChatAdapter(userId, it)
            rv_chat.adapter = adapter
            rv_chat.scrollToPosition(it.size - 1)
        })

        viewModel.chatError.observe(this, Observer {
            if(it.isNotEmpty()) {
                showMessage(it)
                viewModel.chatError.value = EMPTY_STRING
            }
        })
    }

    /**
     * As tool bar is associated with the Main Activity, we have to hide
     * some toolbar menu items that are not reagent to us in this fragment.
     * @author Balraj VE00YM023
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.findItem(R.id.search_task_list_toolbar)?.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        viewModel.chatFragmentLoaded = true
        viewModel.deleteNotification()
        if(viewModel.xmppConfig.isValid()) { viewModel.setChatStatus(Presence.Type.available) }
    }

    override fun onPause() {
        super.onPause()
        viewModel.chatFragmentLoaded = false
    }

    private fun hideSoftInput() {
        val imm: InputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}