package ymsli.com.couriemate.base

import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.R
import ymsli.com.couriemate.di.component.ActivityComponent
import ymsli.com.couriemate.di.component.DaggerActivityComponent
import ymsli.com.couriemate.di.module.ActivityModule
import ymsli.com.couriemate.views.login.LoginActivity
import androidx.work.WorkManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseActivity : This abstract class is the base Activity of all the activities
 *                  and contains common functionalities of all the activities
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    companion object {
        private const val GPS_REQUEST_CODE = 100
    }

    @Inject
    lateinit var viewModel: VM

    @Inject
    lateinit var locationManager: LocationManager

    private lateinit var toggle: ActionBarDrawerToggle

    protected var showSearchBar: Boolean = false

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildActivityComponent())
        super.onCreate(savedInstanceState)
        setContentView(provideLayoutId())
        setupObservers()
        setupView(savedInstanceState)
        viewModel.onCreate()
    }

    /**
     * used to create object of properties
     * annotated with @Inject
     * The feature is handled by Dagger2 framework
     */
    private fun buildActivityComponent() =
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as CourieMateApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()

    /**
     * contains all livedata observers.
     * these parameters are present in
     * BaseViewModel class
     */
    protected open fun setupObservers() {
        viewModel.messageString.observe(this, Observer {
            it?.data?.run { showMessage(this) }
        })

        viewModel.messageStringId.observe(this, Observer {
            it?.data?.run { showMessage(this) }
        })
        viewModel.logoutUser.observe(this, Observer {
            logoutUser()
        })
        viewModel.invalidInput.observe(this, Observer {
            showMessage(it)
        })

        viewModel.messageStringEventId.observe(this, Observer {
            it.getIfNotHandled()?.let {
                it.data?.run { showMessage(this) }
            }
        })

        viewModel.messageStringAPI.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showMessage(it)
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    /*
     * sets navigation drawer for activity that
     * inherits BaseActivity<*> and calls
     * below method
     */
    protected open fun setupNavigationDrawer(
        showSearchBar: Boolean,
        @StringRes title: Int,
        inputToolbar: androidx.appcompat.widget.Toolbar
    ) {
        this.showSearchBar = showSearchBar
        setSupportActionBar(inputToolbar)
        supportActionBar?.title = this.resources.getString(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (showSearchBar) menuInflater.inflate(R.menu.toolbar_actions, menu)
        return true
    }

    /**
     * logs out the user from the session &
     * erase all the locally stored information
     * specific to the user
     */
    private fun logoutUser() {
        viewModel.logoutUser()
        WorkManager.getInstance(this).cancelAllWork()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    /**
     * used to ack user of any validation, server response, internet connection, etc
     */
    fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)

    protected abstract fun setupView(savedInstanceState: Bundle?)

    /** tells if GPS is active or not */
    fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * asks user to turn on GPS of the device
     */
    fun buildAlertMessageGPS() {
        val locationSettingsBuilder: LocationSettingsRequest.Builder =
            LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create())
        locationSettingsBuilder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationSettingsBuilder.build())

        result.addOnCompleteListener { p0 ->
            try {
                p0.getResult(ApiException::class.java)
            } catch (excep: ApiException) {
                when ((excep).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvable: ResolvableApiException = excep as ResolvableApiException
                        resolvable.startResolutionForResult(this, GPS_REQUEST_CODE)
                    }
                }
            }
        }
    }

    /**
     * Displays a confirmation dialog containing a message and two actions buttons.
     * @param msg confirmation message to be displayed
     * @param labelAction label of 'POSITIVE' Action
     * @param actionCancel
     * @param actionOk
     */
    protected fun showConfirmationDialog(msg: String, option1Text: String,option2Text: String,
                                         actionCancel: () -> Unit, actionOk: () -> Unit){
        val (dialogView, btnOk, btnCancel) = inflateConfirmationDialogView(msg, option1Text,option2Text)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(dialogView)
            .create()
        btnCancel.setOnClickListener { actionCancel(); dialog.dismiss() }
        btnOk.setOnClickListener { dialog.dismiss(); actionOk() }
        dialog.show()
    }

    /**
     * Inflates the UI for a confirmation dialog, which contains message text and
     * two action buttons.
     * @param message confirmation message to display
     * @param labelOk label for the 'POSITIVE' action button
     * @return Triple containing dialog view as first, 'POSITIVE BTN' as second
     *         and 'CANCEL BTN' as third
     *
     */
    private fun inflateConfirmationDialogView(message: String, option1Text: String, option2Text: String)
            : Triple<View, Button, Button>{
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirmation, null, false)
        val tvMessage = dialogView.findViewById(R.id.tv_message) as TextView
        val option1 = dialogView.findViewById(R.id.btn_option1) as Button
        val option2 = dialogView.findViewById(R.id.btn_option2) as Button
        option1.text = option1Text
        option2.text = option2Text
        tvMessage.text = message
        return Triple(dialogView, option1, option2)
    }

    /**
     * sends SMS to specific number via common intent
     */
    fun sendSMS(message: String, mobileNumber: String?){
        if (!mobileNumber.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                val smsData = "smsto:${mobileNumber}"
                data = Uri.parse(smsData)
                putExtra("sms_body",message)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
            else{
                showMessage(this.resources.getString(R.string.app_not_found))
            }
        }
        else{
            showMessage(this.resources.getString(R.string.no_mobile_number))
        }
    }
}