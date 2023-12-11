package ymsli.com.couriemate.views.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ymsli.com.couriemate.base.BaseItemViewModel
import ymsli.com.couriemate.database.entity.NotificationResponse
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Jan 7, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * NotificationListItemViewModel : List item view model for NotificationListFragment
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class NotificationListItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    couriemateRepository: CouriemateRepository
): BaseItemViewModel<NotificationResponse>(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    /** Map the data to fields requried by UI, we currently only show three fields
     *  in notification Item.
     *  @author Balraj VE00YM023
     */
    val header: LiveData<String> = Transformations.map(data){ it.eventHeader }
    val detail: LiveData<String> = Transformations.map(data){ it.eventBody }
    val receivedOn: LiveData<String> = Transformations.map(data){ it.receivedOn}

    override fun onCreate() {}
}