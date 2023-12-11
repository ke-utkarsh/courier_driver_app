package ymsli.com.couriemate.utils.services

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkerParameters
import org.json.JSONArray
import org.json.JSONObject
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.base.BaseWorkManager
import ymsli.com.couriemate.model.LocationConfigResponseData
import ymsli.com.couriemate.utils.common.Constants
import java.lang.Exception

class TripConfigWorkManager(private val context: Context, workerParameters: WorkerParameters):BaseWorkManager(context,workerParameters) {

    override fun inject(serviceComponent: ymsli.com.couriemate.di.component.ServiceComponent) {
        serviceComponent.inject(this)
    }

    override fun doWork(): Result {
        init()
        return try{
            if(isNetworkConnected()){
                getLocationConfigServerURL()
                //getTripConfigs()
            }
            Result.success()
        }
        catch (ex: Exception){
            Result.success()
        }
    }

    /**
     * returns configurable items
     * related to trip/location history
     */
    private fun getTripConfigs(){
        compositeDisposable.addAll(
        couriemateRepository.getCodeMasterConfig(14)
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                if(it!=null){
                    val duration = ((JSONArray(it.data.get("value")).get(0)) as JSONObject).get("MinimumDuration")
                    val displacement = ((JSONArray(it.data.get("value")).get(0)) as JSONObject).get("MinimumDistance")
                    val locationHistoryServer: String? = ((JSONArray(it.data.get("value")).get(0)) as JSONObject).get("LocationHistoryServer").toString()
                    couriemateRepository.setMinimumDuration(duration as Int)
                    couriemateRepository.setMinimumDistance(displacement as Int)
                    if(!locationHistoryServer.isNullOrBlank()) couriemateRepository.setLocationHistoryURL(locationHistoryServer as String)
                }
            },{
                Log.d("Error",it.toString())
            })
        )
    }

    /**
     * gets server URL and then tirp configuration
     * like sleep time
     */
    private fun getLocationConfigServerURL(){
        if(couriemateRepository.getUserId()!=0L) {
            compositeDisposable.addAll(
                couriemateRepository.getCodeMasterConfig(15)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        val locationConfigServer: String? =
                            (JSONArray(it.data.get("value")).get(0) as JSONObject).get(it.description)
                                .toString()
                        if (!locationConfigServer.isNullOrBlank()) {
                            val locationServer = "${locationConfigServer}${Constants.LOCATION_STORE_SERVER_END_POINT}"
                            couriemateRepository.setLocationHistoryURL(locationServer)
                            getConfigs(locationConfigServer)
                        }
                    }, {
                        Log.d("Error", it.localizedMessage)
                    })
            )
        }
    }

    private fun getConfigs(serverURL: String) {
        val endPointURL = "${serverURL}${Constants.LOCATION_CONFIG_SERVER_END_POINT}"
        val userId = couriemateRepository.getUserId()
        compositeDisposable.addAll(
            couriemateRepository.getLocationConfiguration(userId, endPointURL)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    // store configuration locally
                    if (it.responseData != null) {
                        val configData: LocationConfigResponseData = it.responseData
                        if (configData.send_location == 1) couriemateRepository.setSendLocation(true)
                        else couriemateRepository.setSendLocation(false)
                        couriemateRepository.setLocationInterval(configData.interval ?: 10)
                        couriemateRepository.setLocationSpeed(configData.speed ?: 5)
                        couriemateRepository.setSleepModeTime(configData.sleepModeTime)
                        // trigger local broadcast to start location foreground service
                        sendConfigBroadcast()
                    }
                }, {
                    Log.d("Error", it.localizedMessage)
                })
        )
    }

    /**
     * trigger local broadcast to start location foreground service
     */
    private fun sendConfigBroadcast(){
        val intent = Intent("couriemate_trip_config")
        val appli = (context.applicationContext as CourieMateApplication)
        intent.setClass(appli,TripConfigCallback::class.java)
        intent.putExtra("CONFIG_RECEIVED",true)
        SystemClock.sleep(2000)
        appli.sendBroadcast(intent)
    }
}

