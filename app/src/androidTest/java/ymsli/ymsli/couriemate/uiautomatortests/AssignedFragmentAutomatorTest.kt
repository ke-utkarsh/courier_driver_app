package ymsli.ymsli.couriemate.uiautomatortests

import android.app.Instrumentation
import android.os.SystemClock
import android.view.MotionEvent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.com.couriemate.R
import java.lang.Exception


@RunWith(AndroidJUnit4::class)
@LargeTest
class AssignedFragmentAutomatorTest : BaseAutomatorTest() {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun validateTaskItemsInCard(){
        launchMainActivity()
        val elements = resources.getStringArray(R.array.taskListRecyclerItems)
        for(e in elements){
            Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
        }
    }
    /*
 to check order details
*/
    @Test
    fun validateOrderDetails() {
        launchMainActivity()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)
        assert(checkViewIdis())

    }
    @Test
    fun validateCallOnSwipe(){
        launchMainActivity()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).swipeLeft(5)
        Thread.sleep(2000)
        assert((device.findObject(UiSelector().descriptionContains("#")).exists()))
    }
    @Test
    fun validateCall(){
        launchMainActivity()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)

        var callId: String = resources.getString(R.string.action_call)
        (device.findObject(UiSelector().resourceId(callId))).click()
        Thread.sleep(2000)
        assert((device.findObject(UiSelector().descriptionContains("#")).exists()))
    }

//    @Test
//    fun validateAssignedToDelivering(){
//        launchMainActivity()
//        val resId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
//        val uiObject : UiObject = (device.findObject(UiSelector().resourceId(resId)))
//        if(uiObject.text.equals("ASSIGNED",true)){
//            var orderNoID = resources.getStringArray(R.array.taskListRecyclerItems)[0]
//            val orderNo : UiObject = (device.findObject(UiSelector().resourceId(orderNoID)))
//            var orderNoText = orderNo.text
//            uiObject.swipeRight(5)
//            Thread.sleep(2000)
//            device.findObject(By.text("ONGOING")).click()
//            Thread.sleep(1000)
//            swiper(1000, 100, 0);
//            Thread.sleep(2000)
//            assert(device.findObject(UiSelector().text(orderNoText)).exists())
//
//        }
//        else throw Exception("No ASSIGNED tasks for testing!!")
//    }

    @Test
    fun validateFilterActions(){
        launchMainActivity()
        var uiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        uiObject.click()
        Thread.sleep(2000)
        uiObject  = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_location))))
        uiObject.click()

        var filterAddress = (device.findObject(UiSelector().resourceId(resources.getString(R.string.location)))).text

        uiObject  = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_apply_button))))
        uiObject.click()

        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[5]
        val navList : List<UiObject2> = device.findObjects(By.res(resourceId))
        for(i in navList){
            assert(i.text.equals(filterAddress))
        }

    }

    @Test
    fun validateSearchNoResults(){
        launchMainActivity()
        var uiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.search_icon))))
        uiObject.click()
        Thread.sleep(1000)

        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.search_edit_text))))
        uiObject.setText("2017")
        Thread.sleep(500)
        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[0]
        val navList : List<UiObject2> = device.findObjects(By.res(resourceId))
        Assert.assertTrue(navList.size == 0)
    }

    /*
    to check search match found by order id
     */
    @Test
    fun validateSearchResults(){
        launchMainActivity()
        val orderId = resources.getStringArray(R.array.taskListRecyclerItems)[0]
        val orderUiObject: String? = (device.findObject(UiSelector().resourceId(orderId))).text
        var uiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.search_icon))))
        uiObject.click()
        Thread.sleep(1000)
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.search_edit_text))))
        uiObject.setText(orderUiObject)
        Thread.sleep(500)
        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[0]
        val navList : List<UiObject2> = device.findObjects(By.res(resourceId))
        Assert.assertTrue(navList.size >0)
    }
    @Test
    fun validateSMSAction() {
        launchMainActivity()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)
        Thread.sleep(2000)
        var smsId: String = resources.getString(R.string.action_message)
        var smsUiObject: UiObject = (device.findObject(UiSelector().resourceId(smsId)))
        smsUiObject.click()
        Thread.sleep(2000)
        assert(device.findObject(UiSelector().textContains("Thanks")).exists()||device.findObject(UiSelector().textContains("call")).exists())
    }

    @Test
    fun validateSorting () {
        launchMainActivity()
        var uiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.sorting_icon))))
        uiObject.click()
        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[2]
        val navList : List<UiObject2> = device.findObjects(By.res(resourceId))
        Assert.assertTrue(navList.size >0)
        var str : String =""
        for(i in navList){
            var result =(i.text.compareTo(str))
            if(result>=0){
                assert(true)
                str = i.text
            }
            else{
                assert(false)
                break
            }
        }

    }

    private fun checkViewIdis(): Boolean {
        val viewIdis = intArrayOf(
                R.id.taskDetail,
                R.id.task_id,
                R.id.quantity,
                R.id.status,
                R.id.receiverName,
                R.id.lable_task_detail_pickup,
                R.id.lable_task_detail_drop,
                R.id.task_date,
                R.id.lable_task_detail_order_memo,
                R.id.input_task_detail_order_memo,
                R.id.priceDetails,
                R.id.cod_label,
                R.id.cod_value

        )
        for (id in viewIdis) {
            Espresso.onView(ViewMatchers.withId(id)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        return true
    }





    fun swiper(start: Int, end: Int, delay: Int) {
        val downTime: Long = SystemClock.uptimeMillis()
        var eventTime: Long = SystemClock.uptimeMillis()
        val inst: Instrumentation = InstrumentationRegistry.getInstrumentation()
        var event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 500f, start.toFloat(), 0)
        inst.sendPointerSync(event)
        eventTime = SystemClock.uptimeMillis() + delay
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, 500f, end.toFloat(), 0)
        inst.sendPointerSync(event)
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, 500f, end.toFloat(), 0)
        inst.sendPointerSync(event)
        SystemClock.sleep(2000) //The wait is important to scroll
    }
}