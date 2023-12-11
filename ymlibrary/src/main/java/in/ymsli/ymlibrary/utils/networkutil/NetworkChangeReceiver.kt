/**
 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 12, 2018
 * Description
 * -----------------------------------------------------------------------------------
 * NetworkChangeReceiver :  This class broadcasts changing in network state
 * -----------------------------------------------------------------------------------
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------

 */

package `in`.ymsli.ymlibrary.utils.networkutil

import `in`.ymsli.ymlibrary.utils.CommonUtils
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NetworkChangeReceiver : BroadcastReceiver() {

    // Set NetworkChangeListener first in order to get status
    var networkChangeListener: NetworkChangeListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        val status = ConnectivityUtils.findNetworkType(context)
        CommonUtils.showLog(Log.INFO, status)

        if (networkChangeListener != null) {
            networkChangeListener!!.onNetworkConnectionChanged(ConnectivityUtils.isConnected(context))

        }
    }
}

