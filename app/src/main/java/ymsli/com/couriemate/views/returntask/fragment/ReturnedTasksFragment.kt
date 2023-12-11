package ymsli.com.couriemate.views.returntask.fragment

import ymsli.com.couriemate.R
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.ViewUtils
import ymsli.com.couriemate.views.returntask.filter.ReturnTaskFilterDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_tasks.*
import ymsli.com.couriemate.utils.common.FilterHelper
import java.util.logging.Filter

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 6, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ReturnedTasksFragment : Subclass of BaseReturnTasksFragment which is used for the
 *                         Returned tab on Return tasks screen.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class ReturnedTasksFragment : BaseReturnTasksFragment(TasksFragmentType.ONGOING){

    /** Fields for filter module and its helpers */
    private var filterHelper = FilterHelper(FilterHelper.ComparisonMode.DONE)
    private var filterDialogState = FilterModel.getDefaultState()
    private var filterDialog = ReturnTaskFilterDialog()

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getReturnTasks()
        viewModel.getDeliveringReturnTasks().observe(this, Observer{
            disableUserInteraction()
            val sortedList = it.sortedWith(ViewUtils.taskListComparator)
            this.tasksList = ArrayList(sortedList)
            this.filteredList = filterHelper.filterByPredicate(tasksList, filterDialogState)
            taskListAdapter.clearData()
            taskListAdapter.loadData(filteredList)
            enableUserInteraction()
        })
        viewModel.errorDataRefreshed.observe(this, Observer {
            pull_refresh.isRefreshing = false
        })
        viewModel.filterApplied.observe(this, Observer {
            if((it != null) && (filterDialog.isVisible)){
                filter(it)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        filterDialogState = FilterModel.getDefaultState()
    }

    /**************** Filter module helper functions ************************/
    fun filter(predicate: FilterModel) {
        viewModel.filterState.value = predicate
        this.filterDialogState = predicate
        filterDialog.dismiss()
        setFilterIcon(predicate)
        filteredList = filterHelper.filterByPredicate(tasksList, predicate)
        taskListAdapter.loadData(filteredList)
    }

    /**
     * if any filter is set then show the filter icon in toolbar with a blue tint,
     * otherwise show the default black icon.
     * @author Balraj VE00YM023
     */
    private fun setFilterIcon(predicate: FilterModel){
        if (predicate.isSet()) {
            filter!!.icon = activity?.getDrawable(R.drawable.ic_sort_blue_24dp)
        }
        else{
            filter!!.icon = activity?.getDrawable(R.drawable.ic_sort_black_24dp)
        }
    }

    /**
     * When filter menu item is selected then show the FilterDialog.
     * @author Balraj VE00YM023
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.filter){
            filterDialog.show(activity?.supportFragmentManager, filterDialog.tag)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "DeliveringReturnTasksFragment"
        fun newInstance(): ReturnedTasksFragment {
            val args = Bundle()
            val fragment =
                ReturnedTasksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}