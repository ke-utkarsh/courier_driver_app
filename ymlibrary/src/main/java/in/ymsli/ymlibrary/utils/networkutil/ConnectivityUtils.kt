/**
 * Project Name :YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 10, 2018
 * Description
 * -----------------------------------------------------------------------------------
 * ConnectivityUtils Class : responsible for providing info related to network
 * -----------------------------------------------------------------------------------
 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description


 * -----------------------------------------------------------------------------------

 */
package `in`.ymsli.ymlibrary.utils.networkutil

import `in`.ymsli.ymlibrary.config.YMConstants
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object ConnectivityUtils {

    /** @param context instance of Context
     *  @return true if internet is available else false
     */
    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * @param context instance of Context
     * @return info about network
     */
    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    fun isConnectedWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }



    /**
     * @param context instance of Context
     * @return String value 'wifi' or 'mobile_data'
     */
    fun findNetworkType(context: Context): String? {
        var networkResult: String? = null
        if (isConnected(context)) {
            if (isConnectedWifi(context)) {
                networkResult = YMConstants.WIFI_CONNECTED
            } else
                networkResult = YMConstants.MOBILE_CONNECTED
        }
        return networkResult
    }

}
