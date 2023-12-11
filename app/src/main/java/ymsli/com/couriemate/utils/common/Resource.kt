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
 * Resource : ENUM of resource  if erroneous/successful. Used in user input validations
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
data class Resource<out T> constructor(val status: Status, val data: T?) {

    companion object {
        fun <T> success(data: T? = null): Resource<T> =
            Resource(
                Status.SUCCESS,
                data
            )

        fun <T> error(data: T? = null): Resource<T> =
            Resource(
                Status.ERROR,
                data
            )

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(
                Status.LOADING,
                data
            )

        fun <T> unknown(data: T? = null): Resource<T> =
            Resource(
                Status.UNKNOWN,
                data
            )
    }
}