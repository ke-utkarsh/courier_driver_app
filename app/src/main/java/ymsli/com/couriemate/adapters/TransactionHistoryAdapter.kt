package ymsli.com.couriemate.adapters

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import ymsli.com.couriemate.base.BaseAdapter
import ymsli.com.couriemate.model.DeliveryExpenses
import ymsli.com.couriemate.model.DeliveryExpensesResponse

class TransactionHistoryAdapter (parentLifeCycle : Lifecycle,
                                 transactionHistoryList: ArrayList<DeliveryExpenses>,
                                 private val transactionHistoryItemSelectedListener: TransactionHistoryViewHolder.TransactionHistoryItemSelectedListener
)
    :
    BaseAdapter<DeliveryExpenses, TransactionHistoryViewHolder>(parentLifeCycle, transactionHistoryList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHistoryViewHolder = TransactionHistoryViewHolder(parent,transactionHistoryItemSelectedListener)

}