package ymsli.com.couriemate.views.tasklist.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_tasks.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.FilterHelper
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.utils.common.TaskStatus
import ymsli.com.couriemate.views.tasklist.filter.TasksFilterDialog

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   DEC 20, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * OngoingTasksFragment : Subclass of BaseTasksFragment which adds additional features
 *                        required by the ongoing tasks list.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
class OngoingTasksFragment : BaseTasksFragment(TasksFragmentType.ONGOING){

    /** Fields for filter module and its helpers */
    private var filterHelper = FilterHelper(FilterHelper.ComparisonMode.ONGOING)
    private var filterDialogState = FilterModel.getDefaultState()
    private var filterDialog = TasksFilterDialog(TasksFilterDialog.FilterType.ONGOING)

    var timeStampFormatter = SimpleDateFormat(Constants.COURIEMATE_TIMESTAMP_FORMAT)

    /** Define comparator for ongoing tasks, sort by start date ASC */
    private val ongoingTasksComparator = Comparator<TaskRetrievalResponse>{ a, b ->
        var firstTaskStartedOn = timeStampFormatter.parse(a.startDate)
        var secondTaskStartedOn = timeStampFormatter.parse(b.startDate)
        firstTaskStartedOn.compareTo(secondTaskStartedOn)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.errorDataRefreshed.observe(this, Observer {
            pull_refresh.isRefreshing = false
        })

        viewModel.filterApplied.observe(this, Observer {
            if((it != null) && (filterDialog.isVisible)){
                filter(it)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        /**
         * Bind today's ongoing tasks
         * Don't show the first failed tasks assigned to direct driver
         */
        viewModel.getOngoingTasksForToday().observe(this, Observer {
            disableUserInteraction()
            val sortedList = it.sortedWith(ongoingTasksComparator)
            this.tasksList = ArrayList(sortedList)
            this.filteredList = filterHelper.filterByPredicate(this.tasksList, filterDialogState)
            taskListAdapter.clearData()
            taskListAdapter.loadData(filteredList)
            enableUserInteraction()
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
     * When filter option item is selected show the filter dialog.
     * @author Balraj VE00YM023
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.filter){
            filterDialog.show(activity?.supportFragmentManager, filterDialog.tag)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "OngoingTasksFragment"
        fun newInstance(): OngoingTasksFragment {
            val args = Bundle()
            val fragment =
                OngoingTasksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}