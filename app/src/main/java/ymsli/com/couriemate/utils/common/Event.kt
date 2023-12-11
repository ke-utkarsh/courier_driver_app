package ymsli.com.couriemate.utils.common

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * Event : Used as a wrapper for dataList that is exposed via a LiveData that
 *          represents an event.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
data class Event<out T>(private val content: T) {

    private var hasBeenHandled = AtomicBoolean(false)

    /**
     * Returns the content and prevents its use again.
     */
    fun getIfNotHandled(): T? = if (hasBeenHandled.getAndSet(true)) null else content

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peek(): T = content
}
