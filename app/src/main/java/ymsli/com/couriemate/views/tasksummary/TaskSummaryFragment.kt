package ymsli.com.couriemate.views.tasksummary

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.utils.common.TaskSummaryPeriod
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_task_summary.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 11, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TaskSummaryFragment : Shows a summary of tasks
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TaskSummaryFragment : BaseFragment<TaskSummaryViewModel>() {

    override fun provideLayoutId(): Int = R.layout.fragment_task_summary

    override fun injectDependencies(fragmentComponent: FragmentComponent)
            = fragmentComponent.inject(this)

    /**
     * Retrieve the task summary data for this driver id.
     * and select spinner's item selected listener
     * @author Balraj VE00YM023
     */
    override fun setupView(view: View) {
        setHasOptionsMenu(true)
        viewModel.getDriverId()?.let {
            viewModel.getDriverTaskSummaryFromServer(it)
        }
        spinner_task_summary.onItemSelectedListener = itemSelectedListener
    }

    override fun setupObservers() {
        /** Once API call finishes(successfully or unsuccessfully), load task summary
         *  from local database (Room). */
        viewModel.apiCallFinished.observe(this, Observer {
            viewModel.getDriverTaskSummary(TaskSummaryPeriod.TODAY.type)
        })

        /** Bind the summary data to UI */
        viewModel.taskSummary.observe(this, Observer {
            it?.let {
                tv_task_summary_assigned.text   = getDisplayTextByTaskNo(it.assigned)
                tv_task_summary_delivering.text = getDisplayTextByTaskNo(it.delivering)
                tv_task_summary_delivered.text  = getDisplayTextByTaskNo(it.delivered)
                tv_task_summary_failed.text     = getDisplayTextByTaskNo(it.failed)
                tv_task_summary_refused.text    = getDisplayTextByTaskNo(it.refused)
                tv_task_summary_returned.text   = getDisplayTextByTaskNo(it.returned)
                tv_task_summary_total.text      = it.total.toString()
                tv_task_summary_total_delivery_fee.text = it.totalDeliveryFee.toString()
                tv_task_summary_total_cash.text = it.totalCashReceived.toString()
                tv_task_summary_total_other_payment.text = it.totalOtherPaymentReceived.toString()
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

    private fun getDisplayTextByTaskNo(taskNo: Int): String{
        return if(taskNo == 1) "1 Task" else "$taskNo Tasks"
    }

    /** Define the item selected listener for the task summary period drop down */
    private val itemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(
            parent: AdapterView<*>?, view: View?,
            position: Int, id: Long) {
            viewModel.getDriverTaskSummary(position + 1)
        }
    }

    companion object {
        const val TAG = "TaskSummaryFragment"
        fun newInstance(): TaskSummaryFragment {
            val args = Bundle()
            val fragment =
                TaskSummaryFragment()
            fragment.arguments = args
            return fragment
        }
    }
}