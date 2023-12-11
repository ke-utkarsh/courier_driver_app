package ymsli.com.couriemate.di.component

import ymsli.com.couriemate.di.module.ApplicationTestModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationTestModule::class])
interface TestComponent : ApplicationComponent {

}