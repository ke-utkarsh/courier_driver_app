package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiSelector
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.com.couriemate.R


@RunWith(AndroidJUnit4::class)
@LargeTest
class ChangePasswordAutomatorTest : BaseAutomatorTest() {

    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun checkCurrentPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.current_password))).exists())
    }

    @Test
    fun checkNewPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.new_password))).exists())
    }

    @Test
    fun checkRetypeNewPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.retype_new_password))).exists())
    }



    @Test
    fun checkEncryptedDataCurrentPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        device.findObject(UiSelector().resourceId(resources.getString(R.string.current_password))).text = "current72980$#%%"
        Thread.sleep(1000)
        var ui = device.findObject(UiSelector().text("****************"))
        assert(ui.exists())
    }

    @Test
    fun checkEncryptedDataNewPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        device.findObject(UiSelector().resourceId(resources.getString(R.string.new_password))).text = "current72980$#%%"
        Thread.sleep(1000)
        var ui = device.findObject(UiSelector().text("****************"))
        assert(ui.exists())
    }

    @Test
    fun checkEncryptedDataRetypeNewPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        device.findObject(UiSelector().resourceId(resources.getString(R.string.retype_new_password))).text = "current72980$#%%"
        Thread.sleep(1000)
        var ui = device.findObject(UiSelector().text("****************"))
        assert(ui.exists())
    }

    @Test
    fun checkDecryptedDataCurrentPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        device.findObject(UiSelector().resourceId(resources.getString(R.string.current_password))).text = "current72980$#%%"
        Thread.sleep(1000)
        device.findObject(By.res("ymsli.com.couriemate", "text_input_password_toggle")).click()
        Thread.sleep(1000)
        var ui = device.findObject(UiSelector().text("current72980$#%%"))
        assert(ui.exists())
    }

    @Test
    fun checkDecryptedDataNewPassword() {
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        device.findObject(UiSelector().resourceId(resources.getString(R.string.new_password))).text = "current72980$#%%"
        Thread.sleep(1000)
        // val resourceId=device.findObject(By.res("ymsli.com.couriemate", "text_input_password_toggle")).click()
        val resourceId = resources.getString(R.string.password_toggle)
        val navList: List<UiObject2> = device.findObjects(By.res(resourceId))
        var j=0;
        for (i in navList) {
            if (j.equals(1)) {
                i.click()
                break;
            }
            j++
        }
            Thread.sleep(1000)
            var ui = device.findObject(UiSelector().text("current72980$#%%"))
            assert(ui.exists())
        }

        @Test
        fun checkDecryptedDataRetypeNewPassword() {
            launchMainActivity()
            openNavDrawer()
            var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
            changePasswordObject.click()
            device.findObject(UiSelector().resourceId(resources.getString(R.string.retype_new_password))).text = "current72980$#%%"
            Thread.sleep(1000)
            val resourceId = resources.getString(R.string.password_toggle)
            val navList: List<UiObject2> = device.findObjects(By.res(resourceId))
            var j=0;
            for (i in navList) {
                if (j.equals(2)) {
                    i.click()
                    break;
                }
                j++
            }
            var ui = device.findObject(UiSelector().text("current72980$#%%"))
            assertTrue(ui.exists())
        }

    @Test
    fun checkSubmitButton() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Change Password"))).click()
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.update_btn))).exists())
    }

    @Test
    fun validateSubmitButton() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Change Password"))).click()
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.update_btn))).click())
    }
}