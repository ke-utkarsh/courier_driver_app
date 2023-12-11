package ymsli.com.couriemate.views.taskdetail.taskdetailfragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.fragment_deliver.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.model.DropDownEntity
import ymsli.com.couriemate.utils.common.SignatureView
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.ViewUtils
import ymsli.com.couriemate.utils.common.parseJsonForSpinnerAdapter
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class DeliverFragment : BaseFragment<TaskDetailViewModel>() {

    private var signature: ByteArray? = null

    /** Before performing update this message is shown to the user */
    private lateinit var confirmationMessage: String

    private var lastDeliveryTime = 0L

    /**
     * When user confirms the update operation, this code block is executed.
     * @author Balraj VE00YM023
     */
    private val onConfirm = DialogInterface.OnClickListener { _, _ ->
        var hideCodInputs = arguments?.getBoolean(HIDE_COD_INPUT) ?: false
        if (signature == null) {
            Toast.makeText(context, "Please input signature", Toast.LENGTH_SHORT).show()
        } else {
            val currentTime = Utils.getTimeInMilliSec()
            if (currentTime - lastDeliveryTime > 1500) {
                lastDeliveryTime = currentTime
                viewModel.proceedDelivery(!hideCodInputs, signature)
            }
        }
    }

    companion object {
        const val HIDE_COD_INPUT = "hideCodInput"
        const val TAG = "DeliveryFragment"
        fun newInstance(): DeliverFragment {
            val args = Bundle()
            val fragment = DeliverFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_deliver

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {
        handleSignature()
        confirmationMessage = getString(R.string.task_update_confirm, "Deliver")
        setSpinnerAdapter()
        setCodAmountInputStatus(viewModel.taskDetail.value!!)
        amount_value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                amount_value.removeTextChangedListener(this)

                try {
                    var originalString: String = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longval: Long = originalString.toLong()
                    val formatter: DecimalFormat =
                        NumberFormat.getInstance() as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    val cashAmountString = amount_value.text?.toString()?.replace(",", "")
                    viewModel.codReceived.postValue(cashAmountString?.trim())
                    amount_value.setText(formattedString)
                    amount_value.setSelection(amount_value.getText().toString().length)

                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }


                amount_value.addTextChangedListener(this)


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        //        var input: String = s.toString()
/* if (!input.isEmpty()) {
     input = input.replace(",", "")
     val format = DecimalFormat("#,###,###")
     val newPrice = format.format(input.toDouble())
     amount_value.removeTextChangedListener(this) //To Prevent from Infinite Loop
     amount_value.setText(newPrice)
     amount_value.setSelection(newPrice.length) //Move Cursor to end of String
     amount_value.addTextChangedListener(this)
 }*/
            }
        })

        mm_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) =
                viewModel.mmReceived.postValue(s.toString().trim())

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        task_memo_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) =
                viewModel.taskMemo.postValue(s.toString().trim())

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        mm_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?,
                position: Int, id: Long
            ) {
                (parent?.selectedItem as? DropDownEntity)?.let {
                    viewModel.mmType.value = it.code
                }
            }
        }

        /** Set focus change listeners on the cod inputs, to show warning if
         *  user enters less cod amount then expected.
         */
        amount_value.onFocusChangeListener = codInputFocusChangeListener
        mm_editText.onFocusChangeListener = codInputFocusChangeListener

        /**
         * When continue button is clicked, first show the confirmation message and if user
         * wants to continue then proceed with the update operation.
         *
         * @author Balraj VE00YM023
         */
        continue_button.setOnClickListener {
            ViewUtils.showConfirmationDialog(context!!, confirmationMessage, onConfirm)
        }
    }

    private val codInputFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) showCodWarning()
    }

    /**
     * When user enters amount in any cod input field (cash or mobile money),
     * if total cod received is less than the expected cod amount then we
     * show a warning message in both the fields.
     * added date: 26/02/2020
     * @author Balraj VE00YM023
     */
    private fun showCodWarning() {
        val codInputsHidden = arguments?.getBoolean(HIDE_COD_INPUT) ?: false
        if (!codInputsHidden) {
            /** If user has not entered anything, then clear error if any and return*/
            if (amount_value.text?.toString().isNullOrEmpty() &&
                mm_editText.text?.toString().isNullOrEmpty()
            ) {
                amount_value.error = null
                mm_editText.error = null
                return
            }
            /** If user has typed a value in atleast one field then show the warning if required */
            val cashAmountString = amount_value.text?.toString()?.replace(",", "")
            val cashAmount = cashAmountString?.toDoubleOrZero() ?: 0.0
            val mobileMoney = mm_editText.text?.toString()?.toDoubleOrZero() ?: 0.0
            val totalMoneyReceived = cashAmount + mobileMoney
            val expectedCod = viewModel.taskDetail.value!!.expectedCodAmount.toString().toDouble()
            if (totalMoneyReceived < expectedCod) {
                amount_value.error = getString(R.string.warning_less_cod_received)
                mm_editText.error = getString(R.string.warning_less_cod_received)
            } else {
                amount_value.error = null
                mm_editText.error = null
            }
        }
    }


    private fun setCodAmountInputStatus(task: TaskRetrievalResponse) {
        if ((task.expectedCodAmount == 0.0) || (task.orderTypeId == 0) ||
            (task.orderTypeId == 1) || (task.orderTypeId == 2)
        ) {
            amount_value.isEnabled = false
            mm_editText.isEnabled = false
            amount_value.setText("0.0")
            viewModel.mmType.value = null
            mm_spinner.isEnabled = false
            mm_editText.isEnabled = false
            mm_editText.setText("0.0")
        }

        /** Hide the input fields if the cod amount field on the parent activity is hidden */
        val hideInputFields = arguments?.getBoolean(HIDE_COD_INPUT) ?: false
        if (hideInputFields) {
            amountReceived.visibility = View.GONE
            amount_value.visibility = View.GONE
            mm_text.visibility = View.GONE
            mm_spinner.visibility = View.GONE
            mm_editText.visibility = View.GONE
        }
    }

    /**
     * First tries to load the spinner data from shared prefs.
     * if no data is available in shared prefs then load the default data. */
    private fun setSpinnerAdapter() {
        var dataList = viewModel.getMobileMoneyTypes()?.parseJsonForSpinnerAdapter()
            ?: Utils.getDefaultMobileMoneyTypes()
        var adapter = ArrayAdapter<DropDownEntity>(
            activity as Context,
            android.R.layout.simple_spinner_item, dataList
        )
        mm_spinner.adapter = adapter
    }

    private fun handleSignature() {
        btn_add_sign.setOnClickListener { showInputDialog() }
    }

    /**
     * Show an input dialog for the signature input,
     * once users inputs the signature then fill the signature view with the input.
     *
     * @author Balraj VE00YM023
     */
    private fun showInputDialog() {
        val viewInflated = LayoutInflater.from(context)
            .inflate(R.layout.dialog_signature, view as ViewGroup, false)
        val signInput = viewInflated.findViewById(R.id.input_signature) as ConstraintLayout
        val signView = SignatureView(context!!, null)
        signInput.addView(signView, MATCH_PARENT, MATCH_PARENT)
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context!!)
        alertDialog.setTitle("Add Signature")
            .setView(viewInflated)
            .setPositiveButton("OK") { dialog, _ ->
                if (!signView.touchDetected) {
                    Toast.makeText(context, "Please input signature", Toast.LENGTH_SHORT).show()
                    showInputDialog()
                } else {
                    view_signature.setImageBitmap(signView.bitmap)
                    view_signature.background = null
                    signature = signView.bytes
                }
            }

        alertDialog.show()
    }
}

private fun String.toDoubleOrZero(): Double = if (isNotEmpty()) toDouble() else 0.0