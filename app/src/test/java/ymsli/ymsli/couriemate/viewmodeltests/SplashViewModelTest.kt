package ymsli.ymsli.couriemate.viewmodeltests

import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.model.AppVersionResponse
import ymsli.com.couriemate.model.CompanyDetails
import ymsli.com.couriemate.model.UserMaster
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.Status
import ymsli.com.couriemate.views.splash.SplashViewModel
import ymsli.ymsli.couriemate.SchedulerProviderTest
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var repository: CouriemateRepository

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var testScheduler: TestScheduler

    /**
     * variables need to be initialized
     * before execution of the test
     */
    @Before
    fun setup(){
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val schedulerProviderTest = SchedulerProviderTest(testScheduler)
        splashViewModel = SplashViewModel(
            schedulerProviderTest,
            compositeDisposable,
            networkHelper,
            repository
        )
    }

    /**
     * tests if internet is not connected,
     * error is stored in messageStringId
     */
    @Test
    fun showWErrorWhenNoInternet(){
        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()

        splashViewModel.getAppVersion()
       assert((splashViewModel.messageStringId.value)?.status== Status.ERROR)
    }

    @Test
    fun checkAppVersionResponse(){
        val appVersionResponse = AppVersionResponse()


         doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        doReturn(Single.just(appVersionResponse))
            .`when`(repository)
            .fetchAppStatusAndApiInfo()

        repository.fetchAppStatusAndApiInfo();
        verify(repository).fetchAppStatusAndApiInfo()
    }

    @Test
    fun checkIsLoggedIn(){
        doReturn(true)
            .`when`(repository)
            .isLoggedIn()

        splashViewModel.isLoggedIn()
        verify(repository).isLoggedIn()
    }

    @Test
    fun checkSetApiVersionInSharedPref(){
        val appVersionResponse = AppVersionResponse()
        splashViewModel.setApiVersionInSharedPref(appVersionResponse)
        verify(repository).setApiVersionInSharedPref(appVersionResponse)
    }

    @Test
    fun checkGetInitStatus(){
        doReturn(true)
            .`when`(repository)
            .getInitStatus()

        repository.getInitStatus()
        verify(repository).getInitStatus()
    }

    @Test
    fun checkGetCompanyDetails(){
        val companyDetails = CompanyDetails("123456789", "delhi", "out for delivery", "1234" )
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        doReturn(Single.just(companyDetails))
            .`when`(repository)
            .getCompanyDetails()

       repository.getCompanyDetails()
        verify(repository).getCompanyDetails()
    }


}