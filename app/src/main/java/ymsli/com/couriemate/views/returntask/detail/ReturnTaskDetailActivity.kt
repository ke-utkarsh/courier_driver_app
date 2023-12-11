package ymsli.com.couriemate.views.returntask.detail

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_order_detail.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseActivity
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.di.component.ActivityComponent
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.TaskStatus
import ymsli.com.couriemate.utils.common.Utils
import kotlinx.android.synthetic.main.activity_return_task_detail.*
import kotlinx.android.synthetic.main.activity_return_task_detail.address
import kotlinx.android.synthetic.main.activity_return_task_detail.dropdown
import kotlinx.android.synthetic.main.activity_return_task_detail.dropup
import kotlinx.android.synthetic.main.activity_return_task_detail.map
import kotlinx.android.synthetic.main.activity_return_task_detail.phoneNo
import kotlinx.android.synthetic.main.activity_return_task_detail.priceDetails
import kotlinx.android.synthetic.main.activity_return_task_detail.quantity
import kotlinx.android.synthetic.main.activity_return_task_detail.receiverName
import kotlinx.android.synthetic.main.activity_return_task_detail.status
import kotlinx.android.synthetic.main.activity_return_task_detail.task_date
import kotlinx.android.synthetic.main.activity_return_task_detail.task_id
import kotlinx.android.synthetic.main.activity_return_task_detail.totalAmountValue
import kotlinx.android.synthetic.main.activity_return_task_detail.tvItemDetails
import ymsli.com.couriemate.adapters.OrderItemAdapter
import ymsli.com.couriemate.utils.common.ViewUtils
import java.text.DecimalFormat

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ReturnTaskDetailActivity : Return task detail activity of the application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class ReturnTaskDetailActivity: BaseActivity<ReturnTaskDetailViewModel>() {

    /** Before performing update this message is shown to the user */
    private lateinit var confirmationMessage: String
    var isDropdown = true
    /**
     * When user confirms the update operation, this code block is executed.
     * @author Balraj VE00YM023
     */
    private val onConfirm = DialogInterface.OnClickListener { _, _ ->
        viewModel.completeTask(selectedTask)
        submitBtn.visibility = View.GONE
        disableButton()
    }

    private lateinit var selectedTask: TaskRetrievalResponse
    override fun provideLayoutId(): Int = R.layout.activity_return_task_detail
    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)
    override fun setupView(savedInstanceState: Bundle?) {
        confirmationMessage = getString(R.string.task_update_confirm, "Return")
        selectedTask = (intent.getSerializableExtra("SelectedTask") as TaskRetrievalResponse)
        viewModel.taskDetail.value = selectedTask
        setActivityStatus(selectedTask.taskStatusId!!, selectedTask.completed!!)
        setupNavigationDrawer(false,R.string.header_order_detail, returntaskdetailtoolbar)
        map.setOnClickListener {
            val addressUri = Uri.parse("google.navigation:q=" +selectedTask.dropAddress+ "&mode=l")
            val mapIntent = Intent(Intent.ACTION_VIEW, addressUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) == null){
                showMessage("No application found to perform this action.")
            }
            else{
                startActivity(mapIntent)
            }
        }

        phoneNo.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:${(viewModel.taskDetail.value as TaskRetrievalResponse).receiverMobile}"))
            if (callIntent.resolveActivity(packageManager) == null){
                showMessage("No application found to perform this action.")
            }
            else{
                startActivity(callIntent)
            }
        }

        submitBtn.setOnClickListener{
            ViewUtils.showConfirmationDialog(this, confirmationMessage, onConfirm)
        }
        tvItemDetails.setOnClickListener {
            if(isDropdown)
            {
                dropdown.visibility =View.GONE
                dropup.visibility =View.VISIBLE
                constraintOrder.visibility =View.VISIBLE
                recylverViewOrder.visibility = View.VISIBLE
                isDropdown=false
            }else{
                dropdown.visibility =View.VISIBLE
                dropup.visibility =View.GONE
                constraintOrder.visibility =View.GONE
                recylverViewOrder.visibility = View.GONE
                isDropdown=true
            }
        }
        if(selectedTask.taskStatusId != TaskStatus.DELIVERING.statusId){
            submitBtn.visibility = View.GONE
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.taskDetail.observe(this, Observer {

            setupOrderMemoInput()
            task_id.text = it.orderNo.toString()
            receiverName.text = viewModel.taskDetail.value?.receiverName.toString()
            phoneNo.text = viewModel.taskDetail.value?.receiverMobile.toString()

            address.text = viewModel.taskDetail.value?.pickupLocation.toString()
            tv_return_task_detail_drop.text = viewModel.taskDetail.value?.dropAddress.toString()
            var quantityValue = if (it.quantity == null) Constants.HYPHEN else it.quantity.toString()
            quantity.text = getString(R.string.Quanity, quantityValue)


            var taskStatus = TaskStatus.getEnumById(it.taskStatusId!!)
            status.text = taskStatus!!.name
            status.setTextColor(Color.parseColor(taskStatus.color))
            setFinalDestination(selectedTask)
            setCodAmount(selectedTask)

            val adapter = OrderItemAdapter(viewModel.taskDetail.value!!.items!!)
            recylverViewOrder.adapter = adapter
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
        })
    }


    private fun setActivityStatus(taskStatusId: Int, isLastTaskCompleted: Int) {
        if(taskStatusId != TaskStatus.DELIVERING.statusId || isLastTaskCompleted == 0){
            disableButton()
        }
    }

    private fun disableButton(){
        submitBtn.isEnabled = false
        submitBtn.background = resources.getDrawable(R.drawable.disabledeliveronclick,theme)
    }

    private fun setupOrderMemoInput(){
        input_return_task_detail_order_memo.setText(selectedTask.orderMemo)
        when(selectedTask.taskStatusId){
            TaskStatus.DELIVERING.statusId-> {
                input_return_task_detail_order_memo.isEnabled = true
                input_return_task_detail_order_memo.background = getDrawable(R.drawable.border)
            }
            else -> {
                input_return_task_detail_order_memo.isEnabled = false
                input_return_task_detail_order_memo.background = getDrawable(R.drawable.bg_border_less)
            }
        }

        /**
         * When value of order memo changes, post it to live data, so that it can be used
         * when performing update.
         * @author Balraj VE00YM023
         */
        input_return_task_detail_order_memo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { viewModel.orderMemo.value = s?.trim().toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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
            label_return_task_detail_final_destination.visibility = View.VISIBLE
            tv_return_task_detail_final_destination.visibility = View.VISIBLE
            tv_return_task_detail_final_destination.text = selectedTask.senderDistrict
        }
    }


    private fun setCodAmount(task: TaskRetrievalResponse){
        if(task.orderTypeId == 3 || (task.orderTypeId == 4)){
            showPriceDetailSection()
            totalAmount.visibility = View.VISIBLE
            totalAmountValue.visibility = View.VISIBLE
            vat_ucc_value_label.visibility =View.VISIBLE
            total_vat_value.visibility=View.VISIBLE
            totalAmountValue.text = task.expectedCodAmount.formatForUI()
            total_vat_value.text= task.deliveryFeeWithTax?.toDouble().formatForUI2()
        }
    }

    private fun showPriceDetailSection(){
        priceDetails.visibility = View.VISIBLE
        fifthDivider.visibility = View.VISIBLE
        sixthDivider.visibility = View.VISIBLE
    }

    private fun Double?.formatForUI(): String{
        if(this == null) return "0"

        var formatter = DecimalFormat("#,###")
        formatter.maximumFractionDigits = 0
        formatter.minimumFractionDigits = 0
        return formatter.format(this)
    }

    private fun Double?.formatForUI2(): String{
        if(this == null) return "0.0"

        var formatter = DecimalFormat("#,###.#")
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 2
        return formatter.format(this)
    }
}