package ymsli.com.couriemate.adapters

import ymsli.com.couriemate.base.BaseAdapter
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.utils.SelectionHandler
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.views.returntask.detail.ReturnTaskDetailActivity
import ymsli.com.couriemate.views.taskdetail.TaskDetailActivity
import ymsli.com.couriemate.views.tasklist.drag.ItemTouchHelperAdapter
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Oct 15, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TaskListAdapter : Recycler Adapter for all the task fragments
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TaskListAdapter(parentLifeCycle : Lifecycle,
                      tasksLists:ArrayList<TaskRetrievalResponse>)
    :BaseAdapter<TaskRetrievalResponse, TaskListViewHolder>(parentLifeCycle,tasksLists),
    ItemTouchHelperAdapter{

    private var recyclerView: RecyclerView? = null
    var selectionHandler = SelectionHandler(this)

    /**
     * fields to support the empty notification mode,
     * when this mode is on and recycler view has no data
     * then a no data view is displayed to the user.
     *
     * @author Balraj VE00YM023
     */
    private var emptyNotificationMode = false
    private var noTasksAvailableView: TextView? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder =
        TaskListViewHolder(parent)

    /**
     * Enables the empty notification mode.
     * if data list to be loaded is empty and this mode is enabled then
     * the notification view (passed as param to this method) will be shown to the user.
     *
     * @param view: this view will be shown to the user whenever the data list of adapter is empty
     * @author Balraj VE00YM023
     */
    fun setEmptyNotificationMode(view: TextView){
        emptyNotificationMode = true
        noTasksAvailableView = view
    }

    /**
     * Resets the empty notification mode.
     * if empty notification mode is reset then no notification will be shown to user
     * if data list of adapter becomes empty.
     *
     * @author Balraj VE00YM023
     */
    fun resetEmptyNotificationMode(){
        emptyNotificationMode = false
        noTasksAvailableView = null
    }

    /**
     * Calls the load data of parent and then shows the empty notification view
     * if empty notification mode is on and the data list of adapter is empty.
     *
     * @param data: data to be loaded in recycler adapter
     * @author Balraj VE00YM023
     */
    override fun loadData(data: ArrayList<TaskRetrievalResponse>) {
        super.loadData(data)
        if(emptyNotificationMode) setVisibilityBasedOnDataSize(data.isEmpty())
    }

    /**
     * Calls the load data of parent and shows the empty notification view
     * if the empty notification mode is on.
     *
     * @author Balraj VE00YM023
     */
    override fun clearData() {
        super.clearData()
        if(emptyNotificationMode) showEmptyNotification()
    }

    /**
     * Depending on the size of data list of adapter shows the appropriate view.
     * (recycler view or no data available view)
     *
     * @param isEmpty
     * @author Balraj VE00YM023
     */
    private fun setVisibilityBasedOnDataSize(isEmpty: Boolean) {
        if (isEmpty) {
            showEmptyNotification()
        } else {
            showRecyclerView()
            notifyDataSetChanged()
        }
    }

    /**
     * Hides the recycler view and shows the no tasks available view.
     * @author Balraj VE00YM023
     */
    private fun showEmptyNotification() {
        recyclerView?.visibility = View.GONE
        noTasksAvailableView?.visibility = View.VISIBLE
    }

    /**
     * Shows the recycler view and hides the no tasks available view.
     * @author Balraj VE00YM023
     */
    private fun showRecyclerView() {
        recyclerView?.visibility = View.VISIBLE
        noTasksAvailableView?.visibility = View.GONE
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        setListeners(dataList[position], holder)
        holder.bind(dataList[position], selectionHandler.isSelected(position))
    }

    override fun onViewAttachedToWindow(holder: TaskListViewHolder) {
        if(holder.itemView.isActivated != selectionHandler.isSelected(holder.adapterPosition)){
            holder.bind(dataList[holder.adapterPosition], selectionHandler.isSelected(holder.adapterPosition))
        }
    }

    /**
     * Set the list item click listeners.
     * If multi selection mode is on then simply add clicked item to selected items.
     * otherwise move the user to appropriate next screen( TaskDetailActivity, ReturnTaskDetailActivity)
     *
     * @author Balraj VE00YM023
     */
    private fun setListeners(task: TaskRetrievalResponse, viewHolder: TaskListViewHolder){
        viewHolder.itemView.setOnClickListener {
            if(selectionHandler.selectionMode){
                selectionHandler.onClick(viewHolder)
            } else{
                val intent = if(task.taskNo!!.startsWith("T")) {
                    Intent(viewHolder.itemView.context, TaskDetailActivity::class.java)
                } else {
                    Intent(viewHolder.itemView.context, ReturnTaskDetailActivity::class.java)
                }
                intent.putExtra("SelectedTask",task)
                Log.d("dineshTAG", "setListeners: "+task)
                viewHolder.itemView.context.startActivity(intent)
            }
        }
        viewHolder.itemView.setOnLongClickListener{selectionHandler.onLongClick(viewHolder)}
    }

    /**
     * This function is used to support the drag and drop feature of assigned tasks fragment.
     * @author Balraj VE00YM023
     */
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev = dataList.removeAt(fromPosition)
        dataList.add(if(toPosition > fromPosition) toPosition -1 else toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
        Utils.sortedAssingedTasks = dataList
    }

    override fun onItemDismiss(position: Int) {
    }
}