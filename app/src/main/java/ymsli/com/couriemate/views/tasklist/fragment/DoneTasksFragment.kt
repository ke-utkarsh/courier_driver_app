package ymsli.com.couriemate.views.tasklist.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import ymsli.com.couriemate.R
import ymsli.com.couriemate.utils.common.FilterHelper
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.views.tasklist.filter.TasksFilterDialog
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_tasks.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   DEC 20, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * DoneTasksFragment : Subclass of BaseTasksFragment which adds additional features
 *                     required by the Done tasks list.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
class DoneTasksFragment : BaseTasksFragment(TasksFragmentType.DONE){

    /** Fields for filter module and its helpers */
    private var filterHelper = FilterHelper(FilterHelper.ComparisonMode.DONE)
    private var filterDialog = TasksFilterDialog(TasksFilterDialog.FilterType.DONE)
    private var filterDialogState = FilterModel.getDefaultState()

    override fun setupObservers() {
        super.setupObservers()

        viewModel.errorDataRefreshed.observe(this, Observer {
            pull_refresh.isRefreshing = false
        })

        /** observe the today's assigned tasks */
        viewModel.getDoneTasksForToday().observe(this, Observer {
            viewModel.doneListSorter(it)
        })

        viewModel.sortedDoneTasks.observe(this, Observer {
            disableUserInteraction()
            this.tasksList = ArrayList(it)
            this.filteredList = filterHelper.filterByPredicate(this.tasksList, filterDialogState)
            taskListAdapter.clearData()
            taskListAdapter.loadData(filteredList)
            enableUserInteraction()
        })
        viewModel.filterApplied.observe(this, Observer {
            if((it != null) && (filterDialog.isVisible)){
                filter(it)
            }
        })
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
     * Show the filter dialog when filter option item is selected.
     * @author Balraj VE00YM023
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.filter){
            filterDialog.show(activity?.supportFragmentManager, filterDialog.tag)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "DoneTasksFragment"
        fun newInstance(): DoneTasksFragment {
            val args = Bundle()
            val fragment =
                DoneTasksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}