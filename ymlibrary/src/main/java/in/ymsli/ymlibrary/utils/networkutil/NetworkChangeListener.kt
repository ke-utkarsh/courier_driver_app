/**
 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 12, 2018
 * Description
 * -----------------------------------------------------------------------------------
 *
 * NetworkChangeListener :  Interface class for callback methods
 * Need to implement this interface to get notified
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
package `in`.ymsli.ymlibrary.utils.networkutil

interface NetworkChangeListener {

    // Method to handle changes in network state
    fun onNetworkConnectionChanged(isConnected: Boolean)
}