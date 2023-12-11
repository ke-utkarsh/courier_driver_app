package ymsli.com.couriemate.views.taskdetail.taskdetailfragments

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.model.DropDownEntity
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.parseJsonForSpinnerAdapter
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_deliver.*
import kotlinx.android.synthetic.main.fragment_refuse.*
import kotlinx.android.synthetic.main.fragment_refuse.view.*
import ymsli.com.couriemate.utils.common.ViewUtils

class RefuseFragment : BaseFragment<TaskDetailViewModel>() {

    /** Before performing update this message is shown to the user */
    private lateinit var confirmationMessage:String

    /**
     * When user confirms the update operation, this code block is executed.
     * @author Balraj VE00YM023
     */
    private val onConfirm = DialogInterface.OnClickListener { _, _ ->
        viewModel.refuseDelivery()
    }

    companion object{
        const val TAG = "RefuseFragment"

        fun newInstance(): RefuseFragment {
            val args = Bundle()
            val fragment =
                RefuseFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_refuse

    override fun injectDependencies(fragmentComponent: FragmentComponent)
            = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        confirmationMessage = getString(R.string.task_update_confirm, "Refuse")
        setSpinnerAdapter()

        /**
         * When continue button is clicked, first show the confirmation message and if user
         * wants to continue then proceed with the update operation.
         *
         * @author Balraj VE00YM023
         */
        view.refusal_submit_btn.setOnClickListener {
            ViewUtils.showConfirmationDialog(context!!, confirmationMessage, onConfirm)
        }

        explain.visibility = View.GONE

       reason_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
           override fun onNothingSelected(parent: AdapterView<*>?) {}

           override fun onItemSelected(parent: AdapterView<*>?, view: View?,
               position: Int, id: Long) {
               var selectedItem = parent?.selectedItem as? DropDownEntity
               if(selectedItem != null && selectedItem.value == "Others"){
                   explain.visibility = View.VISIBLE
                   viewModel.refusalComment.postValue(explain.text.toString())
               }
               else{
                   explain.visibility = View.GONE
                   viewModel.refusalComment.postValue("")
               }
               selectedItem?.let { viewModel.refusalReason.postValue(selectedItem.value)}
           }
       }

        explain.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.refusalComment.postValue(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * First tries to load the spinner data from shared prefs.
     * if no data is available in shared prefs then load the default data. */
    private fun setSpinnerAdapter(){
        var dataList = viewModel.getRefuseReasons()?.parseJsonForSpinnerAdapter()?:
            Utils.getDefaultRefuseReasonData()
        var adapter = ArrayAdapter<DropDownEntity>(activity as Context,
            android.R.layout.simple_spinner_item, dataList)
        reason_spinner.adapter = adapter
    }
}