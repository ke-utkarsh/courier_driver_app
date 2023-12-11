package ymsli.com.couriemate.adapters

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.recycler_view_card_transaction_item.view.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseItemViewHolder
import ymsli.com.couriemate.di.component.ViewHolderComponent
import ymsli.com.couriemate.model.DeliveryExpenses
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.views.transaction.TransactionHistoryListItemViewModel

class TransactionHistoryViewHolder (parent: ViewGroup,private val transactionHistoryItemSelectedListener: TransactionHistoryItemSelectedListener):
    BaseItemViewHolder<DeliveryExpenses, TransactionHistoryListItemViewModel>(R.layout.recycler_view_card_transaction_item,parent)  {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) = viewHolderComponent.inject(this)

    override fun setupView(view: View) {
        view.setOnClickListener {
            val selectedItem = viewModel.data.value
            viewModel.getTransactionConfigItems(selectedItem?.itemId?:0).observe(this, Observer {
                if(it!=null){
                    if(it.source!=null && it.source.equals(Constants.SOURCE_MOBILE,true)){
                        if(selectedItem!=null) transactionHistoryItemSelectedListener.selectedHistoryItem(selectedItem)
                    }
                    else{
                        // show item cannot be edited
                        transactionHistoryItemSelectedListener.noItemEditPossible()
                    }
                }
            })
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.itemValue.observe(this, Observer {
            itemView.tv_transaction_item.text = it.toString()
        })

        viewModel.amount.observe(this, Observer {
            itemView.tv_transaction_amount_value.text = "${it} ${itemView.context.resources.getString(R.string.ugx_label)}"
        })

        viewModel.dateTime.observe(this, Observer {
            if(!it.isNullOrBlank()) itemView.tv_date_value.text = Utils.getDateTimeForTxHistory(it)
        })

        viewModel.description.observe(this, Observer {
            if(!it.isNullOrBlank()) itemView.tv_transaction_description_value.text = it
            else itemView.tv_transaction_description_value.text = Constants.NA_KEY
        })
    }

    interface TransactionHistoryItemSelectedListener{
        fun selectedHistoryItem(transactionHistoryResponse: DeliveryExpenses)
        fun noItemEditPossible()
    }
}