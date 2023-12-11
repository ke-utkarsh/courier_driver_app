package ymsli.com.couriemate.views.returntask.fragment


import ymsli.com.couriemate.R
import ymsli.com.couriemate.adapters.TaskListAdapter
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import ymsli.com.couriemate.views.tasklist.drag.SimpleItemTouchHelperCallback
import ymsli.com.couriemate.views.tasklist.swipe.SwipeHandler
import android.content.Intent
import android.net.Uri
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_tasklist.*
import kotlinx.android.synthetic.main.activity_tasks.*
import kotlinx.android.synthetic.main.activity_tasks.pull_refresh
import kotlinx.android.synthetic.main.activity_tasks.recycler_taskList
import kotlinx.android.synthetic.main.fragment_recycler_task_list.*
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 6, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseReturnTasksFragment : Base fragment for all the return task list fragments
 *                          (delivered, returned).
 *  following common features are handled by this fragment.
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
abstract class BaseReturnTasksFragment(private val taskFragmentType: TasksFragmentType)
    : BaseFragment<TaskListViewModel>(), SearchView.OnQueryTextListener{

    protected var filter: MenuItem? = null
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    /** Fields for recycler view and its helper modules */
    @Inject lateinit var taskListAdapter: TaskListAdapter
    @Inject lateinit var linearLayoutManager: LinearLayoutManager
    protected lateinit var swipeHandler: SwipeHandler
    protected lateinit var tasksList: ArrayList<TaskRetrievalResponse>
    protected lateinit var filteredList: ArrayList<TaskRetrievalResponse>

    override fun provideLayoutId(): Int = R.layout.fragment_recycler_task_list

    override fun injectDependencies(fragmentComponent: FragmentComponent)
            = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        setHasOptionsMenu(true)
        toggle = ActionBarDrawerToggle(activity, drawer_layout,action_bar_top,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        setupSwipeRefreshLayout()
        setupRecyclerView(view)
        setupListItemSwipeHandler()
    }

    private fun setupRecyclerView(layout: View){
        recycler_taskList.adapter = taskListAdapter
        taskListAdapter.setEmptyNotificationMode(layout.findViewById(R.id.text_no_data_task_list))
        recycler_taskList.layoutManager = linearLayoutManager

        if(taskFragmentType == TasksFragmentType.ASSIGNED){
            swipeRefreshLayout.isEnabled = false
            val callback = SimpleItemTouchHelperCallback(taskListAdapter)
            val mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper.attachToRecyclerView(recycler_taskList)
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

    private fun setupSwipeRefreshLayout(){
        swipeRefreshLayout = pull_refresh
        swipeRefreshLayout.setOnRefreshListener {viewModel.refreshDriverTasks()}
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
        /** Don't show task drag drop menu item, in return task screens */
        menu?.findItem(R.id.drag_drop_mode_task_list)?.isVisible = false
    }

    private fun setupToolbar(menu: Menu?) {
        activity?.actionBar?.title = this.resources.getString(R.string.title_toolbar_task_list)
        activity?.actionBar?.setDisplayShowCustomEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_black_24)
        activity!!.menuInflater.inflate(R.menu.toolbar_actions, menu)
        filter = menu?.findItem(R.id.filter)
        setupSearchView(menu)
    }


    /***************** Search view configuration functions *********************/

    /** When search view is visible hide the filter item from toolbar menu */
    private fun setupSearchView(menu: Menu?) {
        val searchView = menu?.findItem(R.id.search_task_list_toolbar)!!.actionView as SearchView?
        searchView!!.setOnQueryTextListener(this)
        searchView!!.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                if(taskFragmentType != TasksFragmentType.ASSIGNED){
                    swipeRefreshLayout.isEnabled = true
                    filter?.isVisible = true
                }
            }

            override fun onViewAttachedToWindow(v: View?) {
                if(taskFragmentType == TasksFragmentType.ASSIGNED){
                    swipeRefreshLayout.isEnabled = false
                    filter?.isVisible = false
                }
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean {
        applySearchFilter(newText)
        return true
    }

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

    enum class TasksFragmentType{ ASSIGNED, ONGOING, DONE }

}