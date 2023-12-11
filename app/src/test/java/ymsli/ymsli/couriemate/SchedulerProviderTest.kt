package ymsli.ymsli.couriemate

import ymsli.com.couriemate.utils.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class SchedulerProviderTest(private val testScheduler: TestScheduler):
    SchedulerProvider {

    override fun computation(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler

    override fun ui(): Scheduler = testScheduler

}