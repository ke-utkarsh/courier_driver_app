package ymsli.ymsli.couriemate.viewmodeltests

import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.model.TaskRetrievalRequest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.views.returntask.list.ReturnTaskListViewModel
import ymsli.ymsli.couriemate.SchedulerProviderTest
import androidx.lifecycle.LiveData
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReturnTaskListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var repository: CouriemateRepository

    @Mock
    lateinit var taskRetrievalLiveData : LiveData<Array<TaskRetrievalResponse>>

    private lateinit var returnTaskListViewModel: ReturnTaskListViewModel

    private lateinit var testScheduler: TestScheduler

    @Before
    fun testSetup(){
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val schedulerProviderTest = SchedulerProviderTest(testScheduler)
        returnTaskListViewModel = ReturnTaskListViewModel(
            schedulerProviderTest,
            compositeDisposable,
            networkHelper,
            repository
        )
    }

    @Test
    fun checkGetReturnTasks(){
        val taskPattern = "R%"
        doReturn(taskRetrievalLiveData)
            .`when`(repository)
            .getDriverTasks(taskPattern)

        returnTaskListViewModel.getReturnTasks()
        verify(repository).getDriverTasks(taskPattern)
    }

    //@Test
    fun checkRefreshDriverTasks(){
        val taskRetrievalRequest = TaskRetrievalRequest()
        val taskRetrievalResponse = TaskRetrievalResponse(null, null, null, null,
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

        doReturn(Single.just(arrayOf(taskRetrievalResponse)))
            .`when`(repository)
            .getDriverTasks(taskRetrievalRequest)

        returnTaskListViewModel.refreshDriverTasks()
        verify(repository).getDriverTasks(taskRetrievalRequest)
    }
}