/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 11, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * CommonUtils : This class provides common methods used by application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
package `in`.ymsli.ymlibrary.utils

import `in`.ymsli.ymlibrary.config.YMConstants
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.ymsli.ymlibrary.R
import java.io.File
import java.util.ArrayList


object CommonUtils {


    /** Method to send email
     * @param mailTo recepient email address
     * @param subject subject of email
     * @param bodyText body of email
     * @param context Context instance
     */
    fun sendEmail(mailTo: String?, subject: String?,
                  bodyText: String?, context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)

        // TO
        if (mailTo != null && mailTo.length > 0) {
            emailIntent.data = Uri.parse("mailto:$mailTo")
        }

        // Subject
        if (subject != null && subject.length > 0) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        // Body
        if (bodyText != null && bodyText.length > 0) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText)
        }

        // Open all installed applications, used for sending an email
        try {
            context.startActivity(Intent.createChooser(emailIntent,
                    "Send mail via"))
        } catch (exception: Exception) {
            showToast(context.getString(R.string.msg_no_application_available_to_view_pdf), context)
            showLog(Log.ERROR, exception.message)
        }

    }


    /**
     *
     * method to view pdf
     * @param pdfPath path of pdf
     * @param context Context instance
     */
    fun showPDF(pdfPath: String, context: Context) {
        val file = File(pdfPath)

        if (file.exists()) {
            val path = Uri.fromFile(file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(path, YMConstants.MIMEPDF)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast(context.getString(R.string.msg_no_application_available_to_view_pdf), context)
            }

        }
    }


    /**
     * Method to show short toast messages
     * @param content Toast message
     * @param context Context instance
     */
    fun showToast(content: String?, context: Context?): Toast? {
        if (null != content && null != context) {
            val toast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
            toast.show()
            return toast
        }
        return null
    }


    /**
     * Method to provide that logging is enable or disable
     * @param context Context instance
     * @return true/ false
     */
    private fun isLogingEnable(context: Context?): Boolean{
     return  SharedPrefsUtils.getBooleanPreference(context!!,YMConstants.LOGGING_ENABLE)
    }


 /*   *//**
     * Method to enable or disable logging
     * @param context Context instance
     * @param value to set
     * @return true/ false
     *//*
     fun setLoging( context: Context?,enable:Boolean): Boolean{
        return  SharedPrefsUtils.setBooleanPreference(context!!,YMConstants.LOGGING_ENABLE,enable)
    }


    *//**
     * Method to show long toast messages
     * @param content
     * @param context
     *//*
    fun showLongToast(content: String?, context: Context?) {
        if (null != content && null != context) {
            Toast.makeText(context, content, Toast.LENGTH_LONG).show()
        }
    }*/


    /**
     * Prints log to LogCat panel in Android LogCat
     *
     * @param logType
     * Type of Log.
     * Accepts Log.DEBUG, Log.ERROR, Log.INFO, Log.WARN, Log.ASSERT
     * and Log.VERBOSE
     * @param logMessage
     * message to display
     *
     */
    fun showLog(logType: Int, logMessage: String?) {
        if (YMConstants.IS_SHOW_LOG) {
            if (logMessage == null) {
                return
            }

            when (logType) {
                Log.DEBUG -> Log.d("YM Debug", logMessage)
                Log.ERROR -> Log.e("YM Error", logMessage)
                Log.INFO -> Log.i("YM Info", logMessage)
                Log.WARN -> Log.w("YM Warning", logMessage)
                else -> Log.v("YM message", logMessage)
            }
        }
    }


    /**
     * This method will navigate from current activity to target activity
     *
     * @param intent intent with all fields
     * @param activity Source activity
     */
    fun navigationMethod(intent: Intent, activity: Activity) {

        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.activity_in,
                R.anim.activity_out)
    }


    /**
     * This method will navigate from current activity to last activity
     *
     * @param activity
     * @param target
     */
    fun navigationBackMethod(activity: Activity) {
        activity.finish()
        activity.overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out)
    }




}