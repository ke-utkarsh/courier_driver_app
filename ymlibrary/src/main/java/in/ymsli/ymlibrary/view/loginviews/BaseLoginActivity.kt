package com.ymsli.ymlibrary.login

import android.content.Context
import android.os.Bundle
import android.text.Spanned
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ymsli.ymlibrary.R
import kotlinx.android.synthetic.main.footer_app_info.*
import kotlinx.android.synthetic.main.login_screen.*
import kotlinx.android.synthetic.main.logo_upper_content.*


abstract class BaseLoginActivity : AppCompatActivity(), LoginView {
    override fun getActivity(): Context {
        return this
    }

    var strUserId=null.toString()
    var strPassword=null.toString()

   override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

         setContentView(R.layout.login_screen)

        //set the listeners for activity buttons
        setListeners()
    }

    //set the textview for all the fields present in the screen
    open fun setFields(userName:String,password:String,appName:String,logo:Int,pageBackground:Int){
        editTextUserId.hint=userName
        editTextPassword.hint=password
        textViewLogo.text=appName
        imgViewYamahaLogo.setImageResource(logo)
        layoutLoginPage.setBackgroundResource(pageBackground)


    }

    open fun setVersionInfo(releaseNote:String,releaseVersion:String){
        textViewVersion.text=releaseVersion
        textViewReleaseNote.text=releaseNote
    }

     open fun setForgotPassword(message: Spanned){
         tv_forgot_password.visibility=View.VISIBLE
         tv_forgot_password.text=message
     }

     open fun setContactUsDetails(phoneNumberDetails: Spanned,emailDetails:Spanned){
         contact_us_number.text=phoneNumberDetails
         contact_us_email.text=emailDetails
     }



    open fun setListeners() {

        //call onLogin function when the button login is clicked
        buttonlogin.setOnClickListener { onLogin() }

        tv_forgot_password.setOnClickListener { actionForgotPassword() }
    }

     open fun actionForgotPassword(){

     }




     //function to be called when button login is clicked
     open fun onLogin() {

        //set the form fields into the string variables
        setDetails()

        //calls the login method of presenter with string of userid and  password
        //login(strUserId,strPassword)

    }

    //method to show the progress bar in the view
    override fun showProgress() {

        //set progressbar visibility to VISIBLE
        progressBarlogin!!.visibility= View.VISIBLE
    }

    override fun hideProgress() {

        //set progressbar visibility to GONE
        progressBarlogin!!.visibility= View.GONE
    }

    //set error message on the snackbar
    override fun setUserIdError() {
        Snackbar.make(layoutLoginPage,resources.getString(R.string.user_id_error), Snackbar.LENGTH_SHORT).show()
    }

    //set error message on the snackbar
    override fun setPasswordError() {
        Snackbar.make(layoutLoginPage,resources.getString(R.string.password_error), Snackbar.LENGTH_SHORT).show()
    }



    //s
    private fun setDetails() {

        strUserId=editTextUserId.text.toString().toUpperCase()
        strPassword=editTextPassword.text.toString()



    }


     /**
      * Method to enable and disable the UI components
      *
      * @param enable
      */
     override fun enableComponent(enable: Boolean?) {
         editTextUserId.setEnabled(enable!!)
         editTextPassword.setEnabled(enable)
         buttonlogin.setEnabled(enable)
     }


     //method to show message from the api
     override fun showMessage(message:String?) {
         Snackbar.make(layoutLoginPage, message.toString(), Snackbar.LENGTH_SHORT).show()
     }



}
