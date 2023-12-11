package ymsli.com.couriemate.views.splash

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseActivity
import ymsli.com.couriemate.di.component.ActivityComponent
import ymsli.com.couriemate.model.AppVersionResponse
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.ViewUtils
import ymsli.com.couriemate.views.login.LoginActivity
import ymsli.com.couriemate.views.main.MainActivity
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_splash.*
import java.net.NetworkInterface
import java.util.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 11, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SplashActivity : This is the launcher activity of application, based on the user's
 *                  log in status, it directs the flow to appropriate activity.
 *
 *                  if user is not logged in then move to Login Activity
 *                  if user is already logged in then move to Main Activity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

class SplashActivity : BaseActivity<SplashViewModel>() {

    /** Define the layout for this activity */
    override fun provideLayoutId(): Int = R.layout.activity_splash

    /** Prepare and inject dependencies for this activity */
    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    /**
     * Setup the initial state of progress bar and then call the app version API
     * to get the state of current apk version, based on the result of app version API
     * let the user use the app or halt the execution if current APK version is not stable.
     *
     * @author Balraj VE00YM023
     */
    override fun setupView(savedInstanceState: Bundle?) {
        progressBar.max = 100
        progressBar.progress = 20
        val deviceId = generateUniqueID()?:this.resources.getString(R.string.place_holder_na)
        viewModel.storeDeviceId(deviceId)
        Utils.animateProgressBar(progressBar, 1000, 20)
        viewModel.getAppVersion()
    }

    /**
     * Observe the response of api.
     * if there is no response (in case of no internet or some exception in request handling)
     * then ask user for required permissions and move them to next activity.
     *
     * if app version api returns a response then only move to next screen if current APK
     * version is stable and force update is not required.
     * @author Balraj VE00YM023
     */
    override fun setupObservers() {
        super.setupObservers()
        viewModel.apiResponse.observe(this, Observer {
            if(it != null){
                setAppAccessLevel(it)
            }
            else{
                setPermissions()
            }
        })

        /** When notifications are synced then move to MainActivity */
        viewModel.notificationsSynced.observe(this, Observer {
            var intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        })
    }

    /**
     * Based on the response of app version API, set the app's access level.
     * @author Balraj VE00YM023
     */
    private fun setAppAccessLevel(status: AppVersionResponse){
        if(!status.isStable!!){
            /* Current app version is not stable, stop user from using application */
            showSystemErrorDialog()
        }
        else if(status.forceUpdate!!){
            /* Current app version can't operate with current api, update app forcefully */
            showForceUpdateDialog()
        }
        else if(!status.isLatestVersion!!){
            /* A newer version of application is available, let the user know */
            showOptionalUpdateDialog()
        }
        else{
            /* Everything is in order continue with normal application flow */
            setPermissions()
        }
    }

    /**
     * Check if required permission are granted by the user, if not then ask for the
     * permissions.
     * @author Balraj VE00YM023
     */
    private fun setPermissions() {
        Utils.animateProgressBar(progressBar, 1000,70)
        if (checkPermissions()) {
            Handler().postDelayed({ startNextActivity() },Constants.SPLASH_DELAY)
        }
        else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_CALENDAR),100)
        }
    }

    /**
     * Check if required permissions are granted by the user.
     * Permissions
     *      * ACCESS_FINE_LOCATION -> Required for the map feature
     *      * WRITE_CALENDAR -> Required to add the reminder entry in calender
     * @return true if permissions are granted, otherwise false
     * @author Balraj VE00YM023
     */
    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    /**
     * When permission request result is available move to the next activity.
     * @author Balraj VE00YM023
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Handler().postDelayed({ startNextActivity() },Constants.SPLASH_DELAY)
    }

    /**
     * When application starts check if user is already logged in.
     * Depending on the logged in status, move to the appropriate activity.
     * Ex. Login or Home screen
     *
     * @author Balraj VE00YM023
     */
    private fun startNextActivity(){
        Utils.animateProgressBar(progressBar, 100,90)
        var isLoggedIn = viewModel.isLoggedIn()
        if(isLoggedIn){
            //call Location config API
            viewModel.getLocationConfiguration()
            /** First try to sync notifications and then move to MainActivity */
            viewModel.syncNotifications()
        }
        else{
            var intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    /**
     * Currently installed application version is not stable.
     * If user continues to use application some unexpected errors might occur,
     * this method will show a notification about unstable state and then kills the application.
     *
     * @author Balraj VE00YM023
     */
    private fun showSystemErrorDialog(){
        var onConfirm = DialogInterface.OnClickListener { _, _ -> finishAndRemoveTask()}
        var alertDialog = ViewUtils.getNotificationDialog(this,R.style.AlertDialogTheme,
            getString(R.string.UNSTABLE_STATE),onConfirm)
        alertDialog?.show()
    }

    /**
     * Currently installed application version can't operate correctly with current api version.
     * this method will show a confirmation dialog to update the app, if user cancels then app is killed.
     *
     * @author Balraj VE00YM023
     */
    private fun showForceUpdateDialog(){
        var onConfirm = DialogInterface.OnClickListener { _, _ -> }
        var onCancel  = DialogInterface.OnClickListener { _, _ -> finishAndRemoveTask()}
        var alertDialog = ViewUtils.getConfirmationDialog(this, R.style.AlertDialogTheme,
            getString(R.string.FORCE_UPDATE), onConfirm, onCancel)
        alertDialog!!.show()
    }

    /**
     * A newer version of application is available.
     * this method shows a notification and then continues
     * with normal flow of application.
     *
     * @author Balraj VE00YM023
     *
     */
    private fun showOptionalUpdateDialog(){
        var onConfirm = DialogInterface.OnClickListener { _, _ -> setPermissions()}
        var onCancel  = DialogInterface.OnClickListener { _, _ -> setPermissions()}
        var alertDialog = ViewUtils.getConfirmationDialog(this, R.style.AlertDialogTheme,
            getString(R.string.UPDATE_AVAILABLE), onConfirm, onCancel)
        alertDialog!!.show()
    }

    /**
     * generates unique ID
     * be it android id,
     * MAC Address,
     * or UUID
     */
    private fun generateUniqueID(): String?{
        val androidId: String? = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
        if(androidId.isNullOrEmpty()){
            try {
                var macAddress: String? = null
                val all: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.getName().equals("wlan0",true)) continue
                    val macBytes: ByteArray? = nif.getHardwareAddress()
                    if (macBytes!=null && macBytes.isNotEmpty()) {
                        macAddress = ""
                    }
                    val res1 = StringBuilder()
                    for (b in macBytes!!) {
                        res1.append(String.format("%02X:", b))
                    }
                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    macAddress = res1.toString()
                }
                if(!macAddress.isNullOrEmpty()) return macAddress
                else{
                    val uuid = UUID.randomUUID().toString()
                    return uuid
                }
            } catch (ex: Exception) {
            }
        }
        else return androidId
        return null
    }
}
