package ymsli.ymsli.couriemate.viewmodeltests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.model.TaskRetrievalRequest
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import ymsli.ymsli.couriemate.SchedulerProviderTest
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class TaskListViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var repository: CouriemateRepository

    @Mock
    private lateinit var taskRetrievalRequest: TaskRetrievalRequest

    @Mock
    private lateinit var taskRetrievalResponse: TaskRetrievalResponse

    private lateinit var testScheduler: TestScheduler

    private lateinit var tasklistViewModel: TaskListViewModel

    @Before
    fun setup(){
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val schedulerProviderTest = SchedulerProviderTest(testScheduler)
        tasklistViewModel = TaskListViewModel(schedulerProviderTest,
            compositeDisposable,
            networkHelper,
            repository)


    }

    @Test
    fun checkPerformPickup(){
        val tasks : TaskRetrievalResponse = TaskRetrievalResponse(null, null, null, null,
            null, null, null, null, null, 2, null, null,
            null, null, null, null, null, null, null, null,
            null, null,null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null,null, null, null, null,
            null, null, null, null, null, null, null, null, null,
            null)

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        doReturn(Single.just(arrayOf(tasks)))
            .`when`(repository)
            .syncTasks(arrayOf(tasks))

        tasklistViewModel.performPickup(listOf(tasks))
        verify(repository).syncTasks(arrayOf(tasks))
    }
}