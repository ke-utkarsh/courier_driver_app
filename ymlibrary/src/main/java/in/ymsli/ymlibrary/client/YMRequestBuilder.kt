/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 *
 * YMRequestBuilder : provides base request for creating client
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */


package `in`.ymsli.ymlibrary.client

import `in`.ymsli.ymlibrary.config.YMConstants
import java.util.concurrent.TimeUnit


/**
 * @param baseUrl base url for request must end with "/
 * @param contentType content type for request
 * @param connectTimeOut connection timeout
 * @param readTimeOut timeou for reading
 * @param writeTimeOut write timeout on server
 * @param headermap map of key,value pair as  headers
 */
class YMRequestBuilder(baseUrl: String, contentType: String) {

    var baseUrl: String? = null  //base url for request must end with "/
    var contentType: String? = null //content type for request
    var connectTimeOut: Long?      //connection timeout
    var readTimeOut: Long?         //read timeout
    var writeTimeOut: Long?        // write timeout
    var timeOutUnit: TimeUnit? = null        // timeout unit
    var headermap: HashMap<String, String>?


    init {

        this.baseUrl = baseUrl
        this.contentType = contentType
        connectTimeOut = YMConstants.TIMEOUT_CONNECT
        readTimeOut = YMConstants.TIMEOUT_READ
        writeTimeOut = YMConstants.TIMEOUT_WRITE
        headermap = HashMap()
        timeOutUnit = YMConstants.TIMOUT_UNIT


    }

    /**
     * Method to accept headers
     * @param map hashmap of key value pairs
     */
    fun addHeaders(map: HashMap<String, String>) {
        this.headermap!!.putAll(map)
    }

    /**
     * Method to add timeout for request
     * @param connectTimeOut for connectig to server
     * @param readTimeOut reading data from server
     * @param writeTimeOut writing timeout
     * @param timeOutUnit timeout unit
     */
    fun addTimeOut(connectTimeOut: Long, readTimeOut: Long, writeTimeOut: Long, timeUnit: TimeUnit) {
        this.connectTimeOut = connectTimeOut
        this.readTimeOut = readTimeOut
        this.writeTimeOut = writeTimeOut
        this.timeOutUnit = timeOutUnit

    }


}