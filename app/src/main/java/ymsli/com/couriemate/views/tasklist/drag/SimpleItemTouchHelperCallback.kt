package ymsli.com.couriemate.views.tasklist.drag

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 3, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * SimpleItemTouchHelperCallback : ItemTouchHelper.Callback implementation to provide the
 *                                 drag and drop feature on the assigned tasks list.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class SimpleItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter) :
                                             ItemTouchHelper.Callback() {

    /** We provide the long press drag feature */
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * Item swipe is also enabled, to provide swipe to call and swipe to pick features.
     * @author Balraj VE00YM023
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    /**
     * Setup the direction flags for particular item.
     * we only provide up and down movement for drag and drop feature.
     * @author Balraj VE00YM023
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = 0
        return makeMovementFlags(
            dragFlags,
            swipeFlags
        )
    }

    /**
     * When item is moved use the ItemTouchHelperAdapter to pass the item information
     * to the recycler adapter.
     *
     * @author Balraj VE00YM023
     */
    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }
        mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    /**
     * This implementation doesn't handle the item swipe feature.
     * @author Balraj VE00YM023
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

}