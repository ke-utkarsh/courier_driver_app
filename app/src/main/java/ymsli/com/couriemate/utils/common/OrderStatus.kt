/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   July 9, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * OrderStatus : An enum representing all the status associated with the order
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

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
 * OrderStatus : Order Status ENUM
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
enum class OrderStatus(statusId:Int) {
    INVALID(0),
    REGISTERING(1),
    UNDELIVERED(2),
    DELIVERING(3),
    DELIVERED(4),
    REFUSED(5),
    CLOSE_CANCEL(6),
    CLOSE_DELIVERED(7),
    CLOSE_REFUSED(8),
    CLOSE_RESUME(9);

    val statusId = statusId
}