package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.com.couriemate.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


@RunWith(AndroidJUnit4::class)
@LargeTest
class OngoingFragmentAutomatorTest : BaseAutomatorTest() {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    /*
          to check the presence of
          any task in list
       */
    @Test
    fun validateOngoingTaskListView() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var uiObject = (device.findObject(UiSelector().resourceId(resourceId)))

        if (!uiObject.exists()) {
            val noDataId = resources.getString(R.id.text_no_data_task_list)
            val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
            assert(noDataIdUiObject.exists())
        }

        assert(true)
    }
    /*
     to check order details in delivered
  */
    @Test
    fun validateOrderDetailsDelivered() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val statusUiObject2: UiObject2 = device.findObject(By.text("DELIVERING"))
        val statusUiObject3: UiObject2 = device.findObject(By.text("FAILED"))
        if (statusUiObject2.text.equals("DELIVERING", true) || (statusUiObject3.text.equals("FAILED", true))) {
            if(statusUiObject2!=null)
                statusUiObject2.click()
            else
                statusUiObject3.click()
            assert(checkViewIdis())
            Thread.sleep(2000)
            onView(withText("CONTINUE")).perform(ViewActions.scrollTo(), ViewActions.click());
        }
        else{
            throw Exception("No delivering tasks for testing!!")
        }


    }

    /*
 to check order details in refused
*/
    @Test
    fun validateOrderDetailsRefused() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val items = device.findObjects(By.textContains("DELIVERING"))
        if(items.size>0){
            items.get(0).click()
            var statusUiObject4 = device.findObject(By.text("REFUSED"))
            if(statusUiObject4!=null) {
                Thread.sleep(2000)
                statusUiObject4.click()
                assert(checkViewIdisRefused())

                onView(withText("SUBMIT")).perform(ViewActions.scrollTo(), ViewActions.click());

            }
            else{
                assert(checkViewIdisDelivering())
                Thread.sleep(2000)
                onView(withId(R.id.continue_button)).perform(
                    ViewActions.scrollTo(),
                    ViewActions.click()
                );

            }
        }
    }

    /*
 to check order details in failed
*/
    @Test
    fun validateOrderDetailsFailed()  {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val statusUiObject2: UiObject2 = device.findObject(By.text("DELIVERING"))
        if(statusUiObject2!=null){
            statusUiObject2.click()
            var statusUiObject4 = device.findObject(By.text("REFUSED"))
            if(statusUiObject4!=null) {
                Thread.sleep(2000)
                statusUiObject4 = device.findObject(By.text("FAILED"))
                statusUiObject4.click()
                assert(checkViewIdisFailed())

                onView(withText("SUBMIT")).perform(ViewActions.scrollTo(), ViewActions.click());

            }
            else{
                assert(checkViewIdisDelivering())
                Thread.sleep(2000)
                onView(withId(R.id.continue_button)).perform(
                    ViewActions.scrollTo(),
                    ViewActions.click()
                );

            }
        }
        else{
            throw Exception("No delivering tasks for testing!!")
        }
    }

    /*
      to check sms action after selecting time,
      after clicking on any delivering task
   */
    @Test
    fun validateLocationActionDelivering() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val statusUiObject2: UiObject2 = device.findObject(By.text("DELIVERING"))
        if (statusUiObject2.text.equals("DELIVERING", true)) {
            statusUiObject2.click()
            Thread.sleep(2000)
            var locationId: String = resources.getString(R.string.action_location)
            var locationUiObject: UiObject = (device.findObject(UiSelector().resourceId(locationId)))
            locationUiObject.click()
            device.findObject(By.text("OK")).click()
            Thread.sleep(2000)

            assert(device.findObject(UiSelector().textContains("http")).exists())

        } else throw Exception("No delivering/failed tasks for testing!!")
    }

    /*
      to check sms action
      after clicking on any delivering task
   */
    @Test
    fun validateSMSAction() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val statusUiObject: UiObject2 = device.findObject(By.text("DELIVERING"))
        if (statusUiObject.text.equals("DELIVERING", true)) {
            statusUiObject.click()
            Thread.sleep(2000)
            var smsId: String = resources.getString(R.string.action_message)
            var smsUiObject: UiObject = (device.findObject(UiSelector().resourceId(smsId)))
            smsUiObject.click()
            Thread.sleep(2000)

            assert(device.findObject(UiSelector().textContains("Thanks")).exists()||device.findObject(UiSelector().textContains("call")).exists())
        } else throw Exception("No delivering tasks for testing!!")
    }

    /*
          to check call action
            after clicking on any  task
         */
    @Test
    fun validateCallAction() {
        launchMainActivity()
        Espresso.onView(ViewMatchers.withText("ONGOING")).perform(ViewActions.click())
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)
        var phoneUiObject: String? = (device.findObject(UiSelector().resourceId("ymsli.com.couriemate:id/phoneNo"))).text
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
            assert((device.findObject(UiSelector().textMatches("^[0-9]$"))).exists())
        }
    }



    /*
     to check call action
     after swiping on any delivered task
  */
    @Test
    fun validateCallActionOnSwipeOnDone() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)

        var phoneUiObject: String? = (device.findObject(UiSelector().resourceId("ymsli.com.couriemate:id/phoneNo"))).text
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
        (device.findObject(UiSelector().resourceId(statusId1))).swipeLeft(5)
        Thread.sleep(1000)
        if(!(device.findObject(UiSelector().text(result))).exists()){
            assert((device.findObject(UiSelector().textMatches("^[0-9]$"))).exists())
        }
    }

    /*
      to check sms action after selecting time,
      after clicking on any failed task
   */
    @Test
    fun validateLocationActionOnFailed() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val items = device.findObjects(By.textContains("FAILED"))
        if (items.size>0) {
            items.get(0).click()
            Thread.sleep(2000)
            var locationId: String = resources.getString(R.string.action_location)
            var locationUiObject: UiObject = (device.findObject(UiSelector().resourceId(locationId)))
            locationUiObject.click()
            Thread.sleep(2000)
            device.findObject(By.text("OK")).click()
            Thread.sleep(2000)
            junit.framework.Assert.assertFalse(device.findObject(UiSelector().textContains("link")).exists())
        }
    }

    /*
      to check sms action
      after clicking on any failed task
   */
    @Test
    fun validateSMSActionOnFailed() {
        launchMainActivity()
        onView(withText("DONE")).perform(ViewActions.click())

        Thread.sleep(1000)
        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var uiObject = (device.findObject(UiSelector().resourceId(resourceId)))
        if (!uiObject.exists()) {
            val noDataId = resources.getString(R.id.text_no_data_task_list)
            val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
            assert(noDataIdUiObject.exists())
        }
        else{
            val items = device.findObjects(By.textContains("FAILED"))
                //device.findObject(By.text("FAILED"))
            if (items.size>0) {
                items.get(0).click()
                Thread.sleep(2000)
                var smsId: String = resources.getString(R.string.action_message)
                var smsUiObject: UiObject = (device.findObject(UiSelector().resourceId(smsId)))
                smsUiObject.click()
                Thread.sleep(2000)
                assert(device.findObject(UiSelector().textContains("Thanks")).exists()||device.findObject(UiSelector().textContains("call")).exists())
            }
        }
    }

    /*
       to check call action
       after clicking on any failed task
    */
    @Test
    fun validateCallActionOnFailed() {
        launchMainActivity()
        onView(withText("ONGOING")).perform(ViewActions.click())
        val statusUiObject: UiObject2 = device.findObject(By.text("FAILED"))
        if (statusUiObject.text.equals("FAILED", true)) {
            statusUiObject.click()
            var phoneUiObject: String? = (device.findObject(UiSelector().resourceId("ymsli.com.couriemate:id/phoneNo"))).text
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
                assert((device.findObject(UiSelector().textMatches("^[0-9]$"))).exists())
            }
        }

        else throw Exception("No delivering tasks for testing!!")
    }

    /*
       to check filter action delivering
    */

    @Test
    fun validateFilterActionsDelivering() {
        launchMainActivity()

        onView(withText("ONGOING")).perform(ViewActions.click())
        var uiObject: UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        uiObject.click()
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_delivering))))
        uiObject.click()
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.from_filter_dialog))))
        uiObject.click()
        device.findObject(By.text("1")).click()
        device.findObject(By.text("OK")).click()
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.to_filter_dialog))))
        uiObject.click()
        var today = LocalDate
                .now()
                .getDayOfMonth()

        device.findObject(By.text(today.toString())).click()
        device.findObject(By.text("OK")).click()

        val fromDateTextId = resources.getStringArray(R.array.filterRecyclerList)[4]
        val fromDateTextUiObject: String? = (device.findObject(UiSelector().resourceId(fromDateTextId))).text
        val date1: Date = SimpleDateFormat("yyyy/mm/dd").parse(fromDateTextUiObject)

        val toDateTextId = resources.getStringArray(R.array.filterRecyclerList)[6]
        val toTextUiObject: String? = (device.findObject(UiSelector().resourceId(toDateTextId))).text
        val date2: Date = SimpleDateFormat("yyyy/mm/dd").parse(toTextUiObject)

        Thread.sleep(1000)
        device.findObject(By.text("Apply")).click()

        val endDateTextId = resources.getStringArray(R.array.doneViewRecyclerList)[3]
        val toDateString: String? = (device.findObject(UiSelector().resourceId(endDateTextId))).text
        val toDate: Date = SimpleDateFormat("yyyy/mm/dd").parse(toDateString)


        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        uiObject = (device.findObject(UiSelector().resourceId(resourceId)))

        if (!uiObject.exists()) {
            val noDataId = resources.getString(R.id.text_no_data_task_list)
            val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
            assert(noDataIdUiObject.exists())
        } else {

            var deliveringText = device.findObject(By.text("DELIVERING"))
            var failedText = device.findObject(By.text("FAILED"))

            if ((deliveringText != null && failedText == null) && (toDate.after(date1) && toDate.before(date2))) {

                assert(true)
            } else {
                assert(false)
            }
        }


    }
    /*
       to check filter action failed
    */

    @Test
    fun validateFilterActionsFailed() {
        launchMainActivity()

        onView(withText("ONGOING")).perform(ViewActions.click())
        var uiObject: UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        uiObject.click()
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_failed))))
        uiObject.click()
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.from_filter_dialog))))
        uiObject.click()
        device.findObject(By.text("1")).click()
        device.findObject(By.text("OK")).click()
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.to_filter_dialog))))
        uiObject.click()
        var today = LocalDate
                .now()
                .getDayOfMonth()

        device.findObject(By.text(today.toString())).click()
        device.findObject(By.text("OK")).click()

        val fromDateTextId = resources.getStringArray(R.array.filterRecyclerList)[4]
        val fromDateTextUiObject: String? = (device.findObject(UiSelector().resourceId(fromDateTextId))).text
        val date1: Date = SimpleDateFormat("yyyy/mm/dd").parse(fromDateTextUiObject)

        val toDateTextId = resources.getStringArray(R.array.filterRecyclerList)[6]
        val toTextUiObject: String? = (device.findObject(UiSelector().resourceId(toDateTextId))).text
        val date2: Date = SimpleDateFormat("yyyy/mm/dd").parse(toTextUiObject)

        Thread.sleep(1000)
        device.findObject(By.text("Apply")).click()

        val endDateTextId = resources.getStringArray(R.array.doneViewRecyclerList)[3]
        if((device.findObject(UiSelector().resourceId(endDateTextId))).exists()) {

            val toDateString: String? =
                (device.findObject(UiSelector().resourceId(endDateTextId))).text
            val toDate: Date = SimpleDateFormat("yyyy/mm/dd").parse(toDateString)


            val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
            uiObject = (device.findObject(UiSelector().resourceId(resourceId)))

            if (!uiObject.exists()) {
                val noDataId = resources.getString(R.id.text_no_data_task_list)
                val noDataIdUiObject: UiObject =
                    (device.findObject(UiSelector().resourceId(noDataId)))
                assert(noDataIdUiObject.exists())
            } else {

                var deliveringText = device.findObject(By.text("DELIVERING"))
                var failedText = device.findObject(By.text("FAILED"))

                if ((deliveringText == null && failedText != null) && (toDate.after(date1) && toDate.before(
                        date2
                    ))
                ) {

                    assert(true)
                } else {
                    assert(false)
                }
            }
        }
        else{
            assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.no_data_list))).exists())
        }
    }

    /*
to check no search match found
 */
    @Test
    fun validateSearchNoResults(){
        launchMainActivity()
        (device.findObject(UiSelector().text("ONGOING"))).click()
        var uiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.search_icon))))
        uiObject.click()
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
        (device.findObject(UiSelector().text("ONGOING"))).click()
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

    private fun checkViewIdisRefused(): Boolean {
        val viewIdis = intArrayOf(
                R.id.taskDetail,
                R.id.task_id,
                R.id.receiverName,
                R.id.lable_task_detail_pickup,
                R.id.lable_task_detail_pickup,
                R.id.task_date,
                R.id.lable_task_detail_order_memo,
                R.id.input_task_detail_order_memo,
                R.id.priceDetails,
                R.id.deliver,
                R.id.refuse,
                R.id.fail,
                R.id.reason_label,
                R.id.reason_spinner
        )
        for (id in viewIdis) {
            onView(withId(id)).check(ViewAssertions.matches(isDisplayed()))
        }
        return true
    }
    private fun checkViewIdisFailed(): Boolean {
        val viewIdis = intArrayOf(
                R.id.taskDetail,
                R.id.task_id,
                R.id.receiverName,
                R.id.lable_task_detail_pickup,
                R.id.lable_task_detail_pickup,
                R.id.task_date,
                R.id.lable_task_detail_order_memo,
                R.id.input_task_detail_order_memo,
                R.id.priceDetails,
                R.id.deliver,
                R.id.refuse,
                R.id.fail ,
                R.id.selectReason,
                R.id.dateforpickup,
                R.id.failure_comments



        )
        for (id in viewIdis) {
            onView(withId(id)).check(ViewAssertions.matches(isDisplayed()))
        }
        return true
    }

    private fun checkViewIdis(): Boolean {
        val viewIdis = intArrayOf(
                R.id.taskDetail,
                R.id.task_id,
                R.id.receiverName,
                R.id.lable_task_detail_pickup,
                R.id.lable_task_detail_pickup,
                R.id.task_date,
                R.id.lable_task_detail_order_memo,
                R.id.input_task_detail_order_memo,
                R.id.priceDetails,
                R.id.deliver,
                R.id.refuse,
                R.id.fail,
                R.id.amountReceived,
                R.id.mm_text,
                R.id.amount_value,
                R.id.mm_spinner,
                R.id.mm_editText,
                R.id.task_memo_label,
                R.id.btn_add_sign
        )
        for (id in viewIdis) {
            onView(withId(id)).check(ViewAssertions.matches(isDisplayed()))
        }
        return true
    }

    private fun  checkViewIdisDelivering(): Boolean {
        val viewIdis = intArrayOf(
            R.id.taskDetail,
            R.id.task_id,
            R.id.receiverName,
            R.id.action_message,
            R.id.action_call,
            R.id.lable_task_detail_pickup,
            R.id.lable_task_detail_drop,
            R.id.task_date,
            R.id.phoneNo,
            R.id.map,
            R.id.address,
            R.id.lable_task_detail_order_memo,
            R.id.input_task_detail_order_memo,
            R.id.deliver,
            R.id.fail


        )
        for (id in viewIdis) {
            onView(withId(id)).check(ViewAssertions.matches(isDisplayed()))
        }
        return true
    }
}