package ymsli.com.couriemate.views.transaction

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_transaction_history.*
import ymsli.com.couriemate.R
import ymsli.com.couriemate.adapters.TransactionHistoryAdapter
import ymsli.com.couriemate.adapters.TransactionHistoryViewHolder
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.model.DeliveryExpenses
import ymsli.com.couriemate.utils.common.Event
import ymsli.com.couriemate.views.tasklist.TaskListViewModel

class TransactionHistoryFragment : BaseFragment<TaskListViewModel>(),
    TransactionHistoryViewHolder.TransactionHistoryItemSelectedListener {

    private lateinit var transactionHistoryAdapter: TransactionHistoryAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        const val TAG = "TransactionHistoryFragment"
        fun newInstance(): TransactionHistoryFragment {
            val args = Bundle()
            val fragment =
                    TransactionHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_transaction_history

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        setHasOptionsMenu(true)
        viewModel.getDeliveryExpensesList()
        linearLayoutManager = LinearLayoutManager(context)
        transactionHistoryAdapter = TransactionHistoryAdapter(lifecycle, ArrayList(),this)
        recycler_transaction_list.layoutManager = linearLayoutManager
        recycler_transaction_list.adapter = transactionHistoryAdapter

        pull_transaction_refresh.setOnRefreshListener {
            viewModel.getDeliveryExpensesList()
            pull_transaction_refresh.isRefreshing = false
        }
    }

    override fun setupObservers() {
        viewModel.deliveryExpensesList.observe(this, Observer {
            if(it.size!=0) {
                tv_no_transaction_history.visibility = View.GONE
                transactionHistoryAdapter.loadData(it.toCollection(ArrayList()))
            }
            else{
                tv_no_transaction_history.visibility = View.VISIBLE
            }
        })

        viewModel.showProgress.observe(this, Observer {
            if(it) {
                progressBar.visibility = View.VISIBLE
                activity!!.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
            else {
                progressBar.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
    }

    /** Hide the task bar menu items */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.findItem(R.id.search_task_list_toolbar)?.isVisible = false
    }

    override fun selectedHistoryItem(transactionHistoryResponse: DeliveryExpenses) {
        viewModel.transactionHistoryItemSelected.postValue(transactionHistoryResponse)
    }

    override fun noItemEditPossible() {
        viewModel.messageStringAPI.postValue(Event(resources.getString(R.string.non_editable_transaction)))
    }
}