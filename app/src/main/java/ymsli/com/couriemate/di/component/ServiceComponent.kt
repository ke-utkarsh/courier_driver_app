package ymsli.com.couriemate.di.component

import ymsli.com.couriemate.di.IntentServiceScope
import ymsli.com.couriemate.di.module.FCMServiceModule
import ymsli.com.couriemate.di.module.ServiceModule
import ymsli.com.couriemate.di.module.SyncWorkerModule
import dagger.Component
import ymsli.com.couriemate.utils.services.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ServiceComponent : This is the service component of dagger2 framework. This is
 *                      responsible for injecting objects in the service.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@IntentServiceScope
@Component(dependencies = [ApplicationComponent::class],modules = [ServiceModule::class,
    FCMServiceModule::class, SyncWorkerModule::class])
interface ServiceComponent {
    fun inject(syncIntentService: SyncIntentService)

    fun inject(fcmService: FCMService)

    fun inject(syncWorker: SyncWorker)

    fun inject(zipUploadService: ZipUploadService)

    fun inject(tripConfigWorkManager: TripConfigWorkManager)

    fun inject(transactionConfigWorkManager: TransactionConfigWorkManager)
}