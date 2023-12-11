package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CallOperatorTest : BaseAutomatorTest() {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()


    @Test
    fun validateDialerPad() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Call Operator"))).click()
        assert((device.findObject(UiSelector().textMatches("^.*( |-)\\d{4}\$"))).exists())
    }
    @Test
    fun validateNumbers() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Call Operator"))).click()
        assert((device.findObject(UiSelector().textMatches("^.*( |-)\\d{4}\$"))).exists())
    }
}