package ymsli.com.couriemate.views.tasklist.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_tasks.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.utils.SelectionHandler
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.FilterHelper
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.views.main.MainActivity
import ymsli.com.couriemate.views.tasklist.filter.AssignedTasksFilterDialog


/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   DEC 20, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * AssignedTasksFragment : Subclass of BaseTasksFragment which adds additional features
 *                         required by the assigned tasks list.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class AssignedTasksFragment : BaseTasksFragment(TasksFragmentType.ASSIGNED){

    /** variable to keep track of drag drop mode */
    private var dragDropMode = false
    private lateinit var selectionHandler: SelectionHandler

    /** Fields for filter module and its helpers */
    private var filterHelper = FilterHelper(FilterHelper.ComparisonMode.ASSIGNED)
    private var filterDialog = AssignedTasksFilterDialog()
    private var filterDialogState = FilterModel.getDefaultState()

    /** This pickup location is used to filter the tasks */
    private lateinit var couriematePickupLocation: String

    override fun setupView(view: View) {
        super.setupView(view)
        setupMultipleSelectionHandler()
        couriematePickupLocation = viewModel.getCompanyAddress() ?: Constants.EMPTY_STRING
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.errorDataRefreshed.observe(this, Observer {
            pull_refresh.isRefreshing = false
        })

        viewModel.sortedAssignedTasks.observe(this, Observer {
            disableUserInteraction()
            this.tasksList = ArrayList(it)
            this.filteredList = if(filterDialogState.isSet()) {
                filterHelper.filterByPickupAddress(couriematePickupLocation, tasksList)
            }
            else{
                tasksList
            }
            taskListAdapter.clearData()
            taskListAdapter.loadData(filteredList)
            enableUserInteraction()
        })

        /** observe the today's assigned tasks */
        viewModel.getAssignedTasksForToday().observe(this, Observer {
            viewModel.assignedListSorter(it)
        })

        viewModel.filterApplied.observe(this, Observer {
            if((it != null) && (filterDialog.isVisible)){
                filter(it)
            }
        })

    }

    /**
     * When fragment stops and either multiple selection mode is active
     * or task rearrange feature is active then we invalidate options menu
     * so that other fragments get correct toolbar options.
     *
     * @author Balraj VE00YM023
     */
    override fun onStop() {
        super.onStop()
        if(selectionHandler.selectionMode || dragDropMode){
            activity?.invalidateOptionsMenu()
            (activity as MainActivity).setupToolbarForInteractiveModeOff()
        }
    }

    /**
     * Configures the call backs for multiple selection mode on recycler view.
     * when multiple selection mode is on, toolbar's menu is updated to show a single icon which
     * is used to perform pickup of multiple items and item swipe function is disabled.
     *
     * when multiple selection is off, toolbar's menu is restored to the default state and
     * item swipe function is enabled.
     *
     * @author Balraj VE00YM023
     */
    private fun setupMultipleSelectionHandler(){
        selectionHandler = taskListAdapter.selectionHandler
        selectionHandler.observer = object : SelectionHandler.Observer {
            override fun onSelectionModeOn() {
                activity?.invalidateOptionsMenu()
                (activity as MainActivity).setupToolbarForInteractiveModeOn(Constants.EMPTY_STRING)
                swipeHandler.disableSwipe()
                enablePullToRefresh(false)
            }

            override fun onSelectionModeOff() {
                activity?.invalidateOptionsMenu()
                (activity as MainActivity).setupToolbarForInteractiveModeOff()
                swipeHandler.enableSwipe()
                enablePullToRefresh(true)
            }
        }
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
    override fun onCreateOptionsMenu(menu: Menu?,inflater: MenuInflater) {
        when {
            selectionHandler.selectionMode -> setupContextualToolbar(menu)
            dragDropMode -> setupDragDropToolbar(menu)
            else -> {
                super.onCreateOptionsMenu(menu, inflater)
                setFilterIcon(filterDialogState)
            }
        }
    }

    /**
     * Handle the toolbar item selection.
     * @author Balraj VE00YM023
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        /** If back is pressed and any interactive mode (multiple selection, drag drop)
         *  is on, then turn it off */
        if(item?.itemId == android.R.id.home){
            when{
                selectionHandler.selectionMode -> selectionHandler.clearSelection()
                dragDropMode -> disableDragDropDownMode()
            }
        }
        /** Turn on drag drop mode */
        else if(item?.itemId == R.id.drag_drop_mode_task_list){
            enableDragDropDownMode()
        }
        /** Perform multiple pickup */
        else if ((selectionHandler.selectionMode) && (item?.itemId == R.id.task_list_menu_action_pickup)) {
            viewModel.performPickup(selectionHandler.getSelectedTasks())
            selectionHandler.clearSelection()
        }
        else if (item?.itemId == R.id.filter){
            /** If filter option item is selected but we don't have any filter address,
             *  then show a toast with appropriate message. */
            if(!::couriematePickupLocation.isInitialized ||
                  couriematePickupLocation == Constants.EMPTY_STRING){
                Toast.makeText(context, getString(R.string.no_filter_address), Toast.LENGTH_LONG).show()
            }
            else{
                filterDialog.show(activity?.supportFragmentManager, filterDialog.tag)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** When selection mode is on show the contextual tool bar menu */
    private fun setupContextualToolbar(menu: Menu?) {
        activity?.actionBar?.title = ""
        activity?.actionBar?.title = Constants.EMPTY_STRING
        activity?.actionBar?.setHomeButtonEnabled(false)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
        activity!!.menuInflater.inflate(R.menu.context_bar_menu, menu)
    }

    companion object {
        const val TAG = "AssignedTasksFragment"
        fun newInstance(): AssignedTasksFragment {
            val args = Bundle()
            val fragment =
                AssignedTasksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Handle the back button.
     * if multiple selection is on then turn it off on back press
     * if drag drop mode is on then turn it off on back press
     * other wise do nothing and return false
     *
     * @return true if this back press event is observed by this handler false otherwise
     * @author Balraj VE00YM023
     */
    fun onBackPressed(): Boolean{
        /** If multi selection is on then turn it off */
        if(selectionHandler.selectionMode){
            selectionHandler.clearSelection()
            return true
        }
        /** if drag drop down mode is on then turn it off */
        else if(dragDropMode){
            dragDropMode = false
            disableDragDropDownMode()
            return true
        }
        return false
    }

    private fun setupDragDropToolbar(menu: Menu?){
        activity!!.menuInflater.inflate(R.menu.drag_drop_toolbar_menu, menu)
    }

    /**
     * Enables the drag drop mode,
     * by disabling the multiple selection feature and configuring
     * the toolbar for drag drop mode.
     * @author Balraj VE00YM023
     */
    private fun enableDragDropDownMode(){
        dragDropMode = true
        selectionHandler.isActive = false
        enableDrag(true)
        activity!!.invalidateOptionsMenu()
        (activity as MainActivity).setupToolbarForInteractiveModeOn(Constants.REARRANGE_MODE_TITLE)
    }

    /**
     * Disables the drop drop mode by enabling the multi selection
     * mode and configuring the toolbar for normal operation mode.
     * @author Balraj VE00YM023
     */
    private fun disableDragDropDownMode(){
        dragDropMode = false
        selectionHandler.isActive = true
        enableDrag(false)
        activity!!.invalidateOptionsMenu()
        (activity as MainActivity).setupToolbarForInteractiveModeOff()
    }

    /**************** Filter module helper functions ************************/
    fun filter(predicate: FilterModel) {
        viewModel.filterState.value = predicate
        this.filterDialogState = predicate
        filterDialog.dismiss()
        setFilterIcon(predicate)
        filteredList = if(predicate.isSet()) {
            filterHelper.filterByPickupAddress(couriematePickupLocation, tasksList)
        }
        else{
            tasksList
        }
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
}
