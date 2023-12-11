package ymsli.com.couriemate.views.taskdetail

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_order_detail.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.adapters.OrderItemAdapter
import ymsli.com.couriemate.base.BaseActivity
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.di.component.ActivityComponent
import ymsli.com.couriemate.utils.braodcastreceivers.ConnectivityChangeReceiver
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.TaskStatus
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.views.taskdetail.taskdetailfragments.*
import java.sql.Timestamp
import java.text.DecimalFormat
import java.util.*


/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TaskDetailActivity : This is the Task Detail activity of the application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class TaskDetailActivity: BaseActivity<TaskDetailViewModel>() {

    private lateinit var selectedTask: TaskRetrievalResponse
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var connectivityChangeReceiver: ConnectivityChangeReceiver
    lateinit var orderItemAdapter: OrderItemAdapter

    override fun provideLayoutId(): Int = R.layout.activity_order_detail

    var isDropdown = true

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        selectedTask = (intent.getSerializableExtra("SelectedTask") as TaskRetrievalResponse)
        viewModel.taskDetail.value = selectedTask
        setupNavigationDrawer(false,R.string.header_order_detail, orderdetailtoolbar)

        /** Fetch the last location, this should already be set because we have
         *  registered the location updates in the MainActivity. */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            viewModel.latitude.value  = location?.latitude
            viewModel.longitude.value = location?.longitude
        }

        map.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=" + selectedTask.dropAddress + "&mode=l")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) == null){
                showMessage("No application found to perform this action.")
            }
            else{
                startActivity(mapIntent)
            }
        }

        phoneNo.setOnClickListener {
            //open phone app for user to contact receiver
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${(viewModel.taskDetail.value as TaskRetrievalResponse).receiverMobile}"))
            if (callIntent.resolveActivity(packageManager) == null){
                showMessage("No application found to perform this action.")
            }
            else{
                startActivity(callIntent)
            }
        }
        tvItemDetails.setOnClickListener {
            if(isDropdown)
            {
                dropdown.visibility =View.GONE
                dropup.visibility =View.VISIBLE
                constraintOrderItem.visibility =View.VISIBLE
                recylverViewOrderID.visibility = View.VISIBLE
                isDropdown=false
            }else{
                dropdown.visibility =View.VISIBLE
                dropup.visibility =View.GONE
                constraintOrderItem.visibility =View.GONE
                recylverViewOrderID.visibility = View.GONE
                isDropdown=true
            }
        }
        setAmountView()
        setActivityState()
        disableFailButton()
    }


    override fun onStart() {
        super.onStart()
        connectivityChangeReceiver = ConnectivityChangeReceiver()
        registerReceiver(
            connectivityChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityChangeReceiver)
    }

    /**
     * if tasks exceeeds permitted failure limit, fail button
     * is disabled
     */
    private fun disableFailButton(){
        if(selectedTask.noOfRetries == selectedTask.failureCount) {
            fail.isEnabled = false
            fail.setTextColor(this.getColor(R.color.colorGrey))
            fail.background = this.getDrawable(R.drawable.fail_disabled)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        // assign view with corresponding object parameters
        viewModel.taskDetail.observe(this, Observer {
            task_id.text = it.orderNo.toString()
            receiverName.text = viewModel.taskDetail.value?.receiverName.toString()
            phoneNo.text = viewModel.taskDetail.value?.receiverMobile.toString()
            address.text = viewModel.taskDetail.value?.pickupLocation.toString()
            tv_task_detail_drop.text = viewModel.taskDetail.value?.dropAddress.toString()
            setupOrderMemoInput()
            val quantityValue = if (it.quantity == null) Constants.HYPHEN else it.quantity.toString()
            quantity.text = getString(R.string.Quanity, quantityValue)

            Log.i("Order",viewModel.taskDetail.value?.items.toString())

            val taskStatus = TaskStatus.getEnumById(it.taskStatusId!!)
            status.text = taskStatus!!.name
            status.setTextColor(Color.parseColor(taskStatus.color))
            setFinalDestination(selectedTask)

            val adapter = OrderItemAdapter(viewModel.taskDetail.value!!.items!!)
            recylverViewOrderID.adapter = adapter
            adapter.notifyDataSetChanged()

            when(taskStatus.statusId){
                2 -> {
                    task_date.text = "Assigned date ${Utils.formatDate(selectedTask.assignedDate.toString())}"
                }
                3 -> {
                    task_date.text = "Delivering date ${Utils.formatDate(selectedTask.startDate.toString())}"
                }
                4 -> {
                    task_date.text = "Delivered date ${Utils.formatDate(selectedTask.endDate.toString())}"
                }
                5 -> {
                    task_date.text = "Refused date ${Utils.formatDate(selectedTask.endDate.toString())}"
                }
                7 -> {
                    task_date.text = "Failed date ${Utils.formatDate(selectedTask.startDate.toString())}"
                }
                8 -> {
                    task_date.text = "Returned date ${Utils.formatDate(selectedTask.endDate.toString())}"
                }
            }
            val statusIcon = findViewById<View>(R.id.stausIcon)
            val circle = statusIcon.background.mutate() as GradientDrawable
            circle.setTint(Color.parseColor(taskStatus.color))

            /** Disable user interaction if task status has changed to something other
             *  than delivering.
             */
            if (((it.taskStatusId != TaskStatus.DELIVERING.statusId)&&((it.taskStatusId != TaskStatus.FAILED.statusId)))){
                disableButtons()
            }
        })

        viewModel.showDatePicker.observe(this, Observer {
            showDatePickerDialog()
        })

        viewModel.showProgressBar.observe(this, Observer {
            if(it) progress_bar.visibility = View.VISIBLE
            else progress_bar.visibility = View.GONE
        })

        viewModel.disableButtons.observe(this, Observer {
            disableButtons()
        })

        viewModel.failCarrierTaskUIHide.observe(this, Observer {
            disableButtonsForCarrieDelivery()
        })

        /**
         * Observer to disable the order memo input field,
         * this field is disabled when any update operation(deliver, fail, refuse) completes
         *
         * @author Balraj VE00YM023
         */
        viewModel.disableOrderMemo.observe(this, Observer {
            input_task_detail_order_memo.isEnabled = false
            input_task_detail_order_memo.background = getDrawable(R.drawable.bg_border_less)
        })

        viewModel.smsBodyLiveData.observe(this, Observer {
            sendSMS(it,selectedTask.receiverMobile)
        })



    }

    /**
     * This function configures the order order memo input field.
     * it sets the correct value and the background on input.
     * it also configures the text change listener to post the text to live data,
     * so that the updated text can be used on the time of update.
     *
     * @author Balraj VE00YM023
     */
    private fun setupOrderMemoInput(){
        input_task_detail_order_memo.setText(viewModel.taskDetail.value?.orderMemo)
        when{
            viewModel.disableOrderMemo.value == true ->{
                input_task_detail_order_memo.isEnabled = false
                input_task_detail_order_memo.background = getDrawable(R.drawable.bg_border_less)
            }
            selectedTask.taskStatusId == TaskStatus.DELIVERING.statusId ||
            selectedTask.taskStatusId == TaskStatus.FAILED.statusId -> {
                input_task_detail_order_memo.isEnabled = true
                input_task_detail_order_memo.background = getDrawable(R.drawable.border)
            }
            else -> {
                input_task_detail_order_memo.isEnabled = false
                input_task_detail_order_memo.background = getDrawable(R.drawable.bg_border_less)
            }
        }

        /**
         * When value of order memo changes, post it to live data, so that it can be used
         * when performing update.
         * @author Balraj VE00YM023
         */
        input_task_detail_order_memo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { viewModel.orderMemo.value = s?.trim().toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * renders appropriate fragments on the activity
     * based on task status, sequence, etc.
     */
    private fun addAppropriateFragments(){
        val task = viewModel.taskDetail.value
        //if(!(task?.failureCount == task!!.noOfRetries)) {
            val count = task?.maxTaskSequence
            when (count) {
                1 -> deliverToEndCustomer()
                3 -> {
                    when (task.taskSequenceNo) {
                        1 -> {
                            // deliver to carrier
                            refuse.visibility = View.GONE
                            addCarrierDeliveryFragment()
                            deliver.setOnClickListener { addCarrierDeliveryFragment() }
                            fail.setOnClickListener { addCarrierFailureFragment() }
                        }
                        3 -> deliverToEndCustomer()
                    }
                }
            }
        //}
    }

    /**
     * used to remove redundant code in when statements
     * Signifies that order is meant for end customer
     */
    private fun deliverToEndCustomer(){
        addDeliveryFragment() //initialize DeliveryFragment upon opening the activity by default
        refuse.setOnClickListener {
            addRefuseFragment()
        }
        fail.setOnClickListener { addFailFragment() }
        deliver.setOnClickListener { addDeliveryFragment() }
    }

    /**
     * adds appropriate fragment wherein
     * delivery fails for carrier
     */
    private fun addCarrierFailureFragment(){
        fail.background = this.getDrawable(R.drawable.failonclick)
        toggleDeliverFailButtonColor(fail,deliver)
        supportFragmentManager.findFragmentByTag(FailCarrierFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,FailCarrierFragment.newInstance(),FailCarrierFragment.TAG)
            .commitAllowingStateLoss()
    }

    /**
     * adds appropriate fragment when item is
     * to be delivered to carrier
     */
    private fun addCarrierDeliveryFragment(){
        toggleDeliverFailButtonColor(deliver,fail)
        supportFragmentManager.findFragmentByTag(CarrierDeliveryFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,CarrierDeliveryFragment.newInstance(),CarrierDeliveryFragment.TAG)
            .commitAllowingStateLoss()
    }

    /**
     * default fragment where user
     * can mark the task as delivered
     */
    private fun addDeliveryFragment(){
        toggleButtonColor(deliver,refuse,fail)
        val fragmentInstance = DeliverFragment.newInstance()
        fragmentInstance.arguments = Bundle().apply {
            /** When Cod Amount field is hidden, we need to hide the inputs in the Deliver fragment.
             *  we pass value of this field to Deliver fragment. */
            var hideCodAmountInputs = cod_value.visibility == View.GONE
            putBoolean(DeliverFragment.HIDE_COD_INPUT, hideCodAmountInputs)
        }
        supportFragmentManager.findFragmentByTag(DeliverFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragmentInstance, DeliverFragment.TAG)
            .commitAllowingStateLoss()
    }

    /**
     * adds refuse fragment where user
     * can mark delivery as refused by receiver
     */
    private fun addRefuseFragment(){
        toggleButtonColor(refuse,deliver,fail)
        supportFragmentManager.findFragmentByTag(RefuseFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, RefuseFragment.newInstance(), RefuseFragment.TAG)
            .commitAllowingStateLoss()
    }

    /**
     * adds failure fragment where user
     * can mark delivery as failed
     */
    private fun addFailFragment(){
        toggleButtonColor(fail,deliver,refuse)
        supportFragmentManager.findFragmentByTag(FailCustomerFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, FailCustomerFragment.newInstance(), FailCustomerFragment.TAG)
            .commitAllowingStateLoss()
    }

    /**
     * use to toggle 2 button UI/color
     * 2 button are present if
     * it is not end delivery
     */
    private fun toggleDeliverFailButtonColor(b1:Button,b2:Button){
        b1.background = this.getDrawable(R.drawable.failonclick)
        b1.setTextColor(Color.parseColor("#ffffff"))
        b2.background = this.getDrawable(R.drawable.deliver)
        b2.setTextColor(Color.parseColor("#000000"))
    }

    /**
     * use to toggle 3 button UI/color
     */
    private fun toggleButtonColor(b1:Button,b2:Button,b3:Button){
        b1.background = this.getDrawable(R.drawable.failonclick)
        b1.setTextColor(Color.parseColor("#ffffff"))
        b2.background = this.getDrawable(R.drawable.deliver)
        b2.setTextColor(Color.parseColor("#000000"))
        b3.background = this.getDrawable(R.drawable.refuse)
        b3.setTextColor(Color.parseColor("#000000"))
        disableFailButton()
    }
    /**
     * shows the date picker
     * on the activity for failed
     * delivery
     * This is used in FailCustomerFragment
     */
    private fun showDatePickerDialog(){
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePickerDialog(cal)
            },
            mYear,mMonth,mDay
        )
        datePickerDialog.show()
        datePickerDialog.datePicker.minDate = (System.currentTimeMillis()-1000)
    }

    /**
     * shows the time picker
     * on the activity for failed
     * delivery
     * This is used in FailCustomerFragment
     */
    private fun showTimePickerDialog(calender : Calendar){
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener {_, hourOfDay, minute ->
                calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calender.set(Calendar.MINUTE, minute)
                viewModel.nextDeliveryDateTime.postValue(Timestamp(calender.timeInMillis))
            },
            mHour,mMinute, false
        )
        timePickerDialog.show()
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //check if task is in done state
        val doneTaskIds = listOf(TaskStatus.DELIVERED.statusId, TaskStatus.RETURNED.statusId, TaskStatus.REFUSED.statusId)
        if(doneTaskIds.contains(selectedTask.taskStatusId)){
            // remove icons from toolbar
            menuInflater.inflate(R.menu.menu_done_task, menu)
        }
        else {
            //show icons from toolbar
            menuInflater.inflate(R.menu.order_detail_actions_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_call -> {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${selectedTask.receiverMobile}")
                if (callIntent.resolveActivity(packageManager) == null){
                    showMessage("No application found to perform this action.")
                    }
                    else{
                        startActivity(callIntent)
                    }
                }

                R.id.action_message -> {
                    //show dialog so that user can select
                    // options available in the dialog
                    //showMessageSelectionDialog()
                    sendSMS(viewModel.getUnreachableCustomerString()?:resources.getString(R.string.delivery_sms_receiver_not_reachable),selectedTask.receiverMobile)
            }

            R.id.action_location -> {
                sendLocationShareSMSToCustomer()
            }

            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * Sets the activity status to interactive or non-interactive based on the
     * state of selected task.
     */
    private fun setActivityState(){
        if (((selectedTask.taskStatusId != TaskStatus.DELIVERING.statusId)&&(selectedTask.taskStatusId != TaskStatus.FAILED.statusId))){
            disableButtons()
        }
        else{
            addAppropriateFragments()
        }
    }

    /**
     * if task is not in delivering status
     * disalbe/hide respective buttons
     * for agent driver
     */
    private fun disableButtons(){
        deliver.visibility = View.GONE
        refuse.visibility = View.GONE
        fail.visibility = View.GONE
        card_view2.visibility = View.GONE
    }

    /**
     * if task is not in delivering status
     * disalbe/hide respective buttons
     * for carrier driver
     */
    private fun disableButtonsForCarrieDelivery(){
        deliver.visibility = View.GONE
        fail.visibility = View.GONE
        card_view2.visibility = View.GONE
    }

    /**
     * responsible for setting
     * total amount, cod amount
     * & total amount
     */
    private fun setAmountView(){
        setCodAmount(selectedTask)
        setDeliveryFee(selectedTask)
        setPriceDetailSectionVisibility()
    }


    /**
     * Sets the visibility and the value of final destination field.
     * final destination field is only visible if order has 3 tasks
     * and current task is first task. meaning this task is being delivered to carrier.
     *
     * @author Balraj VE00YM023
     */
    private fun setFinalDestination(selectedTask: TaskRetrievalResponse){
        if(selectedTask.taskSequenceNo != selectedTask.maxTaskSequence){
            label_task_detail_final_destination.visibility = View.VISIBLE
            tv_order_task_final_destination.visibility = View.VISIBLE
            tv_order_task_final_destination.text = selectedTask.receiverDistrict
        }
    }

    /**
     * Set the value as well as the visibility of the delivery fee fields.
     * @author Balraj VE00YM023
     */
    private fun setDeliveryFee(task: TaskRetrievalResponse){
        when(task.maxTaskSequence){
            1 -> setDeliveryFeeForSingleTaskOrder(task)
            else -> setDeliveryFeeForThreeTasksOrder(task)
        }
    }

    /**
     * This function is used to sets the value as well as visibility of the delivery fee
     * field for the orders having only one task.
     *
     * @author Balraj VE00YM023
     */
    private fun setDeliveryFeeForSingleTaskOrder(task: TaskRetrievalResponse){
        if((task.orderTypeId == 1) &&
            ((task.deliveryFeePaidBy == "Sender" && task.taskStatusId == TaskStatus.ASSIGNED.statusId) ||
            (task.deliveryFeePaidBy == "Receiver" && task.taskStatusId == TaskStatus.DELIVERING.statusId))){
            delivery_fee_label.visibility = View.VISIBLE
            delivery_fee_value.visibility = View.VISIBLE
            delivery_fee_paid_label.visibility = View.VISIBLE
            delivery_fee_paid_label.text = "(Paid By ${selectedTask.deliveryFeePaidBy})"
            delivery_fee_value.text = task.deliveryFeeAmount.formatForUI()
        }
    }

    /**
     * Sets the delivery fee field's value as well as visibility of the orders having
     * three tasks.
     *
     * @author Balraj VE00YM023
     */
    private fun setDeliveryFeeForThreeTasksOrder(task: TaskRetrievalResponse){
        /** if its first task then show delivery fee only
         * when order is 'STD: ONE-TIME' and task's status is 'ASSIGNED' and
         * customer type is 'Sender' */
        if((task.taskSequenceNo == 1) && (task.orderTypeId == 1) &&
           (task.taskStatusId == TaskStatus.ASSIGNED.statusId) &&
           (task.deliveryFeePaidBy == "Sender")){
            delivery_fee_label.visibility = View.VISIBLE
            delivery_fee_value.visibility = View.VISIBLE
            delivery_fee_paid_label.visibility = View.VISIBLE
            delivery_fee_paid_label.text = "(Paid By ${selectedTask.deliveryFeePaidBy})"
            delivery_fee_value.text = task.deliveryFeeAmount.formatForUI()
        }
        /** if its third task then show delivery fee only when
         *  order is 'STD: ONE-TIME' and task's status is 'DELIVERING' and
         *  customer type is "Receiver' */
        else if((task.taskSequenceNo == 3) && (task.orderTypeId == 1) &&
                (task.taskStatusId == TaskStatus.DELIVERING.statusId) &&
                (task.deliveryFeePaidBy == "Receiver")){
            delivery_fee_label.visibility = View.VISIBLE
            delivery_fee_value.visibility = View.VISIBLE
            delivery_fee_paid_label.visibility = View.VISIBLE
            delivery_fee_paid_label.text = "(Paid By ${selectedTask.deliveryFeePaidBy})"
            delivery_fee_value.text = task.deliveryFeeAmount.formatForUI()
        }
    }

    private fun setCodAmount(task: TaskRetrievalResponse){
        when(task.maxTaskSequence){
            1 -> setCodAmountForSingleTaskOrder(task)
            3 -> setCodAmountForThreeTasksOrder(task)
        }
    }

    /**
     * Sets the cod amount field's value as well as visibility for orders with just 1 task.
     * we only show cod amount if order type is either 'COD-INVOICE' or 'COD-RECONCILE'
     *
     * @author Balraj VE00YM023
     */
    private fun setCodAmountForSingleTaskOrder(task: TaskRetrievalResponse){
        if(task.orderTypeId == 3 || task.orderTypeId == 4){
            cod_label.visibility = View.VISIBLE
            cod_value.visibility = View.VISIBLE
            vat_ucc_label.visibility = View.VISIBLE
            vat_ucc_value.visibility=View.VISIBLE
            cod_value.text = task.expectedCodAmount.formatForUI()
            vat_ucc_value.text = task.deliveryFeeWithTax.formatForUI()
            fourthDivider.visibility = View.VISIBLE
            amount_separator.visibility = View.VISIBLE
        }
    }


    /**
     * Sets the cod amount field's value as well as visibility for orders with 3 tasks.
     * for an order with 3 tasks we only show cod amount if
     * its 3rd task and order Type is either 'COD-INVOICE' or 'COD-RECONCILE'
     * @author Balraj VE00YM023
     */
    /**
     * Add Delivery Fee with VAT&UCC amount
     * @author Atul Kumar VE00YM430
     */
    private fun setCodAmountForThreeTasksOrder(task: TaskRetrievalResponse){
        if((task.taskSequenceNo == 1)||(task.taskSequenceNo == 3) && ((task.orderTypeId == 3) || (task.orderTypeId == 4))){
            cod_label.visibility = View.VISIBLE
            cod_value.visibility = View.VISIBLE
            cod_value.text = task.expectedCodAmount.formatForUI()
            vat_ucc_label.visibility = View.VISIBLE
            vat_ucc_value.visibility =View.VISIBLE
            vat_ucc_value.text = task.deliveryFeeWithTax.formatForUI()
            fourthDivider.visibility = View.VISIBLE
            amount_separator.visibility = View.VISIBLE
        }
    }

    /**
     * If Cod Value and Delivery fee both are not displayed then
     * hide the entire price details section.
     *
     * @author Balraj VE00YM023
     */
    private fun setPriceDetailSectionVisibility(){
        if((delivery_fee_value.visibility == View.GONE) &&
           (cod_value.visibility == View.GONE)){
            thirdDivider.visibility = View.GONE
            priceDetails.visibility = View.GONE
            fourthDivider.visibility = View.GONE
            amount_separator.visibility = View.GONE
        }
    }

    private fun Double?.formatForUI(): String{
        if(this == null) return "0"

        var formatter = DecimalFormat("#,###")
        formatter.maximumFractionDigits = 0
        formatter.minimumFractionDigits = 0
        return formatter.format(this)
    }

    private fun String?.formatForUI(): String{
        var value: Double? = this?.toDoubleOrNull() ?: return "0.0"
        var formatter = DecimalFormat("#,###.##")
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 2
        return formatter.format(value)
    }

    /**
     * use common intent to send sms to the customer's phone number
     * via phone's SMS application for location sharing
     */
    private fun sendLocationShareSMSToCustomer(){
        val mobileNumber = selectedTask.receiverMobile
        // start native android timer set
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val myTimeListener = OnTimeSetListener { view, hourOfDay, minute ->
            getSMSBody()
            //send sms to user regarding live location sharing
            cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
            cal.set(Calendar.MINUTE,minute)
            val arrivalTime = Utils.getDeliveryTime(cal.timeInMillis)
            viewModel.arrivalTimeLiveData.value = arrivalTime
            //sendSMS(smsMessage,mobileNumber)
        }
        val timePickerDialog = TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false)
        timePickerDialog.setTitle("Choose time:")
        timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    /**
     * gets SMS body of location sharing
     * from API
     */
    private fun getSMSBody(){
        viewModel.getSMSBody()
    }
}

