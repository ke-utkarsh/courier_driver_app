package ymsli.com.couriemate.views.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.base.BaseItemViewModel
import ymsli.com.couriemate.database.entity.TransactionConfigItemEntity
import ymsli.com.couriemate.model.DeliveryExpenses
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import javax.inject.Inject

class TransactionHistoryListItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val couriemateRepository: CouriemateRepository
): BaseItemViewModel<DeliveryExpenses>(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    override fun onCreate() {

    }

    fun getTransactionConfigItems(codeValue: Int): LiveData<TransactionConfigItemEntity> = couriemateRepository.getTransactionConfigItem(codeValue)

    val itemValue: LiveData<String> = Transformations.map(data){
        couriemateRepository.getTransactionConfigValue(it.itemId)?:Constants.NA_KEY
    }

    val amount: LiveData<Long> = Transformations.map(data){it.amount}

    val dateTime: LiveData<String> = Transformations.map(data){it.transactionDate}

    val description: LiveData<String> = Transformations.map(data){it.description}
}