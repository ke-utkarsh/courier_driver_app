package ymsli.com.couriemate.utils.common

import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import android.icu.text.SimpleDateFormat

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * FilterHelper : used to apply filter based on Predicate or filter model.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class FilterHelper(private val comparisonMode: ComparisonMode) {

    /**
     * Filters the recycler view dataList based on the supplied predicate.
     * this method is used by the applyReturnListFilter dialog of recycler view,
     * which provides different ways to applyReturnListFilter dataList based on the task status.
     *
     * @param predicate model which specifies what type of filtering is desired
     * @author Balraj VE00YM023
     */
    fun filterByPredicate(originalList: ArrayList<TaskRetrievalResponse>,
                          predicate: FilterModel
    ) : ArrayList<TaskRetrievalResponse>{
        return getFilteredListByPredicate(originalList, predicate)
    }

    /**
     * Filters the provided list by the pickup address
     */
    fun filterByPickupAddress(pickupAddress: String,
                              originalList: ArrayList<TaskRetrievalResponse>):
            ArrayList<TaskRetrievalResponse>{
        return ArrayList(originalList.filter { it.pickupLocation == pickupAddress })
    }

    private fun getFilteredListByPredicate
                (originalList: ArrayList<TaskRetrievalResponse>,predicate: FilterModel)
            : ArrayList<TaskRetrievalResponse>{

        if(predicate.isNotSet()){
            return originalList
        }
        var filteredList = applyTaskStatusFilter(predicate, originalList)
        filteredList = applyDateFilter(predicate, filteredList)
        return filteredList
    }

    private fun applyTaskStatusFilter(predicate: FilterModel, originalList: ArrayList<TaskRetrievalResponse>)
            :ArrayList<TaskRetrievalResponse>{

        if (predicate.taskStatusId != null && predicate.taskStatusId != -1) {
            return ArrayList(originalList.filter { o -> o.taskStatusId == predicate.taskStatusId})
        }
        return originalList
    }

     private fun applyDateFilter(predicate: FilterModel, originalList: ArrayList<TaskRetrievalResponse>)
            :ArrayList<TaskRetrievalResponse>{
        if((predicate.toDate != null) && (predicate.fromDate != null)){
            return ArrayList(originalList.filter{compareDate(predicate,it)})
        }
        return originalList
    }

    private fun compareDate(predicate: FilterModel, model: TaskRetrievalResponse): Boolean{
        val comparisonDateString = when(comparisonMode){
            ComparisonMode.ASSIGNED -> model.assignedDate
            ComparisonMode.ONGOING  -> model.startDate
            ComparisonMode.DONE     -> model.endDate
        }

        val comparisonDate = SimpleDateFormat(Constants.COURIEMATE_TIMESTAMP_FORMAT)
            .parse(comparisonDateString)
        return (comparisonDate >= predicate.fromDate) && (comparisonDate <= predicate.toDate)
    }

    enum class ComparisonMode{ASSIGNED, ONGOING, DONE}
}