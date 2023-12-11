package ymsli.com.couriemate.views.taskdetail.taskdetailfragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import kotlinx.android.synthetic.main.fragment_carrier_fail.*
import ymsli.com.couriemate.utils.common.ViewUtils

class FailCarrierFragment: BaseFragment<TaskDetailViewModel>() {

    /** Before performing update this message is shown to the user */
    private lateinit var confirmationMessage: String

    companion object{
        const val TAG = "FailCarrierFragment"
        fun newInstance(): FailCarrierFragment {
            val args = Bundle()
            val fragment = FailCarrierFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_carrier_fail

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        confirmationMessage = getString(R.string.task_update_confirm, "Fail")
        explain.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = viewModel.failureComments.postValue(s.toString().trim())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        fail_submit_btn.setOnClickListener {
            viewModel.taskStatusMessage.value = fail_reason_fragment.selectedItem.toString()
            val failureReason = viewModel.taskStatusMessage.value
            if(this.resources.getStringArray(R.array.failureReasons)[0].equals(failureReason)){
                showMessage(R.string.selectFailureReason)
            }
            else {
                var taskStatusMessage = Constants.FAIL_MSG
                if(!failureReason.equals(getResources().getStringArray(R.array.failureReasons)[3])){
                    taskStatusMessage = taskStatusMessage+Constants.HYPHEN+failureReason
                }
                val onConfirm = DialogInterface.OnClickListener { _, _ ->
                    viewModel.failDeliveryForCarrier(fail_reason_fragment.selectedItem.toString(),taskStatusMessage)
                }
                ViewUtils.showConfirmationDialog(context!!, confirmationMessage, onConfirm)
            }
        }
    }

    private fun adjustCardHeight(){
        var container = activity!!.findViewById<ViewGroup>(R.id.fragment_container)
        var params = container.layoutParams as ViewGroup.LayoutParams
        params.height = 700
    }

    override fun onResume() {
        adjustCardHeight()
        super.onResume()
    }
}