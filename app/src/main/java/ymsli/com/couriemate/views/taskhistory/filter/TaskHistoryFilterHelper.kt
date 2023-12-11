package ymsli.com.couriemate.views.taskhistory.filter

import android.icu.text.SimpleDateFormat
import ymsli.com.couriemate.database.entity.TaskHistoryResponse
import ymsli.com.couriemate.utils.common.Constants

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
class TaskHistoryFilterHelper {

    /**
     * Filters the recycler view dataList based on the supplied predicate.
     * this method is used by the applyReturnListFilter dialog of recycler view,
     * which provides different ways to applyReturnListFilter dataList based on the task status.
     *
     * @param predicate model which specifies what type of filtering is desired
     * @author Balraj VE00YM023
     */
    fun filterByPredicate(originalList: ArrayList<TaskHistoryResponse>,
                          predicate: TaskHistoryFilterModel
    ) : ArrayList<TaskHistoryResponse>{
        return getFilteredListByPredicate(originalList, predicate)
    }

    private fun getFilteredListByPredicate(originalList: ArrayList<TaskHistoryResponse>,
                                           predicate: TaskHistoryFilterModel)
            : ArrayList<TaskHistoryResponse>{

        if(predicate.isNotSet()){
            return originalList
        }
        var filteredList = applyTaskStatusFilter(predicate, originalList)
        filteredList = applyDateFilter(predicate, filteredList)
        return filteredList
    }

    private fun applyTaskStatusFilter(predicate: TaskHistoryFilterModel,
                                      originalList: ArrayList<TaskHistoryResponse>)
            :ArrayList<TaskHistoryResponse>{

        if (predicate.taskStatusId != null && predicate.taskStatusId != -1) {
            return ArrayList(originalList.filter { o -> o.taskStatusId == predicate.taskStatusId})
        }
        return originalList
    }

    private fun applyDateFilter(predicate: TaskHistoryFilterModel,
                                originalList: ArrayList<TaskHistoryResponse>)
            :ArrayList<TaskHistoryResponse>{
        if((predicate.toDate != null) && (predicate.fromDate != null)){
            return ArrayList(originalList.filter{compareDate(predicate,it)})
        }
        return originalList
    }

    private fun compareDate(predicate: TaskHistoryFilterModel, model: TaskHistoryResponse): Boolean{
        val comparisonDateString =  model.endDate
        val comparisonDate = SimpleDateFormat(Constants.COURIEMATE_TIMESTAMP_FORMAT)
            .parse(comparisonDateString)
        return (comparisonDate >= predicate.fromDate) && (comparisonDate <= predicate.toDate)
    }

}