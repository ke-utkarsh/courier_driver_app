package ymsli.com.couriemate.views.tasklist.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_payment_registration.*
import kotlinx.android.synthetic.main.fragment_tabbed_task_list.*
import ymsli.com.couriemate.utils.common.Event

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Dec 20, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TabbedTaskListContainerFragment : A tab container for all the current task tab lists.
 *                                   This container currently holds following tabs
 *                                   1. Assigned
 *                                   2. Ongoing
 *                                   3. Done
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TabbedTaskListContainerFragment : BaseFragment<TaskListViewModel>(){

    /** Define layout for this container */
    override fun provideLayoutId(): Int = R.layout.fragment_tabbed_task_list

    /** Prepare and inject dependencies for this container */
    override fun injectDependencies(fragmentComponent: FragmentComponent)
            = fragmentComponent.inject(this)

    /**
     * Configure all the tabs of this container and then load the assigned tasks tab.
     * @author Balraj VE00YM023
     */
    override fun setupView(view: View) {
        configureTabs()
        loadAssignedTasksFragment()
        startTrip()
    }

    private fun startTrip(){
        if(!viewModel.getTripStatusInSharedPrefs()){
            viewModel.setTripStatusInSharedPrefs(true)
            viewModel.startTrip.postValue(Event(true))
        }
    }

    /**
     * Configure all the tabs for this container by setting up the appropriate text
     * of every tab and then set up the tab selected listeners.
     * @author Balraj VE00YM023
     */
    private fun configureTabs() {
        tabs_task_list.addTab(tabs_task_list.newTab().setText(TAB_HEADER_ASSIGNED))
        tabs_task_list.addTab(tabs_task_list.newTab().setText(TAB_HEADER_ONGOING))
        tabs_task_list.addTab(tabs_task_list.newTab().setText(TAB_HEADER_DONE))

        tabs_task_list.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.messageString.value = null
                viewModel.messageStringId.value = null
                when(tab?.text) {
                    TAB_HEADER_ASSIGNED -> loadAssignedTasksFragment()
                    TAB_HEADER_ONGOING  -> loadOngoingTasksFragment()
                    TAB_HEADER_DONE     -> loadDoneTasksFragment()
                }
            }
        })
    }

    private fun loadAssignedTasksFragment(){
        childFragmentManager.popBackStack()
        childFragmentManager.findFragmentByTag(AssignedTasksFragment.TAG)?: childFragmentManager
            .beginTransaction()
            .replace(R.id.frame_tabbed_task_list,
                AssignedTasksFragment.newInstance(),
                AssignedTasksFragment.TAG)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun loadOngoingTasksFragment(){
        viewModel.filterState.value = FilterModel.getDefaultState()
        childFragmentManager.popBackStack()
        childFragmentManager.findFragmentByTag(OngoingTasksFragment.TAG)?: childFragmentManager
            .beginTransaction()
            .replace(R.id.frame_tabbed_task_list,
                OngoingTasksFragment.newInstance(),
                OngoingTasksFragment.TAG)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun loadDoneTasksFragment(){
        viewModel.filterState.value = FilterModel.getDefaultState()
        childFragmentManager.popBackStack()
        childFragmentManager.findFragmentByTag(DoneTasksFragment.TAG)?: childFragmentManager
            .beginTransaction()
            .replace(R.id.frame_tabbed_task_list,
                DoneTasksFragment.newInstance(),
                DoneTasksFragment.TAG)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    companion object {
        private const val TAB_HEADER_ASSIGNED = "ASSIGNED"
        private const val TAB_HEADER_ONGOING  = "ONGOING"
        private const val TAB_HEADER_DONE = "DONE"
        const val TAG = "TabbedTaskListContainerFragment"
        fun newInstance(): TabbedTaskListContainerFragment {
            val args = Bundle()
            val fragment =
                TabbedTaskListContainerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}