package ymsli.ymsli.couriemate.viewmodeltests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.views.taskdetail.TaskDetailViewModel
import ymsli.ymsli.couriemate.SchedulerProviderTest
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class TaskDetailViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var repository: CouriemateRepository

    @Mock
    private lateinit var taskRetrievalResponse: TaskRetrievalResponse

    private lateinit var taskDetailViewModel: TaskDetailViewModel

    private lateinit var testScheduler: TestScheduler

    @Before
    fun setup(){
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val schedulerProviderTest = SchedulerProviderTest(testScheduler)
        taskDetailViewModel = TaskDetailViewModel(
            schedulerProviderTest,
            compositeDisposable,
            networkHelper,
            repository
        )
    }

    @Test
    fun checkRefuseDelivery(){
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        val refusedDelivery = TaskRetrievalResponse(null, null, null, null,
        null, null, null, null, null, 2, null, null,
            null, null, null, null, null, null, null, null,
        null, null,null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null,null, null, null, null,
        null, null, null, null, null, null, null, null, null,
        null)

        doReturn(Single.just(refusedDelivery))
            .`when`(repository)
            .syncTasks(arrayOf(refusedDelivery))

        taskDetailViewModel.syncTasks(refusedDelivery)
        verify(repository).syncTasks(arrayOf(refusedDelivery))
    }

    @Test
    fun checkFailedDelivery(){
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()


        val failedTask = TaskRetrievalResponse( null, null, null, null,
            null, null, null, null, null, 2, null, null,
            null, null, null, null, null, null, null, null,
            null, null,null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null,null, null, null, null,
            null, null, null, null, null, null, null, null, null,
            null)

        doReturn(Single.just(failedTask))
            .`when`(repository)
            .syncTasks(arrayOf(failedTask))

        taskDetailViewModel.syncTasks(failedTask)
        verify(repository).syncTasks(arrayOf(failedTask))
    }

    @Test
    fun checkSuccessfulDelivery(){

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        val deliveryEntity = TaskRetrievalResponse( null, null, null, null,
            null, null, null, null, null, 2, null, null,
            null, null, null, null, null, null, null, null,
            null, null,null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null,null, null, null, null,
            null, null, null, null, null, null, null, null, null,
            null)

        doReturn(Single.just(deliveryEntity))
            .`when`(repository)
            .syncTasks(arrayOf(deliveryEntity))

        taskDetailViewModel.syncTasks(deliveryEntity)
        verify(repository).syncTasks(arrayOf(deliveryEntity))
    }
}