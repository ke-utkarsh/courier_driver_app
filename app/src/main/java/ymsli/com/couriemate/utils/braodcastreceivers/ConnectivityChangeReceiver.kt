package ymsli.com.couriemate.utils.braodcastreceivers

import ymsli.com.couriemate.utils.ConnectivityUtils
import ymsli.com.couriemate.utils.services.SyncIntentService
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ConnectivityChangeReceiver : This is the broadcast receiver which notifies the
 *                              internet connectivity changes. Thus data from phone's
 *                               local storage can be synced.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class ConnectivityChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(ConnectivityUtils.isConnected(context)){
            val comp = ComponentName(context.packageName,SyncIntentService::class.java.name)
            intent.action = INTENT_ACTION_NETWORK_CONNECTED
            context.startService(intent.setComponent(comp))
        }
    }

    companion object{
        const val INTENT_ACTION_NETWORK_CONNECTED = "NETWORK_CONNECTED"
    }
}