package ymsli.com.couriemate.utils.common

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskStatus : ENUM of Task status
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
enum class TaskStatus(val statusId:Int, val color: String) {
    UNASSIGNED(1, Constants.COLOR_DEFAULT),
    ASSIGNED(2, Constants.COLOR_ASSIGNED),
    DELIVERING(3, Constants.COLOR_DELIVERING),
    DELIVERED(4, Constants.COLOR_DELIVERED),
    REFUSED(5, Constants.COLOR_DEFAULT),
    CANCELLED(6, Constants.COLOR_DEFAULT),
    FAILED(7, Constants.COLOR_FAILED),
    RETURNED(8, Constants.COLOR_DELIVERED);


    companion object{
        fun getEnumById(id:Int): TaskStatus?{
            for(taskStatus in values()){
                if(taskStatus.statusId===id){
                    return taskStatus
                }
            }
            return null
        }
    }
}
