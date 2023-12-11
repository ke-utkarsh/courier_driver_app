/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 11, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * DialogUtils : responsible for handling keyboard and alert dialog listeners
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.utils

import android.content.Context
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AlertDialog


object DialogUtils {
    /**
     * @param alertTitle title
     * @param alertMessage message
     * @param positiveButtonText positive button will show if positive button text is not null
     * @param negativeButtonText negative button will show if negative button text is not null
     * @param neutralButtonText  neutral button will show if neutral button text is not null
     * @param positiveButtonListener will provide on click response of poitive button
     * @param negativeButtonListener will provide on click response of negative button
     * @param neutralButtonListener will provide on click response of neutral button
     * @param context
     *
     * @return AlertDialog Created Dialog
     */
    fun showPrompt(alertTitle: String, alertMessage: String, positiveButtonText: String,
                   negativeButtonText: String?, neutralButtonText: String, positiveButtonListener: OnClickListener,
                   negativeButtonListener: OnClickListener, neutralButtonListener: OnClickListener, isCancelable: Boolean, context: Context): AlertDialog {
        val alert = AlertDialog.Builder(context)

        // Set Properties of alert dialog
        if (!StringUtils.isNullOrEmptyAfterTrim(alertTitle))
            alert.setTitle(alertTitle)
        if (!StringUtils.isNullOrEmptyAfterTrim(alertMessage))
            alert.setMessage(alertMessage)
        if (!StringUtils.isNullOrEmptyAfterTrim(positiveButtonText))
            alert.setPositiveButton(positiveButtonText, positiveButtonListener)
        if (!StringUtils.isNullOrEmptyAfterTrim(negativeButtonText))
            alert.setNegativeButton(negativeButtonText, negativeButtonListener)
        if (neutralButtonText != null)
            alert.setNeutralButton(neutralButtonText, neutralButtonListener)
        alert.setCancelable(isCancelable)

        val alertDialog = alert.show()
        return alertDialog
    }

    /**
     * @param alertTitle title
     * @param alertMessage message
     * @param positiveButtonText positive button will show if positive button text is not null
     * @param negativeButtonText negative button will show if negative button text is not null
     * @param positiveButtonListener will provide on click response of poitive button
     * @param negativeButtonListener will provide on click response of negative button
     * @param context
     * @return AlertDialog Created Dialog
     */
    fun showPrompt(alertTitle: String, alertMessage: String, positiveButtonText: String, negativeButtonText: String?, positiveButtonListener: OnClickListener,
                   negativeButtonListener: OnClickListener,
                   isCancelable: Boolean, context: Context): AlertDialog {
        val alert = AlertDialog.Builder(context)

        // Set Properties of alert dialog
        if (!StringUtils.isNullOrEmptyAfterTrim(alertTitle))
            alert.setTitle(alertTitle)
        if (!StringUtils.isNullOrEmptyAfterTrim(alertMessage))
            alert.setMessage(alertMessage)
        if (!StringUtils.isNullOrEmptyAfterTrim(positiveButtonText))
            alert.setPositiveButton(positiveButtonText, positiveButtonListener)

        if (!StringUtils.isNullOrEmptyAfterTrim(negativeButtonText))
            alert.setNegativeButton(negativeButtonText, negativeButtonListener)
        alert.setCancelable(isCancelable)

        val alertDialog = alert.show()
        return alertDialog
    }

    /**
     * @param alertTitle title
     * @param alertMessage message
     * @param positiveButtonText positive button will show if positive button text is not null
     * @param negativeButtonText negative button will show if negative button text is not null
     * @param positiveButtonListener will provide on click response of poitive button
     * @param context
     * @return AlertDialog Created Dialog
     */
    fun showPrompt(alertTitle: String, alertMessage: String, positiveButtonText: String, positiveButtonListener: OnClickListener,
                   isCancelable: Boolean, context: Context): AlertDialog {
        val alert = AlertDialog.Builder(context)

        // Set Properties of alert dialog
        if (!StringUtils.isNullOrEmptyAfterTrim(alertTitle))
            alert.setTitle(alertTitle)
        if (!StringUtils.isNullOrEmptyAfterTrim(alertMessage))
            alert.setMessage(alertMessage)
        if (!StringUtils.isNullOrEmptyAfterTrim(positiveButtonText))
            alert.setPositiveButton(positiveButtonText, positiveButtonListener)
        alert.setCancelable(isCancelable)

        val alertDialog = alert.show()
        return alertDialog
    }


}