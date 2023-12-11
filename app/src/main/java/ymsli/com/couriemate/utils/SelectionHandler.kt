package ymsli.com.couriemate.utils

import androidx.recyclerview.widget.RecyclerView
import ymsli.com.couriemate.adapters.TaskListAdapter
import ymsli.com.couriemate.adapters.TaskListViewHolder
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.utils.common.TaskStatus


/**
 * This class handles the multiple select functionality for the RV
 */
class SelectionHandler(private val adapter: TaskListAdapter){
    var selection = HashMap<Int, RecyclerView.ViewHolder>()
        private set
    var selectionMode = false
        private set
    var isActive = true
    var observer = object: Observer {
        override fun onSelectionModeOn() {}
        override fun onSelectionModeOff() {}
    }

    fun isSelected(position: Int) = selection.containsKey(position)

    fun clearSelection(){
        selectionMode = false
        selection.clear()
        adapter.notifyDataSetChanged()
        observer.onSelectionModeOff()
    }

    fun getSelectedTasks(): List<TaskRetrievalResponse>{
        var taskList = ArrayList<TaskRetrievalResponse>()
        for(holder in selection.values){
            taskList.add(adapter.dataList[holder.adapterPosition])
        }
        return taskList
    }

    fun onClick(viewHolder: TaskListViewHolder){
        var itemView = viewHolder.itemView
        if((selectionMode) && (isClickable(viewHolder))){
            var select = !itemView.isActivated
            if(select){
                itemView.isActivated = true
                selection[viewHolder.adapterPosition] = viewHolder
                viewHolder.bind(adapter.dataList[viewHolder.adapterPosition],true)
            }
            else{
                itemView.isActivated = false
                selection.remove(viewHolder.adapterPosition)
                viewHolder.bind(adapter.dataList[viewHolder.adapterPosition],false)
            }
            if(selection.isEmpty()) {
                selectionMode = false
                observer.onSelectionModeOff()
            }
        }
    }

    fun onLongClick(viewHolder: TaskListViewHolder):Boolean{
        if((isActive) && (!selectionMode) && (isClickable(viewHolder))){
            selectionMode = true
            viewHolder.itemView.isActivated = true
            selection[viewHolder.adapterPosition] = viewHolder
            viewHolder.bind(adapter.dataList[viewHolder.adapterPosition],true)
            observer.onSelectionModeOn()
        }
        return true
    }

    private fun isClickable(viewHolder: TaskListViewHolder): Boolean{
        var task = adapter.dataList[viewHolder.adapterPosition]
        if(task.taskNo!!.startsWith("T")){
            return ((((task.taskSequenceNo == 1) && (task.taskStatusId == TaskStatus.ASSIGNED.statusId))) ||
                    ((task.taskSequenceNo == 3) && (task.completed == 1) && (task.taskStatusId == TaskStatus.ASSIGNED.statusId)))
        }
        return ((task.taskSequenceNo==3) && (task.completed==1) && (task.taskStatusId== TaskStatus.ASSIGNED.statusId) )
    }

    interface Observer{
        fun onSelectionModeOn()
        fun onSelectionModeOff()
    }
}