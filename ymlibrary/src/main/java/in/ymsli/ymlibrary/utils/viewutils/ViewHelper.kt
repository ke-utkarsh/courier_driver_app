/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 31, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewHelper : Class to provide functionality to change view property
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.utils.viewutils

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.app.Activity
import android.view.LayoutInflater
import com.ymsli.ymlibrary.R


class ViewHelper {

    /**
     * set font to TextView from provided font id
     * @param view TextView
     * @param context Context object
     * @param fontId fontID
     */
  fun  setFont(view: TextView, context:Context, fontId:Int){
      view.typeface = ResourcesCompat.getFont(context,fontId)

  }

    /**
     * set font to EditText from provided font id
     * @param view TextView
     * @param context Context object
     * @param fontId fontID
     */
    fun  setFont(view: EditText, context:Context, fontId:Int){
        view.typeface = ResourcesCompat.getFont(context,fontId)
    }

    /**
     * set font to Button from provided font id
     * @param view TextView
     * @param context Context object
     * @param fontId fontID
     */
    fun  setFont(view: Button, context:Context, fontId:Int){
        view.typeface = ResourcesCompat.getFont(context,fontId)
    }


    /**
     * To get label with given text
     */
   fun getLayout(context:Context,message:String){
        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.label_layout, null)
        val  tv:TextView=view.findViewById(R.id.textView);
        tv.text = message
    }

}