package ymsli.com.couriemate.base

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerServiceComponent
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import javax.inject.Inject

abstract class BaseWorkManager(private val context: Context, workerParameters: WorkerParameters): Worker(context,workerParameters) {
    @Inject
    lateinit var couriemateRepository: CouriemateRepository

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    abstract fun inject(jobServiceComponent: ServiceComponent)

    private fun buildWorkManagerComponent() =
        DaggerServiceComponent
            .builder()
            .applicationComponent((context.applicationContext as CourieMateApplication).applicationComponent)
            .build()

    fun init(){
        inject(buildWorkManagerComponent())
    }

    /**
     * determines if internet is there or not
     */
    fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}