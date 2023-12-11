package ymsli.com.couriemate.adapters

import ymsli.com.couriemate.base.BaseAdapter
import ymsli.com.couriemate.database.entity.NotificationResponse
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 7, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * NotificationListAdapter : Recycler Adapter for Notification list fragment
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class NotificationListAdapter(parentLifeCycle : Lifecycle,
                              notificationList: ArrayList<NotificationResponse>)
    :BaseAdapter<NotificationResponse, NotificationListViewHolder>(parentLifeCycle, notificationList)
{



    private var recyclerView: RecyclerView? = null
    private var emptyNotificationMode = false
    private var noNotificationsView: TextView? = null

    /**
     * Unread count is used to highlight the currently active notification.
     * */
    private var unreadCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListViewHolder =
        NotificationListViewHolder(parent)

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
        noNotificationsView = view
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
        noNotificationsView = null
    }

    /**
     * Calls the load data of parent and then shows the empty notification view
     * if empty notification mode is on and the data list of adapter is empty.
     *
     * @param data: data to be loaded in recycler adapter
     * @author Balraj VE00YM023
     */
    fun loadData(data: ArrayList<NotificationResponse>,
                          unreadCount: Int) {
        this.unreadCount = unreadCount
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
        noNotificationsView?.visibility = View.VISIBLE
    }

    /**
     * Shows the recycler view and hides the no tasks available view.
     * @author Balraj VE00YM023
     */
    private fun showRecyclerView() {
        recyclerView?.visibility = View.VISIBLE
        noNotificationsView?.visibility = View.GONE
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onBindViewHolder(holder: NotificationListViewHolder, position: Int) {
        holder.bind(dataList[position], position < unreadCount)
    }

    override fun onViewAttachedToWindow(holder: NotificationListViewHolder) {
        holder.bind(dataList[holder.adapterPosition], holder.adapterPosition < unreadCount)
    }
}