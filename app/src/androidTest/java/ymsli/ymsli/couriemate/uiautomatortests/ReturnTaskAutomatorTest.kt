package ymsli.ymsli.couriemate.uiautomatortests

import android.icu.text.SimpleDateFormat
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.com.couriemate.R
import java.lang.Exception
import java.time.LocalDate
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class ReturnTaskAutomatorTest :BaseAutomatorTest() {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    /*
          to test presence of
           delivered task list
         */
    @Test
    fun validateDeliveredList() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        val resId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        val uiObject: UiObject = (device.findObject(UiSelector().resourceId(resId)))
        if (uiObject.text.equals("DELIVERED", true)) {
            assert(true)
        } else throw Exception("No Delivered tasks for testing!!")
    }

    /*
      to check delivered
      card ui component present
     */
    @Test
    fun validateDeliveredView() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        val elements = resources.getStringArray(R.array.deliveredAndReturnedRecyclerList)
        for (e in elements) {
            Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
        }
    }

    /*
      to check delivered
      details ui component present
     */
    @Test
    fun validateDeliveredDetail() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        val statusUiObject: UiObject = (device.findObject(UiSelector().resourceId(statusId)))
        statusUiObject.click()
        Thread.sleep(2000)
        val elements = resources.getStringArray(R.array.deliveredDetailsRecyclerItemsVisibleCheck)
        for (e in elements) {
            Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
        }
    }

    /*
      to check call action
      after swiping on any delivered task
   */
    @Test
    fun validateCallActionOnSwipeOnDelivered() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)
        val phoneId = resources.getStringArray(R.array.deliveredDetailsRecyclerItems)[6]
        var phoneUiObject: String? = (device.findObject(UiSelector().resourceId(phoneId))).text
        phoneUiObject=phoneUiObject?.replace("-"," ")
        var result = buildString {
            for (i in 0 until phoneUiObject!!.length) {
                if (i==8) {
                    append(' ')
                }
                append(phoneUiObject[i])
            }
        }
        Espresso.pressBack();
        val statusId1 = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId1))).swipeLeft(20)
        if(!(device.findObject(UiSelector().text(result))).exists()){
            assert((device.findObject(UiSelector().textMatches("^[0-9]\$"))).exists())
        }
    }

    /*
       to check call action
         after clicking on any delivered task
      */
    @Test
    fun validateCallActionDelivered() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)
        val phoneId = resources.getStringArray(R.array.deliveredDetailsRecyclerItems)[6]
        var phoneUiObject: String? = (device.findObject(UiSelector().resourceId(phoneId))).text
        phoneUiObject=phoneUiObject?.replace("-"," ")
        val result = buildString {
            for (i in 0 until phoneUiObject!!.length) {
                if (i==8) {
                    append(' ')
                }
                append(phoneUiObject[i])
            }
        }
        var callId: String = resources.getString(R.string.action_call)
        (device.findObject(UiSelector().resourceId(callId))).click()
        if(!(device.findObject(UiSelector().text(result))).exists()){
            assert((device.findObject(UiSelector().textMatches("^[0-9]\$"))).exists())
        }
    }

    /*
    to check no search match found
     */
    @Test
    fun validateDeliveredSearchNoResults(){
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
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
    fun validateDeliveredSearchResults(){
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
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

    /*
        to check filter ui components in delivered task
     */
    @Test
    fun validateDeliveredFilter(){
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon)))).click()
        val elements = resources.getStringArray(R.array.filterRecyclerList)
        for (e in elements) {
            Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
        }
    }

    /*
       to check filter action in delivered task
    */
    @Test
    fun validateDeliveredFromDateToDateFilterAction(){
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon)))).click()

        val fromDateId = resources.getStringArray(R.array.filterRecyclerList)[4]
        (device.findObject(UiSelector().resourceId(fromDateId))).click()
        (device.findObject(UiSelector().text("5"))).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_filter_ok_btn)))).click()

        val fromDateTextId = resources.getStringArray(R.array.filterRecyclerList)[4]
        val fromDateTextUiObject: String? = (device.findObject(UiSelector().resourceId(fromDateTextId))).text
        val date1: Date = SimpleDateFormat("yyyy/mm/dd").parse(fromDateTextUiObject)

        var toDateUiid = resources.getStringArray(R.array.filterRecyclerList)[6]
        (device.findObject(UiSelector().resourceId(toDateUiid))).click()
        var toDateId = LocalDate
                .now()
                .getDayOfMonth()
        device.findObject(By.text(toDateId.toString())).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_filter_ok_btn)))).click()

        val toDateTextId = resources.getStringArray(R.array.filterRecyclerList)[6]
        val toTextUiObject: String? = (device.findObject(UiSelector().resourceId(toDateTextId))).text
        val date2: Date = SimpleDateFormat("yyyy/mm/dd").parse(toTextUiObject)

        val applyButtonId = resources.getStringArray(R.array.filterRecyclerList)[7]
        (device.findObject(UiSelector().resourceId(applyButtonId))).click()

        val resId = resources.getStringArray(R.array.deliveredAndReturnedRecyclerList)[1]
        val uiObject: UiObject = (device.findObject(UiSelector().resourceId(resId)))
        if(uiObject.exists()) {
            val fromDateUIId = resources.getStringArray(R.array.deliveredAndReturnedRecyclerList)[3]
            val fromDateString: String? = (device.findObject(UiSelector().resourceId(fromDateUIId))).text
            val fromDate: Date = SimpleDateFormat("yyyy/mm/dd").parse(fromDateString)
            assert((fromDate.after(date1) && fromDate.before(date2))||(fromDate.equals(date1)||fromDate.equals(date2)))
        }else if(!uiObject.exists()) {
            var noDataIdUiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.no_data_list))))
            assert(noDataIdUiObject.exists())
        }else{
            assert(false)
        }
    }

    /*
      to test presence of
      returned task list
    */
    @Test
    fun validateReturnedList() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
        val resId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        val uiObject: UiObject = (device.findObject(UiSelector().resourceId(resId)))
        if (uiObject.text.equals("RETURNED", true)) {
            assert(true)
        } else throw Exception("No Returned tasks for testing!!")
    }

    /*
     to check returned task
     card ui component present
    */
    @Test
    fun validateReturnedView() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
        val elements = resources.getStringArray(R.array.deliveredAndReturnedRecyclerList)
        for (e in elements) {
            Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
        }
    }

    /*
    to check returned
    details ui component present
   */
    @Test
    fun validateReturnedDetail() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        val statusUiObject: UiObject = (device.findObject(UiSelector().resourceId(statusId)))
        statusUiObject.click()
        Thread.sleep(2000)
        val elements = resources.getStringArray(R.array.returnedDetailsRecyclerItems)
        for (e in elements) {
            Assert.assertTrue(device.findObjects(By.res(e)).size >=0)
        }
    }

    /*   to check call action
       after swiping on any returned task
    */
    @Test
    fun validateCallActionOnSwipeOnReturned() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)
        val phoneId = resources.getStringArray(R.array.returnedDetailsRecyclerItems)[6]
        var phoneUiObject: String? = (device.findObject(UiSelector().resourceId(phoneId))).text
        phoneUiObject=phoneUiObject?.replace("-"," ")
        val result = buildString {
            for (i in 0 until phoneUiObject!!.length) {
                if (i==8) {
                    append(' ')
                }
                append(phoneUiObject[i])
            }
        }
        Espresso.pressBack();
        val statusId1 = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId1))).swipeLeft(20)
        if(!(device.findObject(UiSelector().text(result))).exists()){
            assert((device.findObject(UiSelector().textMatches("^[0-9]\$"))).exists())
        }
    }

    /*
          to check call action
          after clicking on any RETURNED task
       */
    @Test
    fun validateCallActionReturned() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)
        val phoneId = resources.getStringArray(R.array.returnedDetailsRecyclerItems)[6]
        var phoneUiObject: String? = (device.findObject(UiSelector().resourceId(phoneId))).text
        phoneUiObject=phoneUiObject?.replace("-"," ")
        val result = buildString {
            for (i in 0 until phoneUiObject!!.length) {
                if (i==8) {
                    append(' ')
                }
                append(phoneUiObject[i])
            }
        }
        var callId: String = resources.getString(R.string.action_call)
        (device.findObject(UiSelector().resourceId(callId))).click()
        Thread.sleep(5000)
        if(!(device.findObject(UiSelector().text(result))).exists()){
            assert((device.findObject(UiSelector().textMatches("^[0-9]\$"))).exists())
        }
    }

    /*
    to check no search match found
     */
    @Test
    fun validateReturnedSearchNoResults(){
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
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
    fun validateReturnedSearchResults(){
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
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

    /*
        to check filter ui components in returned task
     */
    @Test
    fun validateReturnedFilter(){
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        (device.findObject(UiSelector().text("RETURNED"))).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon)))).click()
        val elements = resources.getStringArray(R.array.filterRecyclerList)
        for (e in elements) {
            Assert.assertTrue(device.findObjects(By.res(e)).size > 0)
        }
    }

    /*
       to check filter action in returned task
    */
    @Test
    fun validateReturnedFromDateToDateFilterAction() {
        launchMainActivity()
        openNavDrawer()
        (device.findObject(UiSelector().text("Return Tasks"))).click()
        Thread.sleep(5000)
        (device.findObject(UiSelector().text("RETURNED"))).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon)))).click()

        val fromDateId = resources.getStringArray(R.array.filterRecyclerList)[4]
        (device.findObject(UiSelector().resourceId(fromDateId))).click()
        (device.findObject(UiSelector().text("1"))).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_filter_ok_btn)))).click()

        val fromDateTextId = resources.getStringArray(R.array.filterRecyclerList)[4]
        val fromDateTextUiObject: String? = (device.findObject(UiSelector().resourceId(fromDateTextId))).text
        val date1: Date = SimpleDateFormat("yyyy/mm/dd").parse(fromDateTextUiObject)

        var toDateUiid = resources.getStringArray(R.array.filterRecyclerList)[6]
        (device.findObject(UiSelector().resourceId(toDateUiid))).click()
        var toDateId = LocalDate
                .now()
                .getDayOfMonth()
        device.findObject(By.text(toDateId.toString())).click()
        (device.findObject(UiSelector().resourceId(resources.getString(R.string.action_filter_ok_btn)))).click()

        val toDateTextId = resources.getStringArray(R.array.filterRecyclerList)[6]
        val toTextUiObject: String? = (device.findObject(UiSelector().resourceId(toDateTextId))).text
        val date2: Date = SimpleDateFormat("yyyy/mm/dd").parse(toTextUiObject)

        val applyButtonId = resources.getStringArray(R.array.filterRecyclerList)[7]
        (device.findObject(UiSelector().resourceId(applyButtonId))).click()

        val resId = resources.getStringArray(R.array.deliveredAndReturnedRecyclerList)[1]
        val uiObject: UiObject = (device.findObject(UiSelector().resourceId(resId)))
        if(uiObject.exists()) {
            val fromDateUIId = resources.getStringArray(R.array.deliveredAndReturnedRecyclerList)[3]
            val fromDateString: String? = (device.findObject(UiSelector().resourceId(fromDateUIId))).text
            val fromDate: Date = SimpleDateFormat("yyyy/mm/dd").parse(fromDateString)
            assert((fromDate.after(date1) && fromDate.before(date2))||(fromDate.equals(date1)||fromDate.equals(date2)))
        }else if(!uiObject.exists()) {
            var noDataIdUiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.no_data_list))))
            assert(noDataIdUiObject.exists())
        }else{
            assert(false)
        }
    }
}

