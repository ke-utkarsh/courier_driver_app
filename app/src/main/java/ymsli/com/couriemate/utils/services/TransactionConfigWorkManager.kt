package ymsli.com.couriemate.utils.services

import android.content.Context
import android.util.Log
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import ymsli.com.couriemate.base.BaseWorkManager
import ymsli.com.couriemate.database.entity.ReceiptConfigEntity
import ymsli.com.couriemate.database.entity.TransactionConfigItemEntity
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.utils.common.Constants
import java.lang.Exception

class TransactionConfigWorkManager(private val context: Context, workerParameters: WorkerParameters):
    BaseWorkManager(context,workerParameters)  {

    override fun inject(jobServiceComponent: ServiceComponent) = jobServiceComponent.inject(this)

    override fun doWork(): Result {
        init()
        return try {
            if(isNetworkConnected() && couriemateRepository.getUserId()!=0L){
                getTransactionConfigItem()
            }
            Result.success()
        }
        catch (ex: Exception){
            Result.success()
        }
    }

    private fun getTransactionConfigItem(){
        compositeDisposable.addAll(
            couriemateRepository.getCodeMasterConfig(17)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    if(it!=null) {
                        val type = object : TypeToken<List<TransactionConfigItemEntity>>() {}.type
                        val configItems = Gson().fromJson(JSONArray(it.data.get("value")).toString(), type) as ArrayList<TransactionConfigItemEntity>
                        couriemateRepository.insertNewItemConfig(configItems.toTypedArray())
                    }
                    getTransactionConfigReceipt()
                },{
                    Log.d("Error", it.localizedMessage)
                })
        )
    }

    private fun getTransactionConfigReceipt(){
        compositeDisposable.addAll(
            couriemateRepository.getCodeMasterConfig(16)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    if(it!=null) {
                        val type = object : TypeToken<List<ReceiptConfigEntity>>() {}.type
                        val configItems = Gson().fromJson(JSONArray(it.data.get("value")).toString(), type) as ArrayList<ReceiptConfigEntity>
                        couriemateRepository.insertNewReceiptConfig(configItems.toTypedArray())
                    }
                },{
                    Log.d("Error", it.localizedMessage)
                })
        )
    }
}