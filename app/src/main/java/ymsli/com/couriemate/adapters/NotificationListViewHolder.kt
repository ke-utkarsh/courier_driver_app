package ymsli.com.couriemate.adapters

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseItemViewHolder
import ymsli.com.couriemate.database.entity.NotificationResponse
import ymsli.com.couriemate.di.component.ViewHolderComponent
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.views.notifications.NotificationListItemViewModel
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.notification_list_item.view.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 7, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * NotificationListViewHolder : View holder for the notification list fragment's recycler view.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class NotificationListViewHolder(parent:ViewGroup): BaseItemViewHolder<NotificationResponse,
        NotificationListItemViewModel> (R.layout.notification_list_item,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupView(view: View) {}

    override fun setupObservers() {
        super.setupObservers()
        viewModel.header.observe(this, Observer {
            itemView.tv_notification_list_item_header.text = it
        })

        viewModel.detail.observe(this, Observer {
            itemView.tv_notification_list_item_detail.text = it
        })

        viewModel.receivedOn.observe(this, Observer {
            itemView.tv_notification_list_item_time.text =
                Utils.formatTimeForNotification(it)
        })
    }

    /**
     * Bind the recycler item, if this item is unread then highlight this item.
     * @author Balraj VE00YM023
     */
    fun bind(data: NotificationResponse, isUnread: Boolean) {
        super.bind(data)
        initializeRecyclerView(data)
        (itemView as CardView).setCardBackgroundColor(
            if (isUnread) Color.parseColor("#90bcf5") else Color.parseColor("#FFFFFF"))
    }

    /**
     * Load the data in recycler view on the initialization.
     */
    private fun initializeRecyclerView(data: NotificationResponse){
        itemView.tv_notification_list_item_header.text = data.eventHeader
        itemView.tv_notification_list_item_detail.text = data.eventBody
        itemView.tv_notification_list_item_time.text  =
            Utils.formatTimeForNotification(data.receivedOn)
    }
}