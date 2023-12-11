package ymsli.com.couriemate.adapters

import ymsli.com.couriemate.base.BaseAdapter
import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle

class TaskHistoryAdapter (parentLifeCycle : Lifecycle,
                          tasksLists:ArrayList<TaskHistoryResponse>)
    : BaseAdapter<TaskHistoryResponse, TaskHistoryViewHolder>(parentLifeCycle,tasksLists) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHistoryViewHolder = TaskHistoryViewHolder(parent)
}