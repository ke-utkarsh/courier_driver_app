package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.com.couriemate.R


@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskHistoryTest: BaseAutomatorTest()  {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()


    /*
      to check is there any assigned task is present
     */
    @Test
    fun validateAssignedListView() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Task History"))).click()
        val recyclerId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var recyclerListuiObject = (device.findObject(UiSelector().resourceId(recyclerId)))
        if (!recyclerListuiObject.exists()) {
            val noDataId = resources.getString(R.id.text_no_data_task_list)
            val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
            assert(noDataIdUiObject.exists())
        }
        assert(true)
    }

    /*
       to check all card ui component present
      */
    @Test
    fun validateView() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Task History"))).click()
        val elements = resources.getStringArray(R.array.taskHistoryRecyclerList)
        for (e in elements) {
            Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
        }
    }

    /*
      to check all filter ui component present
    */
    @Test
    fun validateFilterActions() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Task History"))).click()
        var filterIconObject: UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        filterIconObject.click()
        Thread.sleep(2000)

        var deliveredUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_delivered))))
        deliveredUiObject.click()

        var returnedUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_return))))
        returnedUiObject.click()

        var refusedUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_refused))))
        refusedUiObject.click()

        var filterSpinnerUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_spinner))))
        filterSpinnerUiObject.click()

        (device.findObject(UiSelector().text("Last 3 days"))).click()

        var clearUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_clear_button))))
        clearUiObject.click()

        filterSpinnerUiObject.click()

        (device.findObject(UiSelector().text("Last 7 days"))).click()

        clearUiObject.click()

        filterSpinnerUiObject.click()

        (device.findObject(UiSelector().text("Last 1 month"))).click()

        var applyUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_apply_button))))
        applyUiObject.click()
    }

    /*
    to check Delivered Filter Action
  */
    @Test
    fun validateDeliveredFilterActions() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Task History"))).click()
        var filterIconObject: UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        filterIconObject.click()
        Thread.sleep(2000)
        var deliveredUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_delivered))))
        deliveredUiObject.click()

        var applyUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_apply_button))))
        applyUiObject.click()

        val recyclerId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var recyclerListuiObject = (device.findObject(UiSelector().resourceId(recyclerId)))
        assert( recyclerListuiObject.text=="DELIVERED")
    }

    /*
    to check Returned Filter Action
    */
    @Test
    fun validateReturnedFilterActions() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Task History"))).click()
        var filterIconObject: UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        filterIconObject.click()
        Thread.sleep(2000)
        var returnedUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_return))))
        returnedUiObject.click()
        var applyUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_apply_button))))
        applyUiObject.click()
        val recyclerId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var recyclerListuiObject = (device.findObject(UiSelector().resourceId(recyclerId)))
        assert(recyclerListuiObject.text=="RETURNED")

    }

    /*
    to check Refused Filter Action
    */
    @Test
    fun validateRefusedFilterActions() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Task History"))).click()
        var filterIconObject: UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        filterIconObject.click()
        Thread.sleep(2000)
        var refusedUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_refused))))
        refusedUiObject.click()
        var applyUiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_apply_button))))
        applyUiObject.click()
        val recyclerId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var recyclerListuiObject = (device.findObject(UiSelector().resourceId(recyclerId)))
        assert(recyclerListuiObject.text=="REFUSED")
    }
}