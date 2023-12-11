package ymsli.com.couriemate.utils.common

/**
 *
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date    Jan 11, 2020 Copyright (c) 2017, Yamaha Motor Solutions (INDIA) Pvt. Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskSummaryPeriod : Enum for different periods for which driver task summary
 * can be generated.
 * ----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By Modified On Description
 * -----------------------------------------------------------------------------------
 */
enum class TaskSummaryPeriod(val type: Int) {
    TODAY(1), THREE_DAYS(2), ONE_WEEK(3), ONE_MONTH(4);
}