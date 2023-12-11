package ymsli.com.couriemate.adapters

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseItemViewHolder
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.di.component.ViewHolderComponent
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.views.tasklist.TaskListItemViewModel
import ymsli.com.couriemate.utils.common.TaskStatus
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.recycler_view_card_item.view.*


/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Oct 15, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TaskListViewHolder : View Holder for the task list fragment's recycler view.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TaskListViewHolder(parent:ViewGroup):
    BaseItemViewHolder<TaskRetrievalResponse, TaskListItemViewModel>(R.layout.recycler_view_card_item,parent)
{

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {}

    /**
     * Setup observers for all the fields required by the UI.
     */
    override fun setupObservers() {
        super.setupObservers()
        viewModel.orderNo.observe(this, Observer {
            itemView.text_task_list_order_no.text = it.toString()
        })

        viewModel.address.observe(this, Observer {
            itemView.task_list_text_address.text = it
        })

        viewModel.taskStatusId.observe(this, Observer {
            var taskStatusEnum = TaskStatus.getEnumById(it!!)
            itemView.task_list_text_taskStatus.text = (taskStatusEnum?.name)
            itemView.task_list_text_taskStatus.setTextColor(Color.parseColor(taskStatusEnum?.color))
            itemView.task_list_circle.setBackgroundColor(Color.parseColor(taskStatusEnum?.color))
            setDateByTaskStatusId(taskStatusEnum!!, viewModel.data.value!!)
            viewModel.data.value?.let { data ->
                setAddressByTaskStatusId(taskStatusEnum, data)
            }
        })

        viewModel.updatedOn.observe(this, Observer{
            itemView.task_list_text_updatedOn.text = Utils.formatDateForListItem(it)
        })
    }

    /**
     * Bind the recycler item, if this item is activated by the multi selection mode
     * then set the background of this item to grey.
     * @author Balraj VE00YM023
     */
    fun bind(data: TaskRetrievalResponse, isActivated: Boolean) {
        super.bind(data)
        initializeRecyclerView(data)
        itemView.isActivated = isActivated
        (itemView as CardView).setCardBackgroundColor(
            if (isActivated) Color.parseColor("#CCCCCC") else Color.parseColor("#FFFFFF"))
    }

    /**
     * Load the data in recycler view on the initialization.
     */
    private fun initializeRecyclerView(data: TaskRetrievalResponse){
        var taskStatus = TaskStatus.getEnumById(data.taskStatusId!!)
        itemView.text_task_list_order_no.text = data.orderNo
        itemView.task_list_text_updatedOn.text = if(data.updatedOn == null) {
            Utils.formatDateForListItem(data.createdOn!!)
        } else {
            Utils.formatDateForListItem(data.updatedOn!!)
        }
        itemView.task_list_text_taskStatus.text = taskStatus!!.name
        itemView.task_list_text_taskStatus.setTextColor(Color.parseColor(TaskStatus.getEnumById(data.taskStatusId!!)?.color))
        itemView.task_list_circle.setBackgroundColor(Color.parseColor(TaskStatus.getEnumById(data.taskStatusId!!)?.color))
        setDateByTaskStatusId(taskStatus, data)
        setAddressByTaskStatusId(taskStatus, data)
    }

    /**
     * Set the value and label of date field shown on the list item card.
     * assigned task -> show assignaed date
     * delivering or failed task -> show start date
     * delivered or returned task -> show end date
     *
     * @author Balraj VE00YM023
     */
    private fun setDateByTaskStatusId(taskStatus: TaskStatus, data: TaskRetrievalResponse){
        when(taskStatus){
            TaskStatus.ASSIGNED -> {
                itemView.task_list_text_label_expectedDate.text = "Assigned Date"
                itemView.task_list_text_expectedDate.text = Utils.formatDateForListItem(data.assignedDate!!)
            }
            TaskStatus.DELIVERING, TaskStatus.FAILED ->{
                itemView.task_list_text_label_expectedDate.text = "Start Date"
                itemView.task_list_text_expectedDate.text = Utils.formatDateForListItem(data.startDate!!)
            }
            TaskStatus.DELIVERED, TaskStatus.REFUSED, TaskStatus.RETURNED-> {
                itemView.task_list_text_label_expectedDate.text = "End Date"
                itemView.task_list_text_expectedDate.text = Utils.formatDateForListItem(data.endDate!!)
            }
        }
    }

    /**
     * Sets the value of Pickup/Drop address and district fields.
     * If task status is 'ASSIGNED' and task no is in -> 'T1', 'T3' or 'R-T3'
     * then pickup address/district is displayed on the list item
     * otherwise drop address/district is displayed on the list item.
     *
     * @author Balraj VE00YM023
     */
    private fun setAddressByTaskStatusId(taskStatus: TaskStatus, data: TaskRetrievalResponse){
        when{
            taskStatus == TaskStatus.ASSIGNED &&
            (data.taskNo == "T1" || data.taskNo == "T3" || data.taskNo == "R-T3")-> {
                itemView.lable_list_item_address.text = "Pickup"
                itemView.task_list_text_address.text = data.pickupLocation
                itemView.tv_list_item_district.text = data.pickupDistrict
            }
            else -> {
                itemView.lable_list_item_address.text = "Drop"
                itemView.task_list_text_address.text = data.dropAddress
                itemView.tv_list_item_district.text = data.dropDistrict
            }
        }
    }
}