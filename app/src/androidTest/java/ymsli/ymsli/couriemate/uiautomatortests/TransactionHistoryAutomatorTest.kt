package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import junit.framework.TestCase.assertFalse
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ymsli.com.couriemate.R

class TransactionHistoryAutomatorTest : ymsli.ymsli.couriemate.uiautomatortests.BaseAutomatorTest() {

    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()


    /*
  to check all card ui component present
 */
    @Test
    fun checkheader() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Transaction History"))).click()
        var label = (device.findObject(UiSelector().resourceId(resources.getString(R.string.transaction_history))))
        if(!label.exists()){
            assert(false)
        }
        assert(true)
    }

    /*
    to check is there any assigned task is present
   */
    @Test
    fun validateAssignedListView() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Transaction History"))).click()
        val recyclerId = resources.getStringArray(R.array.transactionHistoryRecyclerList)[1]
        var recyclerListuiObject = (device.findObject(UiSelector().resourceId(recyclerId)))
        if (!recyclerListuiObject.exists()) {
            val noDataId = resources.getString(R.id.tv_no_transaction_history)
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
        (device.findObject(UiSelector().text("Transaction History"))).click()
        val recyclerId = resources.getStringArray(R.array.transactionHistoryRecyclerList)[1]
        var recyclerListuiObject = (device.findObject(UiSelector().resourceId(recyclerId)))
        if (recyclerListuiObject.exists()) {
            val elements = resources.getStringArray(R.array.transactionHistoryRecyclerList)
            for (e in elements) {
                Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
            }
        }
    }



}