package ymsli.com.couriemate.views.main

import android.Manifest
import android.app.*
import ymsli.com.couriemate.BuildConfig
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseActivity
import ymsli.com.couriemate.di.component.ActivityComponent
import ymsli.com.couriemate.utils.braodcastreceivers.ConnectivityChangeReceiver
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.views.changepassword.ChangePasswordFragment
import ymsli.com.couriemate.views.login.LoginActivity
import ymsli.com.couriemate.views.notifications.NotificationListFragment
import ymsli.com.couriemate.views.notifications.NotificationListFragment.Companion.ACTIVE_NOTIFICATIONS_COUNT
import ymsli.com.couriemate.views.returntask.filter.ReturnTaskFilterDialog
import ymsli.com.couriemate.views.returntask.fragment.TabContainerReturnTaskList
import ymsli.com.couriemate.views.taskhistory.filter.TaskHistoryFilterDialog
import ymsli.com.couriemate.views.taskhistory.TaskHistoryFragment
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import ymsli.com.couriemate.views.tasklist.filter.TasksFilterDialog
import ymsli.com.couriemate.views.tasklist.fragment.AssignedTasksFragment
import ymsli.com.couriemate.views.tasklist.fragment.TabbedTaskListContainerFragment
import ymsli.com.couriemate.views.tasksummary.TaskSummaryFragment
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.VectorDrawable
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.WorkManager
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_side_drawer.view.*
import ymsli.com.couriemate.model.DeliveryExpenses
import ymsli.com.couriemate.utils.ConnectivityUtils
import ymsli.com.couriemate.utils.common.Event
import ymsli.com.couriemate.utils.common.ViewUtils
import ymsli.com.couriemate.utils.services.*
import ymsli.com.couriemate.utils.services.SharedPreferenceUtil
import ymsli.com.couriemate.views.chat.ChatFragment
import ymsli.com.couriemate.views.payment.PaymentRegistrationFragment
import ymsli.com.couriemate.views.taskhistory.filter.TaskHistoryFilterModel
import ymsli.com.couriemate.views.tasklist.filter.AssignedTasksFilterDialog
import ymsli.com.couriemate.views.transaction.TransactionHistoryFragment
import ymsli.com.couriemate.views.transaction.TransactionRegistrationFragment
import ymsli.com.couriemate.xmpp.ConnectionStateListener
import java.lang.Exception
import java.util.*
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
 * MainActivity : This is the Main Activity in the couriemate android application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class MainActivity : BaseActivity<TaskListViewModel>(),
    NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener,
    TasksFilterDialog.Actor,
    ReturnTaskFilterDialog.Actor,
    TaskHistoryFilterDialog.Actor,
    AssignedTasksFilterDialog.Actor,java.util.Observer{

    /** Following fields are used for the fused location provider. */
    private var locationRequest : LocationRequest? = null
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private var  foregroundLocationService: ForegroundLocationService?=null
    private var foregroundOnlyLocationServiceBound = false
    private var foregroundAndBackgroundLocationEnabled = false
    //shared pref parameter for foreground location service
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var geoCoder: Geocoder

    private val startForeService: MutableLiveData<Event<Boolean>> = MutableLiveData()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val NOTIFICATION_ID = 123456789
        private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_02"
        private const val CHAT_NOTIFICATION_INTENT = "CHAT_NOTIFICATION"
    }

    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.LocalBinder
            foregroundLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
            startForeService()
            //startTrip()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    private var locationCallback = object :LocationCallback(){
        override fun onLocationResult(result: LocationResult?) {}
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    /**
     * trigger trip creation
     */
    private fun startTrip(){
        val enabled = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ONLY_ENABLED, false)

        if (enabled) {
            foregroundLocationService?.stopTrackingLocation()
        } else {
            if(!isGPSEnabled()){
                buildAlertMessageGPS()
            }
            if (foregroundPermissionApproved()) {
                foregroundLocationService?.startTrackingLocation()
                    ?: Log.d("Trip Start", "Service Not Bound")
            } else {
                requestForegroundPermissions()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            val handler = Handler()
            handler.postDelayed({
                buildAlertMessageGPS()
            }, 200)
        }
    }

    /**
     * completes on going trip
     */
    private fun stopTrip(){
        foregroundLocationService?.stopTrackingLocation()
        when {
            foregroundAndBackgroundLocationEnabled -> stopForegroundAndBackgroundLocation()
        }
        viewModel.completeOnGoingTrip(geoCoder)
    }

    /**
     * used to run foreground service so as to
     * enable trip creation or connectivity in background
     */
    private fun triggerService(){
        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE) //service is binded but not started to track location and sensor data
    }

    private fun startForeService(){
        startTrip()
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
    }

    @Inject
    lateinit var alertDialogBuilder: AlertDialog.Builder

    private lateinit var alertDialog: AlertDialog

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var tracker: Tracker

    override fun provideLayoutId(): Int = R.layout.activity_main

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    /**
     * default selection handler of
     * navigation drawer
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        /** Clear the state of all the filters when navigation location changes */
        viewModel.messageString.value = null
        viewModel.messageStringId.value = null
        viewModel.taskHistoryFilterApplied.value = TaskHistoryFilterModel.getDefaultState()
        viewModel.taskHistoryFilterState.value = TaskHistoryFilterModel.getDefaultState()
        viewModel.filterApplied.value = FilterModel.getDefaultState()
        viewModel.filterState.value = FilterModel.getDefaultState()
        when (item.itemId) {
            R.id.nav_home -> {
                loadTaskListFragment()
            }
            R.id.nav_return_task -> {
                loadReturnTaskListFragment()
            }
            R.id.nav_call_operator -> {
                val companyContact = viewModel.getCompanyContactNumber()
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${companyContact}")
                if (callIntent.resolveActivity(packageManager) == null){
                    showMessage("No application found to perform this action.")
                }
                else{
                    startActivity(callIntent)
                }
            }
            R.id.nav_logout -> {
                alertDialogBuilder.setMessage(this.getString(R.string.CONFIRM_LOG_OUT))
                    .setCancelable(false)
                    .setPositiveButton(this.getString(R.string.ACTION_YES),
                        DialogInterface.OnClickListener { _, _ -> logoutUser() })
                    .setNegativeButton(this.getString(R.string.ACTION_NO),
                        DialogInterface.OnClickListener { _, _ -> alertDialog.cancel() })
                alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
            R.id.nav_change_password -> {
                loadChangePasswordFragment()
            }
            R.id.nav_task_summary -> {
                loadTaskSummaryFragment()
            }
            R.id.nav_task_history -> {
                loadTaskHistoryFragment()
            }
            R.id.nav_notifications -> {
                loadNotificationListFragment()
                resetActiveNotificationBadge()
            }
            R.id.nav_payment -> {
                loadPaymentRegistrationFragment()
            }
            R.id.nav_transaction_registration -> {
                loadTransactionRegistrationFragment()
            }
            R.id.nav_transaction_history ->{
                loadTransactionHistoryFragment()
            }
            R.id.nav_chat -> { loadChatFragment() }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun setupView(savedInstanceState: Bundle?) {
        ViewUtils.startTripConfigService(applicationContext)
        ViewUtils.startTransactionConfigService(applicationContext)
        ObservableObject.getInstance().addObserver(this)
        initXMPPManager()
        setupDrawer()
        cancelNotification()
        drawer_layout.closeDrawer(GravityCompat.START)
        loadTaskListFragment()
        val cmApp = application as CourieMateApplication
        tracker = cmApp.getDefaultTracker()!!
        tracker.send(HitBuilders.ScreenViewBuilder().build())
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        geoCoder = Geocoder(this, Locale.getDefault())
    }


    override fun onResume() {
        super.onResume()
        registerLocationUpdates()
    }
    /**
     * Register the fused location updates, so that we have a valid location
     * when user reaches the task details activity.
     *
     * Currently we request location updates every 10 minutes.
     *
     * @author Balraj VE00YM023
     */
    private fun registerLocationUpdates(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()?.apply {
            interval = Constants.LOCATION_REQUEST_INTERVAL
            fastestInterval = Constants.LOCATION_REQUEST_FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback, Looper.getMainLooper())
    }

    /**
     * logs out the user from the session &
     * erase all the locally stored information
     * specific to the user
     */
    private fun logoutUser() {
        stopLocationCaptureService()
        viewModel.logoutUser()
        WorkManager.getInstance(this).cancelAllWork()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    /**
     * we stop location capture service when user logs out.
     * @author Balraj VE00YM023
     */
    private fun stopLocationCaptureService(){
        viewModel.setTripStatusInSharedPrefs(false)
        val foregroundIntent = Intent(this,ForegroundLocationService::class.java)
        stopService(foregroundIntent)
    }

    /**
     * opens task list fragment
     */
    private fun loadTaskListFragment() {
        supportFragmentManager.findFragmentByTag(TabbedTaskListContainerFragment.TAG)
            ?: supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_layout_container, TabbedTaskListContainerFragment.newInstance(),
                    TabbedTaskListContainerFragment.TAG
                )
                .commitAllowingStateLoss()
        supportActionBar?.title = this.resources.getString(R.string.title_toolbar_task_list)
    }

    /**
     * opens return task list fragment
     */
    private fun loadReturnTaskListFragment() {
        supportFragmentManager.findFragmentByTag(TabContainerReturnTaskList.TAG)
            ?: supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_layout_container,
                    TabContainerReturnTaskList.newInstance(),
                    TabContainerReturnTaskList.TAG
                )
                .commitAllowingStateLoss()
        supportActionBar?.title = this.resources.getString(R.string.header_return_tasks_list)
    }

    /**
     * opens change password fragment
     */
    private fun loadChangePasswordFragment() {
        supportFragmentManager.findFragmentByTag(ChangePasswordFragment.TAG)
            ?: supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_layout_container,
                    ChangePasswordFragment.newInstance(),
                    ChangePasswordFragment.TAG
                )
                .commitAllowingStateLoss()
        supportActionBar?.title = this.resources.getString(R.string.change_password_title)
    }

    /**
     * opens task summary fragment
     */
    private fun loadTaskSummaryFragment() {
        supportFragmentManager.findFragmentByTag(TaskSummaryFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.frame_layout_container,
                TaskSummaryFragment.newInstance(),
                TaskSummaryFragment.TAG
            )
            .commitAllowingStateLoss()
        supportActionBar?.title = getString(R.string.title_task_summary)
    }

    /**
     * opens tasks history fragment
     */
    private fun loadTaskHistoryFragment() {
        supportFragmentManager.findFragmentByTag(TaskHistoryFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.frame_layout_container, TaskHistoryFragment.newInstance(),
                TaskHistoryFragment.TAG
            )
            .commitAllowingStateLoss()
        supportActionBar?.title = getString(R.string.title_task_history)
    }

    /**
     * opens notifications fragment
     */
    private fun loadNotificationListFragment() {

        var activeNotificationCount = viewModel.getActiveNotificationCount()
        var bundle = Bundle()
        bundle.putInt(ACTIVE_NOTIFICATIONS_COUNT, activeNotificationCount?.toInt()?:0)
        var fragmentInstance = NotificationListFragment.newInstance()
        fragmentInstance.arguments = bundle
        supportFragmentManager.findFragmentByTag(NotificationListFragment.TAG)
            ?: supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frame_layout_container,
                    fragmentInstance,
                    NotificationListFragment.TAG)
                .commitAllowingStateLoss()
        supportActionBar?.title = getString(R.string.title_notifications)
}

    /**
     * opens payment registration fragment
     */
    fun loadPaymentRegistrationFragment() {
        supportFragmentManager.findFragmentByTag(PaymentRegistrationFragment.TAG)
            ?: supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_layout_container, PaymentRegistrationFragment.newInstance(),
                        PaymentRegistrationFragment.TAG
                )
                .commitAllowingStateLoss()
        supportActionBar?.title = getString(R.string.payment_reg)
    }

    /**
     * opens payment registration fragment
     */
    fun loadTransactionRegistrationFragment(selectedTransaction: DeliveryExpenses?=null) {
        if(selectedTransaction!=null){
            supportFragmentManager.findFragmentByTag(TransactionRegistrationFragment.TAG)
                ?: supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.frame_layout_container, TransactionRegistrationFragment.newInstance(selectedTransaction),
                        TransactionRegistrationFragment.TAG
                    )
                    .commitAllowingStateLoss()
        }
        else{
            TransactionRegistrationFragment.selectedTransaction = null
            TransactionRegistrationFragment.isUpdate = false
                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.frame_layout_container, TransactionRegistrationFragment.newInstance(),
                        TransactionRegistrationFragment.TAG
                    )
                    .commitAllowingStateLoss()
        }
        supportActionBar?.title = getString(R.string.transaction_reg)
    }

    /**
     * opens transaction history fragment
     */
    fun loadTransactionHistoryFragment() {
        supportFragmentManager.findFragmentByTag(TransactionHistoryFragment.TAG)
                ?: supportFragmentManager
                        .beginTransaction()
                        .replace(
                                R.id.frame_layout_container, TransactionHistoryFragment.newInstance(),
                                TransactionHistoryFragment.TAG
                        )
                        .commitAllowingStateLoss()
        supportActionBar?.title = getString(R.string.transaction_history)
    }

    /**
     * setups navigation drawer in the application UI
     */
    private fun setupDrawer() {
        setupNavigationMenu()
        setDrawerHeaderText()
        setApiVersionAndAppVersion()
        setSupportActionBar(action_bar_top)
        action_bar_top.setNavigationIcon(R.drawable.ic_dehaze_black_24dp)
        toggle = object: ActionBarDrawerToggle(
            this, drawer_layout, action_bar_top,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                setActiveNotificationsBadge()
                hideKeyboard()
            }
        }
        drawer_layout.addDrawerListener(toggle)
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.isUploadSuccessful.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it) {
                    loadTaskListFragment()
                }
            }
        })

        viewModel.startTrip.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it){
                    //start trip here
                    viewModel.generateAndSaveNewTripId()
                    viewModel.setTripStatusInSharedPrefs(true)
                    startTrip()
                }
            }
        })

        viewModel.transactionHistoryItemSelected.observe(this, Observer {
            if(it!=null){
                //open TransactionRegistrationFragment with selected item
                loadTransactionRegistrationFragment(it)
            }
        })

        viewModel.getChatNotification().observe(this, Observer {
            if(!viewModel.chatFragmentLoaded && it.isNotEmpty()){
                showNotification("You have received ${it.size} new ${if(it.size == 1) "message" else "messages"}")
            }
            else if(it.isNotEmpty()){
                viewModel.deleteNotification()
            }
        })
    }

    /**
     * puts driver name and contact in the
     * header of the navigation drawer
     */
    private fun setDrawerHeaderText() {
        val header = nav_view.getHeaderView(0)
        header.text_driver_name_drawer_header.text = viewModel.getUserDisplayName()
        header.text_driver_phone_drawer_header.text = viewModel.getUserContact()
    }

    /**
     * Sets the APK and API version info on the bottom of navigation drawer.
     */
    private fun setApiVersionAndAppVersion() {
        var appVersionItem = nav_view.menu.findItem(R.id.nav_item_app_version)
        var apiVersionItem = nav_view.menu.findItem(R.id.nav_item_api_version)

        var appVersionText = SpannableString(Constants.APK_VERSION_PREFIX + BuildConfig.VERSION_NAME)
        appVersionText.setSpan(ForegroundColorSpan(Color.parseColor("#AAAAAA")), 0, appVersionText.length, 0)
        appVersionItem.title = appVersionText

        var apiVersionText = SpannableString(Constants.API_VERSION_PREFIX + viewModel.getApiVersion())
        apiVersionText.setSpan(ForegroundColorSpan(Color.parseColor("#AAAAAA")), 0, apiVersionText.length, 0)
        apiVersionItem.title = apiVersionText
    }

    /**
     * Show menu items depending on the type of user.
     * Hide following items for "Direct Driver"
     *      1. Return Task's
     *      2. Payments
     */
    private fun setupNavigationMenu() {
        if (viewModel.getUserType() == getString(R.string.user_type_direct_driver)) {
            nav_view.menu.findItem(R.id.nav_return_task)?.isVisible = true
            nav_view.menu.findItem(R.id.nav_payment)?.isVisible = true
            nav_view.menu.findItem(R.id.nav_notifications)?.isVisible = true
        }
        else{
            setActiveNotificationsBadge()
        }
    }


    private lateinit var tempTitle: String
    /**
     * called when multiple assigned tasks are selected to be put
     * into delivering status
     */
    fun setupToolbarForInteractiveModeOn(interactiveModeTitle: String) {
        setSupportActionBar(action_bar_top)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_black_24)
        tempTitle = supportActionBar?.title.toString()
        supportActionBar?.title = interactiveModeTitle
    }

    /**
     * called when user returns from multiple selection mode of assigned tasks
     */
    fun setupToolbarForInteractiveModeOff() {
        setupDrawer()
        supportActionBar?.title = tempTitle
    }

    private lateinit var connectivityChangeReceiver: ConnectivityChangeReceiver

    override fun onStart() {
        super.onStart()
        connectivityChangeReceiver = ConnectivityChangeReceiver()
        registerReceiver(
            connectivityChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        registerNetworkChangeManagerForXMPP()

        startForeService.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it){
                    triggerService()
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityChangeReceiver)
        unregisterNetworkChangeManagerForXMPP()
    }

    private var isBackPressed: Boolean = false

    override fun onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed()
            return
        }
        try{
            val isVisible = supportFragmentManager.findFragmentByTag(TransactionRegistrationFragment.TAG)?.isVisible?:false
            if(isVisible && TransactionRegistrationFragment.isUpdate){
                // open TransactionHistoryFragment
                loadTransactionHistoryFragment()
                return
            }
        }
        catch (ex: Exception){

        }
        /** check if assigned task's list is in multi selection mode
         *  if multi selection mode is active then turn it off*/
        val parent = supportFragmentManager.findFragmentByTag(TabbedTaskListContainerFragment.TAG)
        val assignedTasks =
            parent?.childFragmentManager?.findFragmentByTag(AssignedTasksFragment.TAG) as? AssignedTasksFragment

        if ((assignedTasks != null) && (assignedTasks.isVisible)) {
            if (assignedTasks.onBackPressed()) return
        }
        isBackPressed = true
        showMessage(R.string.back_exit_confirmation)
        Handler().postDelayed({ isBackPressed = false }, 2500)
    }


    /**
     * Called by the TaskListFilterDialog (OngoingTasks, DoneTasks),
     * when filter is applied. this function posts the filter predicate
     * to live data, and any observer fragment (OngoingTasks or DoneTasks)
     * will update its UI based on new filter information.
     *
     * @author Balraj VE00YM023
     */
    override fun applyFilter(predicate: FilterModel) {
        viewModel.filterApplied.postValue(predicate)
    }

    /**
     * Called by the TaskListFilterDialog when it start to build its UI.
     * previous state is required in order to build the UI according to previous state.
     * For example highlighting previously selected buttons etc.
     *
     * @author Balraj VE00YM023
     */
    override fun getPreviousState(): FilterModel {
        return viewModel.filterState.value ?: FilterModel.getDefaultState()
    }

    /**
     * Called by the ReturnTaskFilterDialog (Delivered, Returned),
     * when filter is applied. this function posts the filter predicate
     * to live data, and any observer fragment (Delivered, Returned)
     * will update its UI based on new filter information.
     *
     * @author Balraj VE00YM023
     */

    override fun applyReturnFilter(predicate: FilterModel) {
        viewModel.filterApplied.postValue(predicate)
    }


    /**
     * Called by the ReturnListFilterDialog when it start to build its UI.
     * previous state is required in order to build the UI according to previous state.
     * For example highlighting previously selected buttons etc.
     *
     * @author Balraj VE00YM023
     */

    override fun getReturnPreviousState(): FilterModel {
        return viewModel.filterState.value ?: FilterModel.getDefaultState()
    }


    override fun applyAssignedTasksFilter(predicate: FilterModel) {
        viewModel.filterApplied.postValue(predicate)
    }

    override fun getAssignedTasksFilterPreviousState(): FilterModel {
        return viewModel.filterState.value ?: FilterModel.getDefaultState()
    }

    override fun getCompanyAddress() = viewModel.getCompanyAddress() ?: "No address"



    /**
     * displays count of notifications in navigation drawer
     */
    private fun setActiveNotificationsBadge(){
        viewModel.getActiveNotificationCount()?.let{
            if(it.toInt() > 0){
                var notificationMenuItem = nav_view.menu.findItem(R.id.nav_notifications)
                (notificationMenuItem.icon as VectorDrawable).setTint(Color.GREEN)
                var badgeView = notificationMenuItem.actionView as TextView
                badgeView.setPadding(0,30,5,0)
                badgeView.setTextColor(Color.BLUE)
                badgeView.setTypeface(badgeView.typeface, Typeface.BOLD)
                badgeView.text = it
            }
        }
    }

    /**
     * reset count of notifications in navigation drawer
     */
    private fun resetActiveNotificationBadge(){
        var notificationMenuItem = nav_view.menu.findItem(R.id.nav_notifications)
        (notificationMenuItem.icon as VectorDrawable).setTint(Color.BLACK)
        var badgeView = notificationMenuItem.actionView as TextView
        badgeView.text = Constants.EMPTY_STRING
    }

    /**
     * cancels notifications
     */
    fun cancelNotification() {
        val ns: String = Context.NOTIFICATION_SERVICE
        val nMgr = getSystemService(ns) as NotificationManager
        nMgr.cancelAll()
    }

    override fun applyTaskHistoryFilter(predicate: TaskHistoryFilterModel) {
        viewModel.taskHistoryFilterApplied.postValue(predicate)
    }

    override fun getTaskHistoryFilterPreviousState(): TaskHistoryFilterModel {
        return viewModel.taskHistoryFilterState.value ?: TaskHistoryFilterModel.getDefaultState()
    }

    /**
     * check permission of access fine location
     */
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun stopForegroundAndBackgroundLocation() {
        Log.d(TAG, "stopForegroundAndBackgroundLocation()")
        foregroundAndBackgroundLocationEnabled = false
    }

    /**
     * Loads the chat fragment in the current activity's fragment container.
     * @author Balraj VE00YM023
     */
    private fun loadChatFragment(){
        supportFragmentManager.findFragmentByTag(ChatFragment.TAG) ?:
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_container, ChatFragment.newInstance(), ChatFragment.TAG)
                .commitAllowingStateLoss()
        supportActionBar?.title = getString(R.string.header_chat)
    }

    override fun update(o: Observable?, arg: Any?) {
        if((arg as Intent).extras?.get("CONFIG_RECEIVED")!=null){
            val isReceived = arg.getBooleanExtra("CONFIG_RECEIVED",false)
            // start Foreground service here
            if(isReceived){
                startForeService.postValue(Event(true))
                //triggerService()
            }
        }

    }

    /*********************** FUNCTIONS RELATED TO XMPP CHAT FUNCTIONALITY *************************/
    inner class NetworkChangeManager: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(ConnectivityUtils.isConnected(this@MainActivity)){
                viewModel.reConnectXMPP()
            }
        }
    }

    private lateinit var networkChangeManager: NetworkChangeManager
    private fun registerNetworkChangeManagerForXMPP(){
        networkChangeManager = NetworkChangeManager()
        registerReceiver(networkChangeManager, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun unregisterNetworkChangeManagerForXMPP(){
        unregisterReceiver(networkChangeManager)
    }

    private val xmppConStateListener = object: ConnectionStateListener{
        override fun onAuthResponse(status: Boolean) {
            Log.d("XMPP_CHAT", "Auth response received : $status")
        }
    }

    private fun initXMPPManager() { viewModel.initXMPPManager(xmppConStateListener) }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) { view = View(this) }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Handle the pending intent passed in case of chat notification.
     * load the chat fragment in that case.
     * @author Balraj VE00YM023
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val intentFromChatNotification = intent?.getBooleanExtra(CHAT_NOTIFICATION_INTENT, false)?:false
        if(intentFromChatNotification){ onNavigationItemSelected(nav_view.menu.findItem(R.id.nav_chat)) }
    }

    private fun showNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val titleText = "New Message"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(message)
            .setBigContentTitle(titleText)
        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(CHAT_NOTIFICATION_INTENT, true)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(message)
            .setSmallIcon(R.mipmap.couriemate_launcher_round)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}