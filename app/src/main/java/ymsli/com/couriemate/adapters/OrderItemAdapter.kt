package ymsli.com.couriemate.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ymsli.com.couriemate.R
import ymsli.com.couriemate.database.entity.OrderItem

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Atul Kumar (VE00YM430)
 * @date   DEC 14,06,2022
 * Copyright (c) 2022, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * OrderItem Details in OrderDetails Page : .
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
class OrderItemAdapter(val items: List<OrderItem>) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_item_design, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(orderItem: OrderItem) {
            val tvOrderItem = itemView.findViewById(R.id.tvOrderItem) as TextView
            val tvQuantity  = itemView.findViewById(R.id.tvQuantity) as TextView
            val tvWeight  = itemView.findViewById(R.id.tvWeight) as TextView
            tvOrderItem.text = orderItem.name
            tvQuantity.text = orderItem.quantity
            tvWeight.text = orderItem.weight
        }
    }

}
