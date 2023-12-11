package ymsli.com.couriemate.views.returntask.fragment

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.views.returntask.list.ReturnTaskListViewModel
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_tabbed_task_list.*
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.views.tasklist.TaskListViewModel

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 6, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TabContainerReturnTaskList : A tab container for all the return task tab lists.
 *                              This container currently holds following tabs
 *                                   1. Delivered
 *                                   2. Returned
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TabContainerReturnTaskList : BaseFragment<TaskListViewModel>(){

    companion object {
        private const val TAB_HEADER_DELIVERED = "DELIVERED"
        private const val TAB_HEADER_RETURNED  = "RETURNED"
        const val TAG = "TabContainerReturnTaskList"
        fun newInstance(): TabContainerReturnTaskList {
            val args = Bundle()
            val fragment =
                TabContainerReturnTaskList()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_tabbed_return_task_list

    override fun injectDependencies(fragmentComponent: FragmentComponent)
            = fragmentComponent.inject(this)

    /**
     * Configure all the tabs of this container and then load the assigned tasks tab.
     * @author Balraj VE00YM023
     */
    override fun setupView(view: View) {
        configureTabs()
        loadDeliveredReturnTasksFragment()
    }

    /**
     * Configure all the tabs for this container by setting up the appropriate text
     * of every tab and then set up the tab selected listeners.
     * @author Balraj VE00YM023
     */
    private fun configureTabs() {
        tabs_task_list.addTab(tabs_task_list.newTab().setText(TAB_HEADER_DELIVERED))
        tabs_task_list.addTab(tabs_task_list.newTab().setText(TAB_HEADER_RETURNED))
        tabs_task_list.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.messageString.value = null
                viewModel.messageStringId.value = null
                when(tab?.text) {
                    TAB_HEADER_DELIVERED    -> loadDeliveredReturnTasksFragment()
                    TAB_HEADER_RETURNED     -> loadReturnedReturnTasksFragment()
                }
            }
        })
    }

    private fun loadDeliveredReturnTasksFragment(){
        viewModel.filterState.value = FilterModel.getDefaultState()
        childFragmentManager.popBackStack()
        childFragmentManager.findFragmentByTag(DeliveredReturnTasksFragment.TAG)?: childFragmentManager
            .beginTransaction()
            .replace(
                R.id.frame_tabbed_task_list,
                DeliveredReturnTasksFragment.newInstance(),
                DeliveredReturnTasksFragment.TAG)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun loadReturnedReturnTasksFragment(){
        viewModel.filterState.value = FilterModel.getDefaultState()
        childFragmentManager.popBackStack()
        childFragmentManager.findFragmentByTag(ReturnedTasksFragment.TAG)?: childFragmentManager
            .beginTransaction()
            .replace(
                R.id.frame_tabbed_task_list,
                ReturnedTasksFragment.newInstance(),
                ReturnedTasksFragment.TAG)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}