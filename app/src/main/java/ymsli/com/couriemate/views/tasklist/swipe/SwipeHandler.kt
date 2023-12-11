package ymsli.com.couriemate.views.tasklist.swipe

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ymsli.com.couriemate.R
import ymsli.com.couriemate.adapters.TaskListAdapter
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.utils.common.TaskStatus
import android.content.Context

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Oct 16, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * SwipeHandler : A wrapper class to provide the swipe feature to a recycler view.
 *                this class internally configures the swipe callbacks by using the
 *                SwipeCallback class.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class SwipeHandler(private val context: Context,
                   private val recyclerView: RecyclerView,
                   private val recyclerAdapter: TaskListAdapter
){

    private lateinit var leftSwipeCallback: SwipeCallback
    private lateinit var rightSwipeCallback: SwipeCallback
    var leftSwipeAction: SwipeAction? = null
    var rightSwipeAction: SwipeAction? = null

    private lateinit var task : TaskRetrievalResponse

    init{
        configureSwipeCallbacks()
    }

    /**
     * Configure the left and right swipe handlers and attach them to the given recycler view.
     * @author Balraj VE00YM023
     */
    private fun configureSwipeCallbacks(){
        /** Configure the left swipe for the call feature */
        leftSwipeCallback = getSwipeCallback(
            ItemTouchHelper.LEFT, Color.parseColor("#FF9400"),
            ContextCompat.getDrawable(context, R.drawable.ic_phone_in_talk_white_24dp)!!,
            object : SwipeCallback.Swipeable {
                override fun isSwipeable(viewHolder: RecyclerView.ViewHolder): Boolean {
                    return true
                }
            })
        val itemTouchHelper = ItemTouchHelper(leftSwipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        /** Configure the right swipe for the task pickup feature */
        rightSwipeCallback = getSwipeCallback(
            ItemTouchHelper.RIGHT, Color.parseColor("#46A853"),
            ContextCompat.getDrawable(context, R.drawable.ic_swipe_pickup_28dp)!!,
            object: SwipeCallback.Swipeable {
                /** Only allow the swipe feature on the items with task status assigned */
                override fun isSwipeable(viewHolder: RecyclerView.ViewHolder): Boolean {
                    return if(viewHolder.adapterPosition != -1){
                        task = recyclerAdapter.dataList[viewHolder.adapterPosition]
                        ( ((((task.taskSequenceNo==1)||(task.taskSequenceNo==3)) && (task.taskStatusId== TaskStatus.ASSIGNED.statusId))) ||
                                ((task.taskSequenceNo==3) && (task.completed==1) && (task.taskStatusId== TaskStatus.ASSIGNED.statusId) ))
                    } else{
                        false
                    }
                }
            })

        val itemPickHelper = ItemTouchHelper(rightSwipeCallback)
        itemPickHelper.attachToRecyclerView(recyclerView)
    }


    /**
     * Utility function to get a swipe callback for a specific direction. (ie. left or right)
     * @author Balraj VE00YM023
     */
    private fun getSwipeCallback(swipeDirection: Int, backgroundColor: Int,
                                 backgroundIcon: Drawable, swipeable: SwipeCallback.Swipeable
    ): SwipeCallback {
        return object: SwipeCallback(swipeDirection, backgroundColor, backgroundIcon, swipeable) {

            /**
             * When any item is swiped pass the selected item to implementing activity,
             * using the perform function of callback interface.
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                task = recyclerAdapter.dataList.get(viewHolder.adapterPosition)
                if(direction== ItemTouchHelper.RIGHT){
                    leftSwipeAction?.perform(task)
                    recyclerView.adapter = recyclerAdapter
                }
                else{
                    viewHolder.itemView.isClickable = false
                    rightSwipeAction?.perform(task)
                    viewHolder.itemView.isClickable = true
                }
            }
        }

    }

    /********* Utility functions that can be used by the activity to enable/disable swipe *******/
    fun enableSwipe(){
        leftSwipeCallback.isSwipable = true
        rightSwipeCallback.isSwipable = true
    }

    fun disableSwipe(){
        leftSwipeCallback.isSwipable = false
        rightSwipeCallback.isSwipable = false
    }

    /**
     * This interface is used by this class to communicate with the implementing
     * activity to perform the operations when any item is swiped.
     * @author Balraj VE00YM023
     */
    interface SwipeAction{
        fun perform(task: TaskRetrievalResponse)
    }
}