package com.ymsli.ymlibrary.Synchronize

import android.content.Context


interface SynchronizeView {

    fun showSyncProgress()
    fun showSyncFailed(message: String)
    fun getActivity(): Context
    fun navigateToNext()
    fun showLogoutAlert()
    fun navigateToLast()
}