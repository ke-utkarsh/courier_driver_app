package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.com.couriemate.R


@RunWith(AndroidJUnit4::class)
@LargeTest
class RegistrationPaymentTest: BaseAutomatorTest() {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun checkAmountPaidEditText() {
      onView(withText("Payment Registration")).perform(click())
        onView(withId(R.id.amount_value)).perform(typeText("12344"),closeSoftKeyboard())
    }

    /*
    to check reason spinner is clickable
    */
    @Test
    fun checkReasonSpinnerEnabled() {
        onView(withText("Payment Registration")).perform(click())
        onView(withId(R.id.reason_spinner)).check(matches(isEnabled()))
        onView(withId(R.id.reason_spinner)).perform(click())
    }

    /*
   to check note box is visible on
   select value of others in Paid to
   */
    @Test
    fun checkReasonSpinnerValue() {
        onView(withText("Payment Registration")).perform(click())
        onView(withId(R.id.reason_spinner)).perform(click())
        onView(withText("Others")).perform(click())
        onView(withId(R.id.explain)).check(matches(isDisplayed()))
    }

    /*
     to check image can be
     upload from camera
  */
    @Test
    fun checkCameraOption() {
        launchMainActivity()
        openNavDrawer()
        onView(withText("Payment Registration")).perform(click())
        onView(withText("Use Camera")).perform(click())
        var uiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_camera_btn))))
        Thread.sleep(1000)
        uiObject.click()
        var uiObject1 : UiObject = (device.findObject(UiSelector().resourceId("com.android.camera2:id/done_button")))
        Thread.sleep(1000)
        uiObject1.click()
        onView(withId(R.id.uploaded_image_view)).check(matches(isDisplayed()))
    }

    /*
     to check image can be
     upload from gallery
  */
    @Test
    fun checkGalleryOption() {
        launchMainActivity()
        openNavDrawer()
        onView(withText("Payment Registration")).perform(click())
        onView(withText("Use Gallery")).perform(click())
        var uiObject : UiObject = (device.findObject(UiSelector().text("Camera")))
        uiObject.click()
        var uiObject1 : UiObject = (device.findObject(UiSelector().className("android.view.ViewGroup")))
        uiObject1.click()
        onView(withId(R.id.uploaded_image_view)).check(matches(isDisplayed()))
    }
}