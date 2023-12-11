package ymsli.com.couriemate

import ymsli.com.couriemate.di.component.ApplicationComponent
import ymsli.com.couriemate.di.component.DaggerApplicationComponent
import ymsli.com.couriemate.di.module.ApplicationModule
import ymsli.com.couriemate.utils.common.Constants
import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.common.Utils
import ymsli.com.couriemate.utils.common.ViewUtils
import java.util.concurrent.Executors
import javax.inject.Inject
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CourieMateApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
    private lateinit var googleAnalytics: GoogleAnalytics
    private lateinit var tracker: Tracker

    @Inject
    lateinit var couriemateRepository: CouriemateRepository

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
        couriemateRepository.setTripStatusInSharedPrefs(false)
        WorkManager.initialize(
            applicationContext,
            Configuration.Builder().setExecutor(Executors.newFixedThreadPool(Constants.WORKMANAGER_THREADS))
                .build()
        )
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        googleAnalytics = GoogleAnalytics.getInstance(this)
       /* if(couriemateRepository.getUserId()!=0L) {
            ViewUtils.startTripConfigService(applicationContext)
        }*/
        couriemateRepository.setLastLocationTimestamp(Utils.getTimeInMilliSec())
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }

    @Synchronized
    fun getDefaultTracker(): Tracker? { // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        tracker = googleAnalytics.newTracker(R.xml.global_tracker)
        return tracker
    }
}
