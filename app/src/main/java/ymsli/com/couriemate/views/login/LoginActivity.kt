package ymsli.com.couriemate.views.login

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseActivity
import ymsli.com.couriemate.di.component.ActivityComponent
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.services.SyncWorker
import ymsli.com.couriemate.views.main.MainActivity
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import ymsli.com.couriemate.utils.common.ViewUtils
import java.util.concurrent.TimeUnit

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * LoginActivity : This is the Login Activity in the couriemate android application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class LoginActivity : BaseActivity<LoginViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_login

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        btn_login.setOnClickListener {
            var countryCode = Constants.COUNTRY_CODE_UGANDA
            viewModel.doLogin(countryCode) }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loggingIn.observe(this, Observer {
            if (it) {
                progressBar_login_activity.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBar_login_activity.visibility = View.GONE
            }
        })

        input_login_userid.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onUsernameChange(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        input_login_password_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPasswordChange(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //error observers
        viewModel.invalidUsername.observe(this, Observer {
            input_login_userid.error = resources.getString(R.string.username_field_empty)
        })

        viewModel.invalidPassword.observe(this, Observer {
            input_login_password.error = resources.getString(R.string.password_field_small_length)
        })

        viewModel.loggedIn.observe(this, Observer {
            configureFCMInstance()
        })

        viewModel.appDataLoaded.observe(this, Observer {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar_login_activity.visibility = View.GONE
            startSyncWorkers()
            startNextActivity()
        })
    }

    /**
     * Configures the FCM instance to send FCM token to app server when token is refreshed.
     * it also configures the FCM instance for specific topics.
     */
    private fun configureFCMInstance(){
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) {
                instanceIdResult ->  viewModel.updateFCMToken(instanceIdResult.token)
        }
    }

    private fun startNextActivity(){
        val taskListIntent = Intent(this, MainActivity::class.java)
        startActivity(taskListIntent)
        finish()
    }

    /**
     * Configures and starts workers using periodic work request
     * for background syncing of local DB and remote DB.
     *
     * Once started these workers will keep running until the user logs out
     * or uninstalls the application.
     *
     * @author Balraj VE00YM023
     */
    private fun startSyncWorkers(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).cancelAllWork()
        WorkManager.getInstance(this).enqueue(syncRequest)
    }
}
