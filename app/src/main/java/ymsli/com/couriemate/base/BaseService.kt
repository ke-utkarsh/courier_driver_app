package ymsli.com.couriemate.base

import android.app.Service
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerForegroundServiceComponent
import ymsli.com.couriemate.di.component.ForegroundServiceComponent
import ymsli.com.couriemate.di.module.ForegroundServiceModule
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import javax.inject.Inject

abstract class BaseService: Service() {

    @Inject
    lateinit var couriemateRepository: CouriemateRepository

    @Inject lateinit var networkHelper: NetworkHelper
    @Inject lateinit var scheduleProvider: SchedulerProvider
    @Inject lateinit var disposable: CompositeDisposable

    protected abstract fun injectDependencies(foregroundServiceComponent: ForegroundServiceComponent)

    override fun onCreate() {
        injectDependencies(buildServiceComponent())
    }

    private fun buildServiceComponent() =
        DaggerForegroundServiceComponent
            .builder()
            .applicationComponent((application as CourieMateApplication).applicationComponent)
            .foregroundServiceModule(ForegroundServiceModule(this))
            .build()


}