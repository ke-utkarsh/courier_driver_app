package ymsli.com.couriemate.adapters

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseItemViewHolder
import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import ymsli.com.couriemate.di.component.ViewHolderComponent
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.views.taskhistory.TaskHistoryListItemViewModel
import ymsli.com.couriemate.utils.common.TaskStatus
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.task_history_recycler_view_card_item.view.*

class TaskHistoryViewHolder(parent: ViewGroup):
    BaseItemViewHolder<TaskHistoryResponse, TaskHistoryListItemViewModel>(R.layout.task_history_recycler_view_card_item,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) = viewHolderComponent.inject(this)

    override fun setupView(view: View) {

    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.taskId.observe(this, Observer {
            itemView.text_task_list_order_no.text = it.toString()
        })

        viewModel.taskStatusId.observe(this, Observer {
            val taskStatusEnum = TaskStatus.getEnumById(it!!)
            itemView.task_list_text_taskStatus.text = (taskStatusEnum?.name)
            itemView.task_list_text_taskStatus.setTextColor(Color.parseColor(taskStatusEnum?.color))
            itemView.task_list_circle.setBackgroundColor(Color.parseColor(taskStatusEnum?.color))
        })

        viewModel.deliveryLocation.observe(this, Observer {
            itemView.task_list_text_address.text = it
        })

        viewModel.endDate.observe(this, Observer {
            itemView.task_list_text_expectedDate.text = Utils.formatDate(it)
        })
    }
}