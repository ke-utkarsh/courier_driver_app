package com.ymsli.ymlibrary.Synchronize

import `in`.ymsli.ymlibrary.utils.DialogUtils
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ymsli.ymlibrary.R
import kotlinx.android.synthetic.main.synchronize_screen.*

/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Pavneet Singh
 * @date   July 12, 2018
 * Copyright (c) 2017, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseSynchronizeActivity : responsible for synchronizing data from api
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

abstract class BaseSynchronizeActivity : AppCompatActivity(),SynchronizeView {

    var syncFailedMessage=null.toString()
    var syncProgressMessage=null.toString()




    //set the generic view in the app
    open fun setFields(syncProgressMessage:String,syncFailedMessage:String,logo:Int){

        this.syncFailedMessage=syncFailedMessage
        this.syncProgressMessage=syncProgressMessage
        imgViewYamahaLogo.setImageResource(logo)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.synchronize_screen)

        //set the listeners for the action to be performed on button click
        setListeners()



    }

    open fun setListeners() {

        //call the actionLogout function when login is clicked
        buttonLogout.setOnClickListener { actionLogout() }

        //call the actionTryAgain function when try again button is clicked
        buttonSyncTryAgain.setOnClickListener { actionTryAgain() }
    }



    //shows the sync is failed on the view
    override fun showSyncFailed(message: String) {

        //set the sync failed message on the view
        textViewSync.setText(message)

        //set the progressbar gone from the view
        progressBarSync.setVisibility(View.GONE)

        //set the button TryAgain visible
        buttonSyncTryAgain.setVisibility(View.VISIBLE)

        //makes the button Logout visible
        buttonSyncTryAgain.setVisibility(View.VISIBLE)

        buttonLogout.visibility = View.VISIBLE
    }

    //shos the sync si in progress on the view
    override fun showSyncProgress() {

        //set the sync progress text
        textViewSync.setText(syncProgressMessage)

        //makes the progressbar visible
        progressBarSync.setVisibility(View.VISIBLE)

        //makes the tryAgain button gone from the view
        buttonSyncTryAgain.setVisibility(View.GONE)

        //makes the logout button gone from the view
        buttonLogout.setVisibility(View.GONE)

    }

    /**
     * Method to define the action of retry button
     *
     */
    abstract fun actionTryAgain()

    /**
     * Method to define the action of logout button
     *
     */
   abstract fun actionLogout()


    //get the instance of View to be used in the app
    override fun getActivity(): Context {
        return this
    }


    override fun showLogoutAlert() {

        //alertDialog to show ask the user if he wants to logout
        DialogUtils.showPrompt("","Do You want to Logout?","Yes","No", ""
                //action to be performed when ok is clicked
                , DialogInterface.OnClickListener { dialog, which ->
                actionLogoutOk()
            //action to be performed when no is clicked
        }, DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            //empty implementation because no neutral button
        }, DialogInterface.OnClickListener { dialog, which ->

        },true,this)
    }

    abstract fun actionLogoutOk()


}
