/**

 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 31, 2018
 * Description

 * -----------------------------------------------------------------------------------

 * KeyBoardHandle Class :  Class provides method to handle eyboard visibility

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description

 * -----------------------------------------------------------------------------------

 */
package `in`.ymsli.ymlibrary.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

object KeyBoardHandler {


    /**
     * @param context
     * @param view pass view which is currently focused
     */
    fun hideKeyboard(context: Context, view: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != view && null != view.windowToken)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        else
            (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        //Clear focus to avoid showing keyboard again
        view!!.clearFocus()
    }

    /**
     * Hide soft keyboard
     * @param context Activity context
     */
    fun hideKeyboard(context: Context) {
        val activity = context as Activity
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        // Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        // Clear focus to avoid showing keyboard again
        view.clearFocus()
    }

}