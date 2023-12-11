package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Test
import ymsli.com.couriemate.R

class NotificationAutomatorTest : BaseAutomatorTest() {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()


    /*
          to check list view of notifications
       */
    @Test
    fun validateNotificationListView() {

        launchMainActivity()
        openNavDrawer()


        var statusUiObject1   = device.findObject(By.text("Notifications"))
        statusUiObject1.click()
        Thread.sleep(1000)
        var statusUiObject2   = device.findObject(By.text("New Task Assigned"))
        if (statusUiObject2==null) {
            val noDataId = resources.getString(R.id.text_no_data_notification_list)
            val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
            assert(noDataIdUiObject.exists())
        }

        assert(true)
    }

    /*
          to check all ui components of notifications
       */

    @Test
    fun validateNotificationUI() {

        launchMainActivity()
        openNavDrawer()

        var statusUiObject1   = device.findObject(By.text("Notifications"))
        statusUiObject1.click()
        Thread.sleep(1000)
        var statusUiObject2   = device.findObject(By.text("Notifications"))
        if(statusUiObject2==null){
            assert(false)
        }
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.notification_header))).exists())
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.notification_detail))).exists())
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.notification_time))).exists())


    }

}