/**
 * Project Name :YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * PermissionUtils Class : Responsible for handling runtime permissions

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description
-----------------------------------------------------------------------------------

 */
package `in`.ymsli.ymlibrary.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment


object PermissionUtils {

    // to check if runtimepermission is required or not
    fun useRunTimePermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    //
    /**
     *  to check if permission is available or not
     *  @param activity instance of activity
     *  @param permission permission to check
     */
    fun hasPermission(activity: Activity, permission: String): Boolean {
        return if (useRunTimePermissions()) {
            activity.checkSelfPermission(permission) === PackageManager.PERMISSION_GRANTED
        } else true
    }

    /**
     * To request multiple permissions
     * @param activity instance of activity
     * @param permission array of permissions
     * @param request code
     */
    fun requestPermissions(activity: Activity, permission: Array<String>, requestCode: Int) {
        if (useRunTimePermissions()) {
            activity.requestPermissions(permission, requestCode)
        }
    }

    /**
     * To request permission from fragments
     * @param fragment instance of fragment
     * @param permission array of permissions
     * @param requestCode request code
     */
    fun requestPermissions(fragment: Fragment, permission: Array<String>, requestCode: Int) {
        if (useRunTimePermissions()) {
            fragment.requestPermissions(permission, requestCode)
        }
    }


    /**
     *
     *
     */
    fun shouldShowRational(activity: Activity, permission: String): Boolean {
        return if (useRunTimePermissions()) {
            activity.shouldShowRequestPermissionRationale(permission)
        } else false
    }

    /**
     * To check if permission request need to show
     * @param activity instance of activity
     * @param permission permission to ask
     */
    fun shouldAskForPermission(activity: Activity, permission: String): Boolean {
        return if (useRunTimePermissions()) {
            !hasPermission(activity, permission) && (!hasAskedForPermission(activity, permission) || shouldShowRational(activity, permission))
        } else false
    }


    /**
     * To send to device settings
     * @param activity instance of activity
     */
    fun goToAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }


    /**
     * To check if already asked for permission from shared preferences
     * @param activity instance of activity
     * @param permission
     */
    fun hasAskedForPermission(activity: Activity, permission: String): Boolean {
        return SharedPrefsUtils.getBooleanPreference(activity, permission)

    }

    /**
     * To marked particular permission in sharedprefrences
     * @param activity instance of activity
     * @param permission permission
     */
    fun markedPermissionAsAsked(activity: Activity, permission: String) {
        SharedPrefsUtils.getBooleanPreference(activity, permission)

    }
}