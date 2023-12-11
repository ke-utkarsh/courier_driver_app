package ymsli.ymsli.couriemate.uiautomatortests.mainactivitytests

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.com.couriemate.R
import ymsli.ymsli.couriemate.uiautomatortests.BaseAutomatorTest


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityAutomatorTest : BaseAutomatorTest() {

    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun validateActionBar(){
        launchMainActivity()
        onView(withId(R.id.search_task_list_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.filter)).check(matches(isDisplayed()))
    }

    @Test
    fun checkPresenceOfDriverName(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.drawer_header_driver_name))))
        assertTrue(navDrawerViewComponent.exists())

    }

    @Test
    fun checkPresenceOfDriverPhoneNumber(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().resourceId(resources.getString(R.string.drawer_header_driver_phone))))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfCurrentTask(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().text("Current Tasks")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfTaskSummary(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().text("Task Summary")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfTaskHistory(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().text("Task History")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfReturnTasks(){
        launchMainActivity()
        openNavDrawer()

        var  navDrawerViewComponent = (device.findObject(UiSelector().text("Return Tasks")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfCallOperator(){
        launchMainActivity()
        openNavDrawer()

        var   navDrawerViewComponent = (device.findObject(UiSelector().text("Call Operator")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfNotifications(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().text("Notifications")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfTransactionRegistration(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().text("Transaction Registration")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfChangePassword(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().text("Change Password")))
        assertTrue(navDrawerViewComponent.exists())
    }

    @Test
    fun checkPresenceOfLogout(){
        launchMainActivity()
        openNavDrawer()

        var navDrawerViewComponent = (device.findObject(UiSelector().text("Logout")))
        assertTrue(navDrawerViewComponent.exists())
    }


    @Test
    fun checkPresenceOfAPKVersion(){
        launchMainActivity()
        openNavDrawer()

        UiScrollable(UiSelector().scrollable(true)).scrollToEnd(5)

        var navDrawerViewComponent = (device.findObject(UiSelector().textContains("APK Version:")))
        assertTrue(navDrawerViewComponent.exists())

    }

    @Test
    fun checkPresenceOfAPIVersion(){
        launchMainActivity()
        openNavDrawer()

        UiScrollable(UiSelector().scrollable(true)).scrollToEnd(5)

        var navDrawerViewComponent = (device.findObject(UiSelector().textContains("API Version:")))
        assertTrue(navDrawerViewComponent.exists())

    }

    @Test
    fun checkCurrentTaskClickable(){
        launchMainActivity()
        openNavDrawer()

         assert(device.findObject(UiSelector().text("Current Tasks")).click())
    }

    @Test
    fun checkTaskSummaryClickable(){
        launchMainActivity()
        openNavDrawer()

        assert(device.findObject(UiSelector().text("Task Summary")).click())

    }

    @Test
    fun checkTaskHistoryClickable(){
        launchMainActivity()
        openNavDrawer()

        assert(device.findObject(UiSelector().text("Task History")).click())

    }

    @Test
    fun checkReturnTasksClickable(){
        launchMainActivity()
        openNavDrawer()

        assert(device.findObject(UiSelector().text("Return Tasks")).click())
    }

    @Test
    fun checkCallOperatorClickable(){
        launchMainActivity()
        openNavDrawer()

      assert(device.findObject(UiSelector().text("Call Operator")).click())
    }

    @Test
    fun checNotificationsClickable(){
        launchMainActivity()
        openNavDrawer()

        assert(device.findObject(UiSelector().text("Notifications")).click())
    }

    @Test
    fun checkTransactionRegistrationClickable(){
        launchMainActivity()
        openNavDrawer()

         assert(device.findObject(UiSelector().text("Transaction Registration")).click())
    }

    @Test
    fun checkChangePasswordClickable(){
        launchMainActivity()
        openNavDrawer()

        assert(device.findObject(UiSelector().text("Change Password")).click())
    }

    @Test
    fun checkLogoutClickable(){
        launchMainActivity()
        openNavDrawer()

      assert(device.findObject(UiSelector().text("Logout")).click())
    }


    @Test
    fun checkAPKVersionNotClickable(){
        launchMainActivity()
        openNavDrawer()

        UiScrollable(UiSelector().scrollable(true)).scrollToEnd(5)

        assert(device.findObject(UiSelector().textContains("APK Version:")).click())

    }

    @Test
    fun checkPresenceOfAPIVersionNotClickable(){
        launchMainActivity()
        openNavDrawer()

        UiScrollable(UiSelector().scrollable(true)).scrollToEnd(5)

         assert(device.findObject(UiSelector().textContains("API Version:")).click())


    }


    @Test
    fun validateChangePasswordInputFields(){
        launchMainActivity()
        openNavDrawer()
        var changePasswordObject = (device.findObject(UiSelector().text("Change Password")))
        changePasswordObject.click()
        Thread.sleep(500)

        var currentPasswordObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.current_password))))
        assert(currentPasswordObject.exists())

        var newPasswordObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.new_password))))
        assert(newPasswordObject.exists())

        var retypeNewPasswordObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.retype_new_password))))
        assert(retypeNewPasswordObject.exists())

        val passwordToggleUiObject2: UiObject2 = device.findObject(By.res("ymsli.com.couriemate", "text_input_password_toggle"))
        assert(passwordToggleUiObject2.isClickable)

        var updateButoonObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.update_btn))))
        assert(updateButoonObject.exists())

    }

    @Test
    fun validateLogoutNo(){
        launchMainActivity()
        openNavDrawer()

        var uiObject = (device.findObject(UiSelector().text("Logout")))
        uiObject.click()
        Thread.sleep(500)

        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_bar))))
        assertFalse(uiObject.exists())

        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.logout_dialog_no))))
        uiObject.click()
        Thread.sleep(500)

        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_bar))))
        assertTrue(uiObject.exists())
    }

    @Test
    fun validateLogoutYes(){
        launchMainActivity()
        openNavDrawer()

        var uiObject = (device.findObject(UiSelector().text("Logout")))
        uiObject.click()
        Thread.sleep(500)

        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_bar))))
        assert(uiObject.exists())

        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.logout_dialog_yes))))
        uiObject.click()
        Thread.sleep(2000)

        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.btn_login))))
        assert(uiObject.exists())
    }
}