package com.ymsli.ymlibrary.login

import android.content.Context

interface LoginView {

    fun showProgress()
    fun hideProgress()
    fun setUserIdError()
    fun setPasswordError()
    fun navigateToNextScreen()
    fun getActivity(): Context
    fun showMessage(string: String?)
    fun enableComponent(enable: Boolean?)


}