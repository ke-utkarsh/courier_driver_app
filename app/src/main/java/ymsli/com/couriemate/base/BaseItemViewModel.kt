package ymsli.com.couriemate.base

import androidx.lifecycle.MutableLiveData
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
 * BaseItemViewModel : This abstract class is the base item view model of all the
 *                  item view model of recycler view, contains common code to all
 *                   view models of item of recycler view
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
abstract class BaseItemViewModel<T : Any>(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val couriemateRepository: CouriemateRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    val data: MutableLiveData<T> = MutableLiveData() //contains data to be populated in recycler view

    fun onManualCleared() = onCleared()

    /**
     * updates data populated in recycler view list
     */
    fun updateData(data: T) {
        this.data.postValue(data)
    }
}