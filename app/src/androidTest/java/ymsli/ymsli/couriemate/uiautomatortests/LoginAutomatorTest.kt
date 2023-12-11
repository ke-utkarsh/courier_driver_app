package ymsli.ymsli.couriemate.uiautomatortests

import ymsli.com.couriemate.R
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginAutomatorTest : ymsli.ymsli.couriemate.uiautomatortests.BaseAutomatorTest(){

    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun validateLoginIncorrectUsername(){
        launchLoginActivity()
        setUsernamePassword("adam622","123456")
        verifyLoginFailure()
    }

    @Test
    fun validateLoginIncorrectPassword(){
        launchLoginActivity()
        setUsernamePassword("adam62","123443&!skn56")
        verifyLoginFailure()
    }

    @Test
    fun validateLoginIncorrectUsernamePassword(){
        launchLoginActivity()
        setUsernamePassword("ksdfadam62","?&(*KJNS")
        verifyLoginFailure()
    }

    @Test
    fun validateLoginCorrectUsernamePassword(){
        launchLoginActivity()
        setUsernamePassword("adam62","123456")
        verifyLoginSuccess()
    }

    /**
     * used to enter username and password in login activity
     */
    private fun setUsernamePassword(username: String, password: String){
        device.findObject(By.res(context.resources.getString(R.string.username_edit_text))).text = username
        device.findObject(By.res(context.resources.getString(R.string.password_edit_text))).text = password
        device.findObject(By.res(context.resources.getString(R.string.btn_login))).click()
        Thread.sleep(4000)
    }

    /**
     * verifies the user is logged in
     */
   private fun verifyLoginSuccess(){
        val uiObject : UiObject= (device.findObject(UiSelector().resourceId(resources.getString(R.string.btn_login))))
        assert(!uiObject.exists())
   }

    /**
     * verifies that user can't login
     */
    private fun verifyLoginFailure(){
        val uiObject : UiObject= (device.findObject(UiSelector().resourceId(resources.getString(R.string.btn_login))))
        assert(uiObject.exists())
    }
}