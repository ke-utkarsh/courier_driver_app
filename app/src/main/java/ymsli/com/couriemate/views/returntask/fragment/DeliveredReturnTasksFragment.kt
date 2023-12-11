package ymsli.com.couriemate.views.returntask.fragment

import ymsli.com.couriemate.R
import ymsli.com.couriemate.views.returntask.filter.ReturnTaskFilterDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_tasks.*
import ymsli.com.couriemate.utils.common.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 6, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd. *
 * Description
 * -----------------------------------------------------------------------------------
 * DeliveredReturnTasksFragment : Subclass of BaseReturnTasksFragment which is used for the
 *                                Returned tab on Return tasks screen.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class DeliveredReturnTasksFragment : BaseReturnTasksFragment(TasksFragmentType.DONE) {

    /** Fields for filter module and its helpers */
    private var filterHelper = FilterHelper(FilterHelper.ComparisonMode.DONE)
    private var filterDialogState = FilterModel.getDefaultState()
    private var filterDialog = ReturnTaskFilterDialog()

    companion object {
        const val TAG = "DeliveredReturnTasksFragment"
        fun newInstance(): DeliveredReturnTasksFragment {
            val args = Bundle()
            val fragment =
                DeliveredReturnTasksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Setup the observers for the live data objects,
     */
    override fun setupObservers() {
        super.setupObservers()
        viewModel.getReturnTasks()
        viewModel.getAssignedReturnTasks().observe(this, Observer{
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


    /**************** Toolbar configuration and helper functions *****************/

    /**
     * When toolbar's options menu is being created,
     * if task list is in multiple selection mode then show the contextual toolbar menu,
     * otherwise let the BaseTaskFragment populate the default toolbar menu and then hide
     * the filter item.
     * because this fragment has no need for filter function.
     *
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        activity?.actionBar?.title = this.resources.getString(R.string.header_tasks_list)
        activity?.actionBar?.setDisplayShowCustomEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_black_24)
        activity!!.menuInflater.inflate(R.menu.return_task_menu, menu)
        filter = menu?.findItem(R.id.filter)
        setupSearchView(menu)
    }

    /* Methods to handle the search functionality */
    private fun setupSearchView(menu: Menu?) {
        val searchView = menu?.findItem(R.id.search_task_list_toolbar)!!.actionView as SearchView?
        searchView!!.setOnQueryTextListener(this)
        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                filter?.isVisible = true
            }

            override fun onViewAttachedToWindow(v: View?) {
                filter?.isVisible = false
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
     * When filter menu item is selected show the filter dialog.
     * @author Balraj VE00YM023
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.filter -> {
                filterDialog.show(activity?.supportFragmentManager, filterDialog.tag)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}