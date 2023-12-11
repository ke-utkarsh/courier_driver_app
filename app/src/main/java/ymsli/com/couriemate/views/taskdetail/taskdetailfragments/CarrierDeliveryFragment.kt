package ymsli.com.couriemate.views.taskdetail.taskdetailfragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import kotlinx.android.synthetic.main.fragment_carrier_delivery.*
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.ViewUtils

class CarrierDeliveryFragment: BaseFragment<TaskDetailViewModel>() {

    /** Before performing update this message is shown to the user */
    private lateinit var confirmationMessage:String

    private var lastDeliveryTime = 0L
    /**
     * When user confirms the update operation, this code block is executed.
     * @author Balraj VE00YM023
     */
    private val onConfirm = DialogInterface.OnClickListener { _, _ ->
        val currentTime = Utils.getTimeInMilliSec()
        if(currentTime-lastDeliveryTime>1500){
            lastDeliveryTime = currentTime
            viewModel.proceedDeliveryToCarrier()
        }
    }

    companion object{
        const val TAG = "CarrierDeliveryFragment"

        fun newInstance(): CarrierDeliveryFragment {
            val args = Bundle()
            val fragment =
                CarrierDeliveryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_carrier_delivery

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        confirmationMessage = getString(R.string.task_update_confirm, "Deliver")
        continue_button.setOnClickListener {
            ViewUtils.showConfirmationDialog(activity!!, confirmationMessage, onConfirm)
        }
    }

    /** Adjusts the height of card so that it doesn't show extra space. */
    private fun adjustCardHeight(){
        var container = activity!!.findViewById<ViewGroup>(R.id.fragment_container)
        var params = container.layoutParams as ViewGroup.LayoutParams
        params.height = 130
    }

    override fun onResume() {
        adjustCardHeight()
        super.onResume()
    }
}