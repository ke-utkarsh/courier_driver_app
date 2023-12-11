package ymsli.com.couriemate.di.module

import ymsli.com.couriemate.utils.services.SyncWorker
import dagger.Module

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SyncWorkerModule : This is the sync worker module of dagger2 framework. This is
 *                      responsible for providing objects with @Inject annotation
 *                      in the sync worker.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Module
class SyncWorkerModule(private val syncWorker: SyncWorker)