package ymsli.com.couriemate.utils.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TripConfigCallback(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if(intent.extras?.get("CONFIG_RECEIVED")!=null){
            val configReceived = intent.getBooleanExtra("CONFIG_RECEIVED",false)
            intent.putExtra("CONFIG_RECEIVED",configReceived)
            ObservableObject.getInstance().updateValue(intent)
        }
    }
}