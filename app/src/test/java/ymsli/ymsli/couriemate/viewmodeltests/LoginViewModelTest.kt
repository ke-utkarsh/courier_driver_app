package ymsli.ymsli.couriemate.viewmodeltests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.Status
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import ymsli.com.couriemate.model.UserMaster
import ymsli.com.couriemate.views.login.LoginViewModel
import ymsli.ymsli.couriemate.SchedulerProviderTest
//import io.swagger.client.models.APIResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify

import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var repository: CouriemateRepository

    @Mock
    private lateinit var loggingInObserver : Observer<Boolean>

    @Mock
    private lateinit var configureFCM : Observer<Boolean>

    private lateinit var loginViewModel: LoginViewModel

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
        loginViewModel = LoginViewModel(
            schedulerProviderTest,
            compositeDisposable,
            networkHelper,
            repository
        )
        loginViewModel.loggingIn.observeForever(loggingInObserver)
       // loginViewModel.configureFCM.observeForever(configureFCM)
    }

    /**
     * tests if login call in view model
     * go to doLogin(user) in repository
     */
    @Test
    fun giveServerResponseAndLaunchTaskListActivity(){
        val username = "driver"
        val password = "admin"
        val user =
            UserMaster(username = username, password = password)
        loginViewModel.usernameField.value = username
        loginViewModel.passwordField.value = password

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        doReturn(Single.just(user))
            .`when`(repository)
            .doLogin(user)

        loginViewModel.doLogin("1234")
        verify(repository).doLogin(user)

    }

    /**
     * tests if internet is not connected,
     * error is stored in messageStringId
     */
    @Test
    fun showWErrorWhenNoInternet(){
        val username = "driver"
        val password = "admin"
        loginViewModel.usernameField.value = username
        loginViewModel.passwordField.value = password

        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()

        loginViewModel.doLogin("1234")
        assert((loginViewModel.messageStringId.value)?.status == Status.ERROR)
    }
    /**
     * varialbes need to be destroyed &
     * observables need to be removed after test
     */
    @After
    fun quitTest(){
        loginViewModel.loggingIn.removeObserver(loggingInObserver)
    }
}