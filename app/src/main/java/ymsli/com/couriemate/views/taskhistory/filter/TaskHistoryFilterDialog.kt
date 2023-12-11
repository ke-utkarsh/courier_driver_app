package ymsli.com.couriemate.views.taskhistory.filter

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ymsli.com.couriemate.R
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.TaskStatus
import android.widget.*
import java.sql.Timestamp
import java.util.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 2, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * TasksFilterDialog : BottomSheetDialogFragment implementation that provides a UI
 *                     for the different filter features on the current tasks fragments.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class TaskHistoryFilterDialog: BottomSheetDialogFragment() {

    lateinit var layout: View
        private set
    private lateinit var actor: Actor
    private var cal: Calendar = Calendar.getInstance()
    private lateinit var backgroundButtonPressed: Drawable

    /** Maintain two arrays for filter buttons and filter texts */
    private val buttons = arrayOfNulls<Button>(3)
    private val texts = arrayOfNulls<TextView>(3)
    private lateinit var spinner: Spinner
    private var applied = false
    private var automaticSelectionEvent = true

    private val blueColor = Color.parseColor("#2079E5")

    /** Maintain the current as well as the previous state of this filter dialog */
    private var currentState  =
        TaskHistoryFilterModel.getDefaultState()
    private var previousState =
        TaskHistoryFilterModel.getDefaultState()

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
        applied = false
        automaticSelectionEvent = true
        layout = inflater.inflate(R.layout.layout_filter_dialog_history, container, false) as View
        initSpinner(layout)
        initButtonsAndTexts(layout)
        previousState = actor.getTaskHistoryFilterPreviousState()
        currentState  = previousState.clone()
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogViewBasedOnPreviousState(previousState)
    }

    /**
     * Init the state of filter buttons and texts.
     * @author Balraj VE00YM023
     */
    private fun initButtonsAndTexts(layout: View){
        buttons[0] = layout.findViewById(R.id.btn_filter_dialog_delivered)
        texts[0]   = layout.findViewById(R.id.text_filter_dialog_delivered)
        buttons[0]!!.setOnClickListener{btn -> filterButtonOnClick(btn as Button, 0)}

        buttons[1] = layout.findViewById(R.id.btn_filter_dialog_returned)
        texts[1]   = layout.findViewById(R.id.text_filter_dialog_returned)
        buttons[1]!!.setOnClickListener{btn -> filterButtonOnClick(btn as Button, 1)}

        buttons[2] = layout.findViewById(R.id.btn_filter_dialog_refused)
        texts[2]   = layout.findViewById(R.id.text_filter_dialog_refused)
        buttons[2]!!.setOnClickListener{btn -> filterButtonOnClick(btn as Button, 2)}
        layout.findViewById<Button>(R.id.btn_filter_dialog_clear).setOnClickListener{resetClearState()}
        layout.findViewById<Button>(R.id.btn_filter_dialog_apply).setOnClickListener{applyFilter()}
        layout.findViewById<Button>(R.id.btn_filter_dialog_close).setOnClickListener{this.dismiss()}
    }

    private fun initSpinner(layout: View){
        spinner = layout.findViewById(R.id.spinner_task_history_filter)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                if(automaticSelectionEvent) automaticSelectionEvent = false
                else{
                    currentState.spinnerIndex = position
                    setClearState()
                }
            }
        }
    }


    /**
     * Configure the dialog UI based on the previous state.
     * if any filter was applied then highlight the corresponding filter buttons and texts.
     * @author Balraj VE00YM023
     */
    private fun setDialogViewBasedOnPreviousState(previousState: TaskHistoryFilterModel){
        /* If Some applyReturnListFilter was set then reset that view */
        if((previousState.selectedBtnIndex != -1) || (previousState.spinnerIndex != 0)){
            if(previousState.selectedBtnIndex != -1){
                setButtonColor(buttons[previousState.selectedBtnIndex]!!, blueColor, blueColor)
                setTextColor(texts[previousState.selectedBtnIndex]!!, Color.parseColor("#000000"))
            }
            if(previousState.spinnerIndex != 0){
                spinner.setSelection(previousState.spinnerIndex, true)
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
            setButtonColor(buttons[currentState.selectedBtnIndex]!!,
                Color.parseColor("#EEEEEE"), Color.parseColor("#AAAAAA"))
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if(!applied){
            spinner.setSelection(previousState.spinnerIndex, true)
        }
        super.dismiss()
    }

    /**
     * When any filter button is clicked change its color and the color of corresponding text.
     * also reset the color of other buttons and texts.
     * update the currentState to include current selectedIndex.
     * @author Balraj VE00YM023
     */
    private fun filterButtonOnClick(btn: Button, index: Int){
        setButtonColor(btn, Color.parseColor("#2079E5"), Color.parseColor("#2079E5"))
        setTextColor(texts[index]!!, Color.parseColor("#000000"))
        resetOtherButtons(btn)
        resetOtherTexts(texts[index]!!)
        currentState.selectedBtnIndex = index
        setClearState()
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
     * When a button is selected, reset color of all the other texts.
     * @author Balraj VE00YM023
     */
    private fun resetOtherTexts(selectedTv: TextView){
        for(tv in texts){
            if(tv!=selectedTv){
                setTextColor(tv!!, Color.parseColor("#8a000000"))
            }
        }
    }

    /**
     * When a button is selected, reset color of all the other buttons to default.
     * @author Balraj VE00YM023
     */
    private fun resetOtherButtons(selectedButton: Button){
        for(btn in buttons){
            if(btn != selectedButton) {
                setButtonColor(btn!!, Color.parseColor("#EEEEEE"),
                    Color.parseColor("#AAAAAA"))
            }
        }
    }

    /**
     * Show the clear button on the header of FilterDialog.
     * @author Balraj VE00YM023
     */
    private fun setClearState() {
        layout.findViewById<Button>(R.id.btn_filter_dialog_clear).visibility = View.VISIBLE
        setApplyButtonStatusOnClear()
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

    /**
     * Reset the clear filter button to default state(hidden).
     * @author Balraj VE00YM023
     */
    private fun resetClearState(){
        layout.findViewById<Button>(R.id.btn_filter_dialog_clear).visibility = View.GONE
        resetTaskStatusBtn()
        resetSpinner()
        setApplyButtonStatusOnClear()
    }

    private fun resetTaskStatusBtn(){
        if(currentState.selectedBtnIndex != -1){
            setButtonColor(buttons[currentState.selectedBtnIndex]!!, Color.parseColor("#EEEEEE"), Color.parseColor("#AAAAAA"))
            setTextColor(texts[currentState.selectedBtnIndex]!!, Color.parseColor("#8a000000"))
            currentState.selectedBtnIndex = -1
        }
    }

    /** Returns the current state of filter dialog */
    private fun getCurrentState(): TaskHistoryFilterModel {
        currentState.taskStatusId = getStatusIdForBtn(currentState.selectedBtnIndex)
        setDateBySpinnerState()
        return currentState
    }

    /** Resets the date filters to default state */
    private fun resetSpinner(){
        spinner.setSelection(0, false)
        currentState.spinnerIndex = 0
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
            R.id.btn_filter_dialog_returned   -> TaskStatus.RETURNED.statusId
            else -> -1
        }
    }

    private fun setDateBySpinnerState(){
        currentState.toDate = if(currentState.spinnerIndex == 0) null else Timestamp(Date().time)
        currentState.fromDate = when(currentState.spinnerIndex){
            0 -> null
            1 -> Utils.getPreviousDayTimeStamp(3, true)
            2 -> Utils.getPreviousDayTimeStamp(7, true)
            else -> Utils.getPreviousDayTimeStamp(30, true)
        }
    }


    /**
     * When apply button is clicked, perform the required validations on date input fields.
     * and then pass the current filter state to the parent activity so that it can
     * filter the bound list based on current filter state.
     * @author Balraj VE00YM023
     */
    private fun applyFilter(){
        applied = true
        actor.applyTaskHistoryFilter(getCurrentState())
    }

    /**
     * User of this filter dialog must implement this interface in order to provide a
     * communication channel.
     * @author Balraj VE00YM023
     */
    interface Actor{
        fun applyTaskHistoryFilter(predicate: TaskHistoryFilterModel)
        fun getTaskHistoryFilterPreviousState(): TaskHistoryFilterModel
    }
}