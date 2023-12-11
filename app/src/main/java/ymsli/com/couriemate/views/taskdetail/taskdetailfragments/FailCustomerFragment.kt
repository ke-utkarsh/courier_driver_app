package ymsli.com.couriemate.views.taskdetail.taskdetailfragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_fail.*
import ymsli.com.couriemate.utils.common.ViewUtils
import java.sql.Timestamp
import java.util.*

class FailCustomerFragment : BaseFragment<TaskDetailViewModel>() {

    private var failureReason: String = "Other"

    /** Before performing update this message is shown to the user */
    private lateinit var confirmationMessage: String

    /**
     * When user confirms the update operation, this code block is executed.
     * @author Balraj VE00YM023
     */
    private val onConfirm = DialogInterface.OnClickListener { _, _ ->
        viewModel.failDelivery(failureReason)
    }


    companion object{
        const val TAG = "FailCustomerFragment"
        fun newInstance(): FailCustomerFragment {
            val args = Bundle()
            val fragment =
                FailCustomerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_fail

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        confirmationMessage = getString(R.string.task_update_confirm, "Fail")
        datePick.setOnClickListener {
            viewModel.showDatePicker.postValue(true)
        }
        failure_comments.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) = viewModel.failureComments.postValue(s.toString().trim())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        fail_reason_fragment.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    failureReason = "Customer unreachable"
                    val d = (Date(System.currentTimeMillis() + 86400000))
                    viewModel.nextDeliveryDateTime.postValue(Timestamp((d).time))
                    failure_comments.visibility = View.GONE
                    viewModel.failureComments.postValue("")
                }
                else {
                    failureReason = "Other"
                    failure_comments.visibility = View.VISIBLE
                }
            }
        })
        fail_submit_btn.setOnClickListener {
            ViewUtils.showConfirmationDialog(context!!, confirmationMessage, onConfirm)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.nextDeliveryDateTime.observe(this,androidx.lifecycle.Observer {
            datePick.text = Utils.formatDate(it)
        })

        //error observers
        viewModel.invalidRetrydate.observe(this, androidx.lifecycle.Observer {
            showMessage(R.string.postponmentErrorMessage)
        })

        viewModel.invalidFailureComments.observe(this, androidx.lifecycle.Observer {
            showMessage(R.string.failureComment)
        })
    }
}