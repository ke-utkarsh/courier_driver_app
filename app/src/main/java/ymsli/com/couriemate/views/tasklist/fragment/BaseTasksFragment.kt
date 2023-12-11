package ymsli.com.couriemate.views.tasklist.fragment

import android.content.Intent
import android.net.Uri
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_tasklist.*
import kotlinx.android.synthetic.main.activity_tasks.*
import kotlinx.android.synthetic.main.activity_tasks.pull_refresh
import kotlinx.android.synthetic.main.activity_tasks.recycler_taskList
import kotlinx.android.synthetic.main.fragment_recycler_task_list.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.adapters.TaskListAdapter
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import ymsli.com.couriemate.views.tasklist.drag.SimpleItemTouchHelperCallback
import ymsli.com.couriemate.views.tasklist.swipe.SwipeHandler
import javax.inject.Inject


/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   DEC 19, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseTasksFragment : Base fragment for all the task list fragments (assigned, ongoing and done)
 * following common features are handled by this fragment.
 *
 *      * Setting up recycler view
 *      * Configuring the swipe refresh layout to provide pull to refresh function
 *      * Setting up the list item swipe handler
 *
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

open class BaseTasksFragment(private val taskFragmentType: TasksFragmentType)
    : BaseFragment<TaskListViewModel>(), SearchView.OnQueryTextListener{

    private var rearrangeTasks: MenuItem? = null
    protected var filter: MenuItem? = null
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    /** Fields for recycler view and its helper modules */
    @Inject lateinit var taskListAdapter: TaskListAdapter
    @Inject lateinit var linearLayoutManager: LinearLayoutManager
    protected lateinit var swipeHandler: SwipeHandler
    protected lateinit var tasksList: ArrayList<TaskRetrievalResponse>
    protected lateinit var filteredList: ArrayList<TaskRetrievalResponse>

    private lateinit var mItemTouchHelper: ItemTouchHelper

    /** Define the layout for this activity */
    override fun provideLayoutId(): Int = R.layout.fragment_recycler_task_list

    /** Prepare and inject dependencies for this activity */
    override fun injectDependencies(fragmentComponent: FragmentComponent)
            = fragmentComponent.inject(this)

    /**
     * This function sets up the basic functions of this fragment.
     * 1. setup the swipe refresh layout to provide pull to refresh feature
     * 2. setup recycler view to show the list
     * 3. setup the list item swipe handler
     *    to provide swipe to call and swipe to pickup features.
     *
     * @author Balraj VE00YM023
     */
    override fun setupView(view: View) {
        setHasOptionsMenu(true)
        toggle = ActionBarDrawerToggle(activity, drawer_layout,action_bar_top,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
    }

    override fun onStart() {
        super.onStart()
        setupSwipeRefreshLayout()
        setupRecyclerView()
        setupListItemSwipeHandler()
    }

    /**
     * Setup the recycler view by configuring the adapter and layout manager for
     * recycler view.
     *
     * If this is assigned tasks fragment then also add the drag drop feature to the
     * items of list.
     * @author Balraj VE00YM023
     */
    private fun setupRecyclerView(){
        recycler_taskList.adapter = taskListAdapter
        taskListAdapter.setEmptyNotificationMode(activity!!.findViewById(R.id.text_no_data_task_list))
        recycler_taskList.layoutManager = linearLayoutManager

        if(taskFragmentType == TasksFragmentType.ASSIGNED){
            val callback = SimpleItemTouchHelperCallback(taskListAdapter)
            mItemTouchHelper = ItemTouchHelper(callback)
        }
    }

    /**
     * Used to enable and disable the drag drop feature of recycler list item.
     * @author Balraj VE00YM023
     */
    protected fun enableDrag(enable: Boolean){
        if(enable){
            mItemTouchHelper.attachToRecyclerView(recycler_taskList)
            swipeRefreshLayout.isEnabled = false
        }
        else {
            mItemTouchHelper.attachToRecyclerView(null)
            swipeRefreshLayout.isEnabled = true
        }
    }

    /**
     * Configures the swipe handler for the swipe to pickup and swipe to call actions on
     * list items.
     *
     * ACTIONS:
     * LEFT SWIPE:   call the receiver of item
     * RIGHT SWIPE:  (only enabled if task fragment type is assigned)
     *               pickup this item, modifies state of this task to delivering
     *
     * @author Balraj VE00YM023
     */
    private fun setupListItemSwipeHandler(){
        swipeHandler = SwipeHandler(activity!!.baseContext, recycler_taskList,taskListAdapter)
        swipeHandler.leftSwipeAction = object : SwipeHandler.SwipeAction {
            override fun perform(task: TaskRetrievalResponse) {
                if(taskFragmentType == TasksFragmentType.ASSIGNED) pickupReturnTask(task)
            }
        }
        swipeHandler.rightSwipeAction = object : SwipeHandler.SwipeAction {
            override fun perform(task: TaskRetrievalResponse) {
                makePhoneCall(task)
            }
        }
    }

    /**
     * Setup the swipe refresh layout to provide the pull to refresh feature.
     * @author Balraj VE00YM023
     */
    private fun setupSwipeRefreshLayout(){
        swipeRefreshLayout = pull_refresh
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshDriverTasks()
            /* Sync notifications only for ONGOING fragment */
            if(taskFragmentType == TasksFragmentType.ONGOING){ viewModel.syncNotifications() }
        }
    }

    /***************** Swipe handler helper functions ****************************/

    /** Called on left swipe */
    private fun makePhoneCall(task: TaskRetrievalResponse) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${task.receiverMobile}")
        startActivity(intent)
        taskListAdapter.loadData(this.filteredList)
    }

    /** Called on right swipe */
    private fun pickupReturnTask(task: TaskRetrievalResponse) {
        viewModel.performPickup(listOf(task))
    }

    /**************** Toolbar configuration and helper functions *****************/
    override fun onCreateOptionsMenu(menu: Menu?,inflater: MenuInflater) {
        setupToolbar(menu)
    }

    /**
     * Setup the toolbar when option menu is loaded
     * if this is not assigned tasks fragment then hide following menu items.
     *      1. sync
     *      2. drag drop
     * @author Balraj VE00YM023
     */
    private fun setupToolbar(menu: Menu?) {
        activity?.actionBar?.title = this.resources.getString(R.string.title_toolbar_task_list)
        activity?.actionBar?.setDisplayShowCustomEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_black_24)
        activity!!.menuInflater.inflate(R.menu.toolbar_actions, menu)
        rearrangeTasks = menu?.findItem(R.id.drag_drop_mode_task_list)
        filter = menu?.findItem(R.id.filter)
        if(taskFragmentType != TasksFragmentType.ASSIGNED){
            menu?.findItem(R.id.drag_drop_mode_task_list)?.isVisible = false
        }
        setupSearchView(menu)
    }


    var searchView: SearchView? = null
    /***************** Search view configuration functions *********************/

    /**
     * Setup the search view to provide the search feature in toolbar.
     * @author Balraj VE00YM023
     */
    private fun setupSearchView(menu: Menu?) {
        searchView = menu?.findItem(R.id.search_task_list_toolbar)!!.actionView as SearchView?
        searchView!!.setOnQueryTextListener(this)
        searchView!!.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                if(taskFragmentType == TasksFragmentType.ASSIGNED){
                    rearrangeTasks?.isVisible = true
                }
                swipeRefreshLayout.isEnabled = true
                filter?.isVisible = true
            }

            override fun onViewAttachedToWindow(v: View?) {
                if(taskFragmentType == TasksFragmentType.ASSIGNED){
                    rearrangeTasks?.isVisible = false
                }
                swipeRefreshLayout.isEnabled = false
                filter?.isVisible = false
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean {
        if(filteredList.size>0) applySearchFilter(newText)
        return true
    }

    /**
     * Apply the search filter by filtering the data list based on the search criteria
     * and then loading the filtered list in the recycler view.
     * @author Balraj VE00YM023
     */
    private fun applySearchFilter(newText: String?) {
        if (!newText.isNullOrEmpty()) {
            val filteredTasks = filteredList.filter { it.orderNo!!.toString().contains(newText) }
            taskListAdapter.loadData(ArrayList(filteredTasks))
        }
        else {
            taskListAdapter.loadData(this.filteredList)
        }
    }

    /** Following methods can be used to enable/disable view until the
     *  List is completely populated.
     *
     *  @author Balraj VE00YM023
     */
    protected fun enableUserInteraction(){
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progress_bar_task_list.visibility = View.GONE
    }

    protected fun disableUserInteraction(){
        progress_bar_task_list.visibility = View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    protected fun enablePullToRefresh(enableState: Boolean) {
        swipeRefreshLayout.isEnabled = enableState
    }

    enum class TasksFragmentType{ ASSIGNED, ONGOING, DONE,HISTORY }

}