package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import org.junit.Test
import ymsli.com.couriemate.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class DoneFragmentAuromatorTest  : BaseAutomatorTest() {
    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    /*
          to check the presence of
          any task in list
       */
    @Test
    fun validateDoneTaskListView() {
        launchMainActivity()
        device.findObject(By.text("DONE")).click()
        Thread.sleep(1000)
        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var uiObject = (device.findObject(UiSelector().resourceId(resourceId)))
        if (!uiObject.exists()) {
            val noDataId = resources.getString(R.id.text_no_data_task_list)
            val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
            assert(noDataIdUiObject.exists())
        }
        else
            assert(true)
    }

    /*
          to check the filter action in case of delivered
       */
    @Test
    fun validateFilterActionsDelivered(){
        launchMainActivity()

        // Espresso.onView(ViewMatchers.withText("DONE")).perform(ViewActions.click())
        device.findObject(By.text("DONE")).click()
        Thread.sleep(1000)
        var uiObject : UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        uiObject.click()
        uiObject  = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_delivered))))
        uiObject.click()
        uiObject  = (device.findObject(UiSelector().resourceId(resources.getString(R.string.from_filter_dialog))))
        uiObject.click()
        device.findObject(By.text("1")).click()
        device.findObject(By.text("OK")).click()
        uiObject  = (device.findObject(UiSelector().resourceId(resources.getString(R.string.to_filter_dialog))))
        uiObject.click()
        var today= LocalDate
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

        device.findObject(By.text("Apply")).click()

        val endDateTextId = resources.getStringArray(R.array.doneViewRecyclerList)[3]
        if((device.findObject(UiSelector().resourceId(endDateTextId))).exists()) {
            val toDateString: String? = (device.findObject(UiSelector().resourceId(endDateTextId))).text
            val toDate: Date = SimpleDateFormat("yyyy/mm/dd").parse(toDateString)

            val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
            uiObject = (device.findObject(UiSelector().resourceId(resourceId)))

            if (!uiObject.exists()) {
                val noDataId = resources.getString(R.id.text_no_data_task_list)
                val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
                assert(noDataIdUiObject.exists())
            }
            else{

                var deliveredText  =  device.findObject(By.text("DELIVERED"))
                var refusedText =   device.findObject(By.text("REFUSED"))

                if((deliveredText!=null && refusedText==null)&&((toDate.after(date1) && toDate.before(date2))||(toDate.equals(date1)||toDate.equals(date2)))){

                    assert(true)
                }
                else{
                    assert(false)
                }
            }
        }
        else{
            assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.no_data_list))).exists())
        }
    }

    /*
          to check the filter action in case of refused
       */
    @Test
    fun validateFilterActionsRefused() {
        launchMainActivity()
        device.findObject(By.text("DONE")).click()
        Thread.sleep(1000)
        var uiObject: UiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_icon))))
        uiObject.click()
        uiObject = (device.findObject(UiSelector().resourceId(resources.getString(R.string.filter_dialog_refused))))
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
            val toDateString: String? = (device.findObject(UiSelector().resourceId(endDateTextId))).text
            val toDate: Date = SimpleDateFormat("yyyy/mm/dd").parse(toDateString)

            var refusedText = device.findObject(By.text("REFUSED"))
            print(refusedText)
            if (refusedText == null) {
                var noDataId= device.findObject(By.text("No tasks available"))
                if(noDataId==null){
                    assert(false)
                }
                else{
                    assert(true)
                }

            }
            else {

                var deliveredText  =  device.findObject(By.text("DELIVERED"))
                var refusedText =   device.findObject(By.text("REFUSED"))

                if((refusedText!=null && deliveredText==null)&&((toDate.after(date1) && toDate.before(date2))||(toDate.equals(date1)||toDate.equals(date2)))){

                    assert(true)
                }
                else{
                    assert(false)
                }
            }
        }
        else{
            assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.no_data_list))).exists())
        }
    }


    /*
     to check call action
     after swiping on any delivered task
  */
    @Test
    fun validateCallActionOnSwipeOnDone() {
        launchMainActivity()
        device.findObject(By.text("DONE")).click()
        Thread.sleep(1000)
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).swipeLeft(5)
        Thread.sleep(3000)
        assert((device.findObject(UiSelector().descriptionContains("#")).exists()))
    }

    /*
       to check call action
         after clicking on any delivered task
      */
    @Test
    fun validateCallAction() {
        launchMainActivity()
        device.findObject(By.text("DONE")).click()
        Thread.sleep(1000)
        val statusId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        (device.findObject(UiSelector().resourceId(statusId))).click()
        Thread.sleep(2000)

        var callId: String = resources.getString(R.string.action_call)
        (device.findObject(UiSelector().resourceId(callId))).click()
        assert((device.findObject(UiSelector().descriptionContains("#")).exists()))
    }
    /*
  to check order details
*/
    @Test
    fun validateOrderDetails() {
        launchMainActivity()
        device.findObject(By.text("DONE")).click()
        Thread.sleep(1000)
        val resourceId = resources.getStringArray(R.array.taskListRecyclerItems)[1]
        var uiObject = (device.findObject(UiSelector().resourceId(resourceId)))

        if (!uiObject.exists()) {
            val noDataId = resources.getString(R.id.text_no_data_task_list)
            val noDataIdUiObject: UiObject = (device.findObject(UiSelector().resourceId(noDataId)))
            assert(noDataIdUiObject.exists())
        }
        else{
            var statusUiObject3: UiObject2 = device.findObject(By.text("DELIVERED"))
            if(statusUiObject3!=null){
                statusUiObject3.click()
                assert(checkViewIdisDelivered())
            }
            else{
                statusUiObject3 = device.findObject(By.text("REFUSED"))
                statusUiObject3.click()
                assert(checkViewIdisDelivered())
            }
        }
    }
    private fun checkViewIdisDelivered(): Boolean {
        val viewIdis = intArrayOf(
                R.id.taskDetail,
                R.id.task_id,
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



}