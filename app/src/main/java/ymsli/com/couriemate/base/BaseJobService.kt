package ymsli.com.couriemate.base

import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerServiceComponent
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import javax.inject.Inject

abstract class BaseJobService : JobService() {

    @Inject
    lateinit var couriemateRepository: CouriemateRepository

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    abstract fun inject(jobServiceComponent: ServiceComponent)

    override fun onCreate() {
        super.onCreate()
        inject(buildActivityComponent())
    }

    private fun buildActivityComponent() =
        DaggerServiceComponent
            .builder()
            .applicationComponent((application as CourieMateApplication).applicationComponent)
            .build()

    /**
     * determines if internet is there or not
     */
    fun isNetworkConnected(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}