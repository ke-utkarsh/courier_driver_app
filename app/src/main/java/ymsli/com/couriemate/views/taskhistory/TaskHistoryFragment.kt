package ymsli.com.couriemate.views.taskhistory

import android.icu.text.SimpleDateFormat
import ymsli.com.couriemate.R
import ymsli.com.couriemate.adapters.TaskHistoryAdapter
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_task_history.*
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.FilterHelper
import ymsli.com.couriemate.utils.common.ViewUtils
import ymsli.com.couriemate.views.taskhistory.filter.TaskHistoryFilterDialog
import ymsli.com.couriemate.views.taskhistory.filter.TaskHistoryFilterHelper
import ymsli.com.couriemate.views.taskhistory.filter.TaskHistoryFilterModel
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
 * TaskHistoryFragment : This is the Task History fragment in the couriemate android application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class TaskHistoryFragment: BaseFragment<TaskListViewModel>() {

    private var filter: MenuItem? = null
    companion object {
        const val TAG = "TaskHistoryFragment"
        fun newInstance(): TaskHistoryFragment {
            val args = Bundle()
            val fragment =
                TaskHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var taskHistoryAdapter: TaskHistoryAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    /* Fields for recycler view applyReturnListFilter module */
    private lateinit var tasksHistoryList: ArrayList<TaskHistoryResponse>

    /** Fields used by the filter functionality */
    private var filterDialog = TaskHistoryFilterDialog()
    private val filterHelper = TaskHistoryFilterHelper()
    private var filterDialogState = TaskHistoryFilterModel.getDefaultState()
    private lateinit var filteredList: ArrayList<TaskHistoryResponse>

    override fun provideLayoutId(): Int = R.layout.fragment_task_history

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        no_history_textView.visibility = View.GONE
        recycler_taskList.adapter = taskHistoryAdapter
        recycler_taskList.layoutManager = linearLayoutManager
        viewModel.getDriverTasksHistory()
        setHasOptionsMenu(true)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getDriverTasksFromRoom().observe(this, Observer {
            tasksHistoryList = ArrayList(it.sortedWith(ViewUtils.taskHistoryComparatorEndDate))
            this.filteredList = filterHelper.filterByPredicate(tasksHistoryList, filterDialogState)
            taskHistoryAdapter.loadData(filteredList)
            setupViewBasedOnDataSize(filteredList.size)
        })
        viewModel.taskHistoryFilterApplied.observe(this, Observer {
            if((it != null) && (filterDialog.isVisible)){
                filter(it)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        filterDialogState = TaskHistoryFilterModel.getDefaultState()
    }

    private fun setupViewBasedOnDataSize(dataSize: Int){
        if(dataSize == 0){
            recycler_taskList.visibility = View.GONE
            no_history_textView.visibility = View.VISIBLE
        }
        else{
            recycler_taskList.visibility = View.VISIBLE
            no_history_textView.visibility = View.GONE
        }
    }

    /**************** Filter module helper functions ************************/
    fun filter(predicate: TaskHistoryFilterModel) {
        viewModel.taskHistoryFilterState.value = predicate
        this.filterDialogState = predicate
        filterDialog.dismiss()
        setFilterIcon(predicate)
        filteredList = filterHelper.filterByPredicate(tasksHistoryList, predicate)
        taskHistoryAdapter.loadData(filteredList)
        taskHistoryAdapter.notifyDataSetChanged()
        setupViewBasedOnDataSize(filteredList.size)
    }

    /**
     * if any filter is set then show the filter icon in toolbar with a blue tint,
     * otherwise show the default black icon.
     */
    private fun setFilterIcon(predicate: TaskHistoryFilterModel){
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.actionBar?.title = this.resources.getString(R.string.title_toolbar_task_list)
        activity?.actionBar?.setDisplayShowCustomEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_black_24)
        activity!!.menuInflater.inflate(R.menu.menu_task_history, menu)
        filter = menu?.findItem(R.id.filter)
    }

    override fun onStart() {
        super.onStart()
        pull_refresh.setOnRefreshListener {
            viewModel.getDriverTasksHistory()
            pull_refresh.isRefreshing = false
        }
    }
}