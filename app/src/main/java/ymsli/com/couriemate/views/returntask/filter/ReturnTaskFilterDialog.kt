package ymsli.com.couriemate.views.returntask.filter

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ymsli.com.couriemate.R
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.FilterModel
import ymsli.com.couriemate.utils.common.TaskStatus
import ymsli.com.couriemate.utils.common.Utils
import java.sql.Timestamp
import java.util.*


/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Oct 10, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * ReturnTaskFilterDialog : BottomSheetDialogFragment implementation that provides a UI
 *                        for the different filter features on the return tasks fragments.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */

class ReturnTaskFilterDialog: BottomSheetDialogFragment()  {
    lateinit var layout: View
        private set
    private lateinit var actor: Actor
    private var cal: Calendar = Calendar.getInstance()
    private lateinit var backgroundButtonPressed: Drawable

    /** Maintain two arrays for filter buttons and filter texts */
    private val buttons = arrayOfNulls<Button>(2)
    private val texts = arrayOfNulls<TextView>(2)

    /** Fields for date filters */
    private lateinit var toDate: TextView
    private lateinit var fromDate: TextView

    private val blueColor = Color.parseColor("#2079E5")

    /** Maintain the current as well as the previous state of this filter dialog */
    private var currentState  = FilterModel.getDefaultState()
    private var previousState = FilterModel.getDefaultState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundButtonPressed = resources.getDrawable(R.drawable.shape_filter_btns_pressed)
    }

    /**
     * When this dialog is attached to any activity, then use the context as an implementation
     * of Actor interface, which will be used for communication between activity and
     * this dialog.
     *
     * @author Balraj VE00YM023
     */
    override fun onAttach(context: Context?) {
        actor = context as Actor
        super.onAttach(context)
    }

    /**
     * When view is created set up the layout for appropriate filter and then
     * init the UI based on the previous state of this filter dialog.
     * @author Balraj VE00YM023
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.return_task_filter, container, false) as View
        initDatePickers(layout)
        initButtonsAndTexts(layout)
        previousState = actor.getReturnPreviousState()
        currentState = previousState.clone()
        setDialogViewBasedOnPreviousState(previousState)
        return layout
    }


    /**
     * Init the date pickers UI and configure the handlers to updated UI when a date is
     * selected.
     *
     * @author Balraj VE00YM023
     */
    private fun initDatePickers(layout: View){
        toDate   = layout.findViewById(R.id.date_to_filter_dialog)
        fromDate = layout.findViewById(R.id.date_from_filter_dialog)

        configureDateInput(toDate)
        configureDateInput(fromDate)
    }

    /**
     * Init the state of filter buttons and texts.
     * @author Balraj VE00YM023
     */
    private fun initButtonsAndTexts(layout: View){
        layout.findViewById<Button>(R.id.btn_filter_dialog_clear).setOnClickListener{resetClearState()}
        layout.findViewById<Button>(R.id.btn_filter_dialog_apply).setOnClickListener{applyFilter()}
        layout.findViewById<Button>(R.id.btn_filter_dialog_close).setOnClickListener{this.dismiss()}
    }

    /**
     * Configure the dialog UI based on the previous state.
     * if any filter was applied then highlight the corresponding filter buttons and texts.
     * @author Balraj VE00YM023
     */
    private fun setDialogViewBasedOnPreviousState(previousState: FilterModel){
        /* If Some applyReturnListFilter was set then reset that view */
        if((previousState.selectedBtnIndex != -1) || (previousState.toDate != null)
            || (previousState.fromDate != null)){

            if(previousState.selectedBtnIndex != -1){
                setButtonColor(buttons[previousState.selectedBtnIndex]!!, blueColor, blueColor)
                setTextColor(texts[previousState.selectedBtnIndex]!!, Color.parseColor("#000000"))
            }
            if(previousState.toDate != null){
                toDate.text = Utils.formatDateForFilterDialog(previousState.toDate!!)
                toDate.setTextColor(blueColor)
            }
            if(previousState.fromDate != null){
                fromDate.text = Utils.formatDateForFilterDialog(previousState.fromDate!!)
                fromDate.setTextColor(blueColor)
            }
            setClearState()
        }
        else{
            layout.findViewById<Button>(R.id.btn_filter_dialog_clear).visibility = View.GONE
            var applyBtn = layout.findViewById<Button>(R.id.btn_filter_dialog_apply)
            applyBtn.isClickable = false
            applyBtn.setBackgroundColor(Color.parseColor("#AAAAAA"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(currentState.selectedBtnIndex!=-1){
            setButtonColor(buttons[currentState.selectedBtnIndex]!!,Color.parseColor("#EEEEEE"), Color.parseColor("#AAAAAA"))
        }
    }

    /**
     * Sets the color of filter button.
     * @author Balraj VE00YM023
     */
    private fun setButtonColor(btn: Button, borderColor: Int, iconTint: Int){
        var bg = btn.background.mutate() as GradientDrawable
        bg.setStroke(4, borderColor)
        var icon = btn.compoundDrawables[1] as VectorDrawable
        icon.setTint(iconTint)
    }

    private fun setTextColor(tv: TextView, color: Int){
        tv.setTextColor(color)
    }

    /**
     * Show the clear button on the header of FilterDialog.
     * @author Balraj VE00YM023
     */
    private fun setClearState() {
        layout.findViewById<Button>(R.id.btn_filter_dialog_clear).visibility = View.VISIBLE
        setApplyButtonStatus()
    }

    /**
     * Set the color as well as the state of apply button based on the current state
     * and previous state. if current state and previous state are equal then disable the
     * apply button as there is no change in the state.
     * @author Balraj VE00YM023
     */
    private fun setApplyButtonStatusOnClear(){
        var applyBtn = layout.findViewById<Button>(R.id.btn_filter_dialog_apply)
        if(currentState.equals(previousState)){
            applyBtn.isClickable = false
            applyBtn.setBackgroundColor(Color.parseColor("#AAAAAA"))
            applyBtn.setTextColor(Color.parseColor("#DDDDDD"))
        }
        else{
            applyBtn.isClickable = true
            applyBtn.setBackgroundColor(Color.parseColor("#2079E5"))
            applyBtn.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }


    private fun setApplyButtonStatus(){
        var applyBtn = layout.findViewById<Button>(R.id.btn_filter_dialog_apply)
        if(currentState.selectedBtnIndex == previousState.selectedBtnIndex){
            applyBtn.isClickable = false
            applyBtn.setBackgroundColor(Color.parseColor("#AAAAAA"))
            applyBtn.setTextColor(Color.parseColor("#DDDDDD"))
        }
        else{
            applyBtn.isClickable = true
            applyBtn.setBackgroundColor(Color.parseColor("#2079E5"))
            applyBtn.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    /**
     * Reset the clear filter button to default state(hidden).
     * @author Balraj VE00YM023
     */
    private fun resetClearState(){
        layout.findViewById<Button>(R.id.btn_filter_dialog_clear).visibility = View.GONE
        resetTaskStatusBtn()
        resetDateFilters()
        setApplyButtonStatusOnClear()
    }

    private fun resetTaskStatusBtn(){
        if(currentState.selectedBtnIndex!=-1){
            setButtonColor(buttons[currentState.selectedBtnIndex]!!, Color.parseColor("#EEEEEE"), Color.parseColor("#AAAAAA"))
            setTextColor(texts[currentState.selectedBtnIndex]!!, Color.parseColor("#8a000000"))
            currentState.selectedBtnIndex = -1
        }
    }

    /** Returns the current state of filter dialog */
    private fun getCurrentState(): FilterModel {
        currentState.taskStatusId = getStatusIdForBtn(currentState.selectedBtnIndex)
        return currentState
    }

    /** Resets the date filters to default state */
    private fun resetDateFilters(){
        toDate.text   = Constants.DATE_PLACE_HOLDER
        toDate.setTextColor(Color.parseColor("#8a000000"))
        fromDate.text = Constants.DATE_PLACE_HOLDER
        fromDate.setTextColor(Color.parseColor("#8a000000"))
        currentState.toDate   = null
        currentState.fromDate = null
    }

    /**
     * Return the task status id for specific filter button.
     * every button has a specific task status Id.
     *
     * @author Balraj VE00YM023
     */
    private fun getStatusIdForBtn(btnIndex: Int):Int{
        if(btnIndex==-1) return -1

        var btn = buttons[btnIndex]
        return when(btn!!.id){
            R.id.btn_filter_dialog_delivering -> TaskStatus.DELIVERING.statusId
            R.id.btn_filter_dialog_delivered  -> TaskStatus.DELIVERED.statusId
            R.id.btn_filter_dialog_refused    -> TaskStatus.REFUSED.statusId
            R.id.btn_filter_dialog_failed     -> TaskStatus.FAILED.statusId
            R.id.btn_filter_dialog_assigned   -> TaskStatus.ASSIGNED.statusId
            else -> -1
        }
    }

    /**
     * Configure a date input to use a OnDateSetListener which will pass the selected date
     * to UI so that it can update itself with the selected date.
     *
     * @author Balraj VE00YM023
     */
    private fun configureDateInput(datePicker: TextView) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.clear()
            if(datePicker == fromDate){
                cal.set(year, monthOfYear, dayOfMonth, 0,0,0)
            }
            else{
                cal.set(year, monthOfYear, dayOfMonth, 23,59,59)
            }
            updateDateWidget(datePicker, Timestamp(cal.timeInMillis))
        }

        datePicker!!.setOnClickListener(View.OnClickListener {
            cal.add(Calendar.DAY_OF_MONTH, 1)
            var datePick = DatePickerDialog(context!!,
                R.style.DialogTheme,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            if((datePicker == fromDate) && (currentState.toDate != null)){
                var selectedToDate = currentState.toDate
                cal.timeInMillis = selectedToDate!!.time
                datePick.datePicker.maxDate = cal.timeInMillis
            }
            if((datePicker == toDate) && (currentState.fromDate != null)){
                var selectedFromDate = currentState.fromDate
                cal.timeInMillis = selectedFromDate!!.time
                datePick.datePicker.minDate = (cal.timeInMillis)
            }
            datePick.datePicker.maxDate = System.currentTimeMillis()
            datePick.show()
        })
    }

    /**
     * Set the UI of date input fields
     * @author Balraj VE00YM023
     */
    private fun updateDateWidget(datePicker: TextView, date: Timestamp){
        if(datePicker == toDate){
            currentState.toDate = date
        }
        else { currentState.fromDate = date }
        var dateString = Utils.formatDateForFilterDialog(date)
        datePicker.setTextColor(blueColor)
        datePicker.text = dateString

        layout.findViewById<Button>(R.id.btn_filter_dialog_clear).visibility = View.VISIBLE
        var applyBtn = layout.findViewById<Button>(R.id.btn_filter_dialog_apply)
        applyBtn.isClickable = true
        applyBtn.setBackgroundColor(Color.parseColor("#2079E5"))
    }

    /**
     * When apply button is clicked, perform the required validations on date input fields.
     * and then pass the current filter state to the parent activity so that it can
     * filter the bound list based on current filter state.
     * @author Balraj VE00YM023
     */
    private fun applyFilter(){
        if((toDate.text == Constants.DATE_PLACE_HOLDER) && (fromDate.text != Constants.DATE_PLACE_HOLDER)){
            Toast.makeText(context, "Please enter To date", Toast.LENGTH_LONG).show()
        }
        else if((toDate.text != Constants.DATE_PLACE_HOLDER) && (fromDate.text == Constants.DATE_PLACE_HOLDER)){
            Toast.makeText(context, "Please enter From date", Toast.LENGTH_LONG).show()
        }
        else{
            actor.applyReturnFilter(getCurrentState())
        }
    }

    /**
     * User of this filter dialog must implement this interface in order to provide a
     * communication channel.
     * @author Balraj VE00YM023
     */
    interface Actor{
        fun applyReturnFilter(predicate: FilterModel)
        fun getReturnPreviousState(): FilterModel
    }
}