package ymsli.com.couriemate.di.component

import dagger.Component
import ymsli.com.couriemate.di.IntentServiceScope
import ymsli.com.couriemate.di.module.FCMServiceModule
import ymsli.com.couriemate.di.module.ForegroundServiceModule
import ymsli.com.couriemate.di.module.ServiceModule
import ymsli.com.couriemate.di.module.SyncWorkerModule
import ymsli.com.couriemate.utils.services.ForegroundLocationService

@IntentServiceScope
@Component(dependencies = [ApplicationComponent::class],modules = [ForegroundServiceModule::class])
interface ForegroundServiceComponent {

    fun inject(foregroundLocationService: ForegroundLocationService)
}