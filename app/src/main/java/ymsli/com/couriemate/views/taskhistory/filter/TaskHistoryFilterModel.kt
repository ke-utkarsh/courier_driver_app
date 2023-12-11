package ymsli.com.couriemate.views.taskhistory.filter

import java.sql.Timestamp


/**
 * @constructor
 * @param selectedBtnIndex index of the selected button in ReturnTaskListFilterDialog, -1 if no button is selected
 * @param taskStatusId status id to be be used for applyReturnListFilter, can be one of TaskStatus enum's statusId
 * @param toDate
 */
data class TaskHistoryFilterModel(var selectedBtnIndex: Int, var taskStatusId: Int?,
                                  var spinnerIndex: Int,
                                  var toDate: Timestamp?, var fromDate: Timestamp?){
    fun clone(): TaskHistoryFilterModel {
        return TaskHistoryFilterModel(
            this.selectedBtnIndex,
            this.taskStatusId,
            this.spinnerIndex,
            this.toDate,
            this.fromDate
        )
    }

    fun equals(o: TaskHistoryFilterModel): Boolean{
        val selectedBtnPredicate = this.selectedBtnIndex == o.selectedBtnIndex
        val fromDatePredicate: Boolean = if((this.fromDate == null) && (o.fromDate == null)) true
        else if((this.fromDate == null) && (o.fromDate != null)) false
        else if((this.fromDate != null) && (o.fromDate == null)) false
        else (this.fromDate!!.equals(o.fromDate))

        val toDatePredicate: Boolean = if((this.toDate == null) && (o.toDate == null)) true
        else if((this.toDate == null) && (o.toDate != null)) false
        else if((this.toDate != null) && (o.toDate == null)) false
        else (this.toDate!!.equals(o.toDate))

        val spinnerIndexPredicate = this.spinnerIndex == o.spinnerIndex

        return selectedBtnPredicate && fromDatePredicate && toDatePredicate && spinnerIndexPredicate
    }

    fun isSet(): Boolean{
        return (selectedBtnIndex != -1 || taskStatusId != -1
                || toDate != null || fromDate != null || spinnerIndex != 0)
    }

    fun isNotSet(): Boolean{
        return (selectedBtnIndex == -1 && taskStatusId == -1 &&
                spinnerIndex == 0 && toDate == null && fromDate == null)
    }

    companion object{
        fun getDefaultState(): TaskHistoryFilterModel {
            return TaskHistoryFilterModel(
                selectedBtnIndex = -1,
                taskStatusId = -1, spinnerIndex = 0, toDate = null, fromDate = null
            )
        }
    }
}