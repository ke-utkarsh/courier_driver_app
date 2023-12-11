package ymsli.com.couriemate.views.main

import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * MainViewModel : This is the MainViewmodel in the couriemate android application
 *                     It is responsible for data flow between main activity
 *                      & APIs/Local data. This also survives configuration change
 *                      of the activity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class MainViewModel(schedulerProvider: SchedulerProvider,
                    compositeDisposable: CompositeDisposable,
                    networkHelper: NetworkHelper,
                    private val courimateRepository: CouriemateRepository):
    BaseViewModel(schedulerProvider,compositeDisposable,networkHelper,courimateRepository) {

    override fun onCreate() {

    }


}