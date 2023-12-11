package ymsli.com.couriemate.views.chat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_item.view.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.utils.common.Utils

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj (VE00YM023)
 * @date    12 JAN 2021, 11:15 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ChatAdapter : Recycler Adapter for the chat screen.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */


class ChatAdapter(private val userId: String,
                  private var dataSet: List<ChatMessage>)
    : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ViewHolder(viewHolder, userId)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = dataSet[position]
        setListeners(holder, chatMessage)
        holder.bindItems(position, chatMessage)
    }

    override fun getItemCount() = dataSet.size

    private fun setListeners(viewHolder: ViewHolder, message: ChatMessage) {
        viewHolder.itemView.setOnClickListener {
            viewHolder.bindItems(viewHolder.adapterPosition, message)
        }
    }

    class ViewHolder(v: View, private val userId: String) : RecyclerView.ViewHolder(v) {

        fun bindItems(position: Int, message: ChatMessage) {
            hideAllViews(itemView)
            loadMessage(itemView, message)
        }

        private fun hideAllViews(v: View) {
            v.container_user_message.visibility = View.GONE
            v.container_admin_message.visibility = View.GONE
            v.iv_message_synced.visibility = View.GONE
            v.iv_message_not_synced.visibility = View.GONE
        }

        private fun loadMessage(v: View, message: ChatMessage) {
            if (message.senderJid == userId) {
                v.container_user_message.visibility = View.VISIBLE
                v.tv_user_message.text = message.text
                v.tv_message_sent_on.text = Utils.formatTimestampForUI(message.timestamp)
            } else {
                v.tv_incoming_msg_sender_name.text = message.senderJid.split("@")[0]
                v.container_admin_message.visibility = View.VISIBLE
                v.tv_admin_message.text = message.text
                v.tv_message_received_on.text = Utils.formatTimestampForUI(message.timestamp)
            }

            /** If this is an incoming message then don't show any status icon */
            if (message.receiverJid == userId) return
            /** Currently we don't show status, may be in future */
            /*when (message.status) {
                ChatMessage.NOT_DELIVERED -> v.iv_message_not_synced.visibility = View.VISIBLE
                ChatMessage.DELIVERED -> v.iv_message_synced.visibility = View.VISIBLE
                ChatMessage.SEEN -> v.iv_message_seen.visibility = View.VISIBLE
                else -> {
                }
            }*/
        }
    }
}