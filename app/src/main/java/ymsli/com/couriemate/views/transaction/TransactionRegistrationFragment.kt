package ymsli.com.couriemate.views.transaction

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_transaction_registration.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.model.DeliveryExpenses
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Event
import ymsli.com.couriemate.utils.common.SignatureView
import ymsli.com.couriemate.views.tasklist.TaskListViewModel

class TransactionRegistrationFragment : BaseFragment<TaskListViewModel>() {
    private var signature: ByteArray? = null
    private var preShownSign: ByteArray? = null
    private val itemsMap: HashMap<String,Int> = HashMap()

    companion object {
        var isUpdate: Boolean = false
        var selectedTransaction: DeliveryExpenses? = null
        const val TAG = "TransactionRegistrationFragment"
        fun newInstance(): TransactionRegistrationFragment {
            val args = Bundle()
            val fragment = TransactionRegistrationFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(selectedTransaction: DeliveryExpenses): TransactionRegistrationFragment {
            this.selectedTransaction = selectedTransaction
            val args = Bundle()
            val fragment = TransactionRegistrationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_transaction_registration

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
            fragmentComponent.inject(this)

    override fun setupView(view: View) {
        signature_fragment.visibility = View.INVISIBLE

        viewModel.getBalanceBeforeTransaction()
        viewModel.getTransactionConfigItems().observe(this, Observer {
            // populate items spinner here
            if(it!=null){
                val mobileConfigItems = it.filter { it.source.equals(Constants.SOURCE_MOBILE,true) }
                val items = Array<String?>(mobileConfigItems.size) { "" }
                for(i in 0..mobileConfigItems.size-1){
                    itemsMap.put(mobileConfigItems.get(i).dataValue?:Constants.NA_KEY,mobileConfigItems.get(i).codeValue)
                    items.set(i,mobileConfigItems.get(i).dataValue)
                }
                val adapter: ArrayAdapter<String?> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                item_spinner.setAdapter(adapter)

                if(isUpdate) setItemSpinnerOnUpdate()
            }
        })

        viewModel.getReceiptConfigItems().observe(this, Observer {
            // populate receipt spinner
            val items = Array<String?>(it.size) { "" }
            if(it!=null){
                it.reverse()
                for(i in 0..it.size-1){
                    items.set(i,it.get(i).dataValue)
                }
                val adapter: ArrayAdapter<String?> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                receipt_spinner.setAdapter(adapter)

                val myString = "no" //the value you want the position for
                val spinnerPosition = 1
                //set the default according to value
                receipt_spinner.setSelection(spinnerPosition)

                if(isUpdate) setReceiptSpinnerOnUpdate()
            }
        })
        if(selectedTransaction!=null){
            isUpdate = true
            // show data over views
            if(selectedTransaction?.itemId==4){
                // hide add signature and sign box
                btn_add_sign.visibility = View.GONE
                view_signature.visibility = View.GONE
            }
            et_description.setText(selectedTransaction!!.description)
            et_price.setText(selectedTransaction!!.amount.toString())
            if(selectedTransaction!!.itemId.toString().contains("deposit", true)){
                item_spinner.setSelection(0)
            }
            else{
                item_spinner.setSelection(1)
            }
            viewModel.expenditureIdValue.postValue(selectedTransaction!!.expenditureId)
            viewModel.isReceiptValue.postValue(selectedTransaction!!.isReciept)
            viewModel.itemIdValue.postValue(selectedTransaction!!.itemId)

            if(!selectedTransaction!!.isReciept && selectedTransaction!=null && selectedTransaction?.receieverSignatureString!=null) {
                preShownSign =
                    Base64.decode(selectedTransaction?.receieverSignatureString, Base64.DEFAULT)
                signature = preShownSign
                val signImage = BitmapFactory.decodeByteArray(preShownSign, 0, preShownSign!!.size)
                view_signature.setImageBitmap(signImage)
                view_signature.background = null
                // hide Add signature button
                btn_add_sign.visibility = View.GONE
            }
        }

        et_balance_before_transaction.isEnabled = false
        handleSignature()

        /*
        visiblity of signature_fragment on selecting No
        */
        receipt_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 1) {
                    signature_fragment.visibility = View.VISIBLE
                    viewModel.isReceiptValue.postValue(false)
                    signature = preShownSign
                } else {
                    signature_fragment.visibility = View.INVISIBLE
                    viewModel.isReceiptValue.postValue(true)
                    signature = null
                }
            }
        }
        /*
        visiblity of signature_fragment on selecting No
        */
        item_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(item_spinner.selectedItem.toString().equals("bank deposit",true)){
                    tv_receipt.visibility =  View.INVISIBLE
                    constraintLayout4.visibility =View.INVISIBLE
                    et_description.setText(resources.getString(R.string.to_stanbic_bank))
                    et_description.isEnabled = false
                    et_description.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.colorReleaseNotes
                        )
                    )
                    signature_fragment.visibility = View.INVISIBLE
                }
                else {
                    tv_receipt.visibility =  View.VISIBLE
                    constraintLayout4.visibility =View.VISIBLE
                    et_description.isEnabled = true
                    et_description.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.colorHeader
                        )
                    )
                    if(!isUpdate) {
                        et_description.setText("")
                        et_description.setHint(resources.getString(R.string.description_hint))
                    }
                }
            }
        }
        setHasOptionsMenu(true)

        upload_button.setOnClickListener {
            val item = item_spinner.selectedItem.toString()
            viewModel.itemIdValue.value = itemsMap.getValue(item)
            val amount = et_price.text.trim().toString()
            val receipt = receipt_spinner.selectedItem.toString()
            val desc = et_description.text.toString()
            viewModel.saveTransaction(
                amount, signature, receipt.contains("yes", true),
                item.contains("deposit", true), isUpdate, desc, selectedTransaction)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.showProgress.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
                activity!!.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                progressBar.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        viewModel.balanceValue.observe(this, Observer {
            it.getIfNotHandled()?.let {
                et_balance_before_transaction.text = "${resources.getString(R.string.ugx_label)} ${it}"
            }
        })
    }
    private fun handleSignature() {
        btn_add_sign.setOnClickListener { showInputDialog() }
    }

    /**
     * Show an input dialog for the signature input,
     * once users inputs the signature then fill the signature view with the input.
     *
     */
    private fun showInputDialog(){
        val viewInflated = LayoutInflater.from(context)
                .inflate(R.layout.dialog_signature, view as ViewGroup, false)
        val signInput = viewInflated.findViewById(R.id.input_signature) as ConstraintLayout
        val signView = SignatureView(context!!, null)
        signInput.addView(
            signView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context!!)
        alertDialog.setTitle("I received " + et_price.text + " UGX.")
                .setView(viewInflated)
                .setPositiveButton("OK") { dialog, _ ->
                    if(!signView.touchDetected){
                        Toast.makeText(context, "Please input signature", Toast.LENGTH_SHORT).show()
                        showInputDialog()
                    }
                    else{
                        view_signature.setImageBitmap(signView.bitmap)
                        view_signature.background = null
                        signature = signView.bytes
                    }
                }

        alertDialog.show()
    }

    /** Hide the task bar menu items */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.findItem(R.id.search_task_list_toolbar)?.isVisible = false
    }

    /**
     * in case of update in tx
     * item spinner is set
     */
    private fun setItemSpinnerOnUpdate(){
        val itemId = selectedTransaction?.itemId
        if(itemId!=null && itemId == 4) item_spinner?.setSelection(1)
        item_spinner?.isEnabled = false
    }

    /**
     * in case of update in tx
     * receipt spinner is set
     */
    private fun setReceiptSpinnerOnUpdate(){
        if(selectedTransaction!!.isReciept) receipt_spinner?.setSelection(0)
        else receipt_spinner?.setSelection(1)
    }
}

