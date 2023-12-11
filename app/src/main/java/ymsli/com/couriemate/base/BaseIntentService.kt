package ymsli.com.couriemate.base

import android.app.IntentService
import ymsli.com.couriemate.CourieMateApplication
import ymsli.com.couriemate.di.component.DaggerServiceComponent
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.di.module.ServiceModule
import ymsli.com.couriemate.repository.CouriemateRepository
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseIntentService : This abstract class is the base intent service of all the
 *                  intent services and contains common code of all the intent services
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
abstract class BaseIntentService(serviceName:String) : IntentService(serviceName){

    @Inject
    lateinit var couriemateRepository: CouriemateRepository

    override fun onCreate() {
        injectDependencies(buildServiceComponent())
        super.onCreate()
    }

    /**
     * used to create object of properties
     * annotated with @Inject
     * The feature is handled by Dagger2 framework
     */
    private fun buildServiceComponent() =
        DaggerServiceComponent
            .builder()
            .applicationComponent((application as CourieMateApplication).applicationComponent)
            .serviceModule(ServiceModule(this))
            .build()

    protected abstract fun injectDependencies(serviceComponent: ServiceComponent)
}