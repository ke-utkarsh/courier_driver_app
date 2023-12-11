package ymsli.com.couriemate.views.tasklist.drag

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 3, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * ItemTouchHelperAdapter : Adapter used for the drag and drop feature on the assigned
 *                          tasks list.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
interface ItemTouchHelperAdapter {

    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and not at the end of a "drop" event.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then end position of the moved item.
     * @author Balraj VE00YM023
     */
    fun onItemMove(fromPosition: Int, toPosition: Int)

    /**
     * Called when an item has been dismissed by a swipe.
     * @param position The position of the item dismissed.
     * @author Balraj VE00YM023
     */
    fun onItemDismiss(position: Int)
}