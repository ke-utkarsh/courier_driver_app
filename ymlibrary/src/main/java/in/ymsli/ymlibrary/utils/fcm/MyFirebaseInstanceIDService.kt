/**
 * Project Name :YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 27, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * MyfirebaseInstanceIDService Class : responsible for saving token on refresh
 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description
-----------------------------------------------------------------------------------

 */


package `in`.ymsli.ymlibrary.utils.fcm

import `in`.ymsli.ymlibrary.config.YMConstants
import `in`.ymsli.ymlibrary.utils.SharedPrefsUtils
import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any
     *
     *
     *
     *
     * server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        SharedPrefsUtils.setStringPreference(this, YMConstants.FCM_TOKEN, token)

    }

    companion object {

        private val TAG = "MyFirebaseIIDService"
    }
}