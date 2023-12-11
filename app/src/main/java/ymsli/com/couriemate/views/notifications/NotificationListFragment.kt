package ymsli.com.couriemate.views.notifications

import ymsli.com.couriemate.R
import ymsli.com.couriemate.adapters.NotificationListAdapter
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_tasklist.*
import kotlinx.android.synthetic.main.activity_tasks.pull_refresh
import kotlinx.android.synthetic.main.fragment_notification_list.*
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 7, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * NotificationListFragment : This fragment contains a recycler view to show the list
 *                            of notifications.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class NotificationListFragment : BaseFragment<NotificationListViewModel>(){

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var notificationListAdapter: NotificationListAdapter

    private lateinit var toggle: ActionBarDrawerToggle

    /** This field is used to highlight the active notifications in list */
    private var activeNotificationCount = 0

    /**
     * When view is created extract the active notification count from arguments.
     * if no value is received then set activeNotificationCount to 0
     * @author Balraj VE00YM023
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activeNotificationCount = arguments?.getInt(ACTIVE_NOTIFICATIONS_COUNT)?:0
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun provideLayoutId(): Int = R.layout.fragment_notification_list

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    /**
     * Configure the swipe refresh layout and recycler view and then
     * retrieve the latest notifications data from the remote server.
     * @author Balraj VE00YM023
     */
    override fun setupView(view: View) {
        setHasOptionsMenu(true)
        toggle = ActionBarDrawerToggle(activity, drawer_layout, action_bar_top_notification_list,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        setupSwipeRefreshLayout()
        setupRecyclerView(view)
        viewModel.getNotificationsFromServer()
    }

    override fun setupObservers() {
        super.setupObservers()

        /** Bind the notification data on recycler view */
        viewModel.notifications.observe(this, Observer{
            notificationListAdapter.clearData()
            notificationListAdapter.loadData(ArrayList(it.asList()), activeNotificationCount)
        })

        /** If API sync failed due to no internet or some error, then don't highlight
         *  any notifications, as the data is not up to date. */
        viewModel.apiSynced.observe(this, Observer { apiSynced ->
            if(apiSynced){
                viewModel.clearActiveNotificationCount()
            }
            else{
                activeNotificationCount = 0
            }
        })
    }

    /**
     * Configure the swipe refresh layout, currently we don't provide pull
     * to refresh feature.
     * @author Balraj VE00YM023
     */
    private fun setupSwipeRefreshLayout(){
        swipeRefreshLayout = pull_refresh
        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.setOnRefreshListener {}
    }

    /**
     * Set up the recycler View by configuring the adapter and layout manager.
     * @author Balraj VE00YM023
     */
    private fun setupRecyclerView(layout: View){
        recycler_notification_list.adapter = notificationListAdapter
        var noDataView = layout.findViewById<TextView>(R.id.text_no_data_notification_list)
        notificationListAdapter.setEmptyNotificationMode(noDataView)
        recycler_notification_list.layoutManager = linearLayoutManager
    }

    /**
     * As tool bar is associated with the Main Activity, we have to hide
     * some toolbar menu items that are not reagent to us in this fragment.
     * @author Balraj VE00YM023
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.findItem(R.id.search_task_list_toolbar)?.isVisible = false
    }

    companion object {
        const val TAG = "NotificationsFragment"
        const val ACTIVE_NOTIFICATIONS_COUNT = "activeNotificationsCount"
        fun newInstance(): NotificationListFragment {
            val args = Bundle()
            val fragment =
                NotificationListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}