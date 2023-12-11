package ymsli.ymsli.couriemate.espressotests.taskdetailtests

import ymsli.com.couriemate.R
import ymsli.com.couriemate.TestComponentRule
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.views.taskdetail.TaskDetailActivity
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

/**
 * validates UI of the task detail activity
 * for end customer
 */
class TaskDetailEndCustomerTest {

    private val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    private val main = IntentsTestRule(TaskDetailActivity::class.java,false,false)

    @get:Rule
    val chain = RuleChain.outerRule(component).around(main)

    @Before
    fun setupTest(){
        val temp = Intent(component.getContext(),TaskDetailActivity::class.java)
        val taskRet = TaskRetrievalResponse(
            taskId = 45,
            completed = 2,
            dropAddress = "Old Fbd",
            failureCount = 0,
            taskSequenceNo = 3,
            maxTaskSequence = 3,
            expectedCodAmount = 400.0,
            orderId = 78,
            taskStatusId = 3,
            expectedDelivery = "2019-11-23T05:05:57.261+0000",
            quantity = 1
        )
        temp.putExtra("SelectedTask",taskRet)
        main.launchActivity(temp)
    }

    @Test
    fun checkEndCustomerDelivery(){
        //checks delivery
        onView(withId(R.id.deliver))
            .check(matches(isDisplayed()))
        onView(withId(R.id.refuse))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fail))
            .check(matches(isDisplayed()))
        onView(withId(R.id.amount_value)).check(matches(isEnabled()))
        onView(withId(R.id.signature_fragment)).check(matches(isEnabled()))

        //checks refusal
        onView(withId(R.id.refuse)).perform(ViewActions.click())
        onView(withId(R.id.explain)).check(matches(isEnabled()))

        //checks failure
        onView(withId(R.id.fail)).perform(ViewActions.click())
        onView(withId(R.id.failure_comments)).check(matches(isEnabled()))
        onView(withId(R.id.fail_reason_fragment)).perform(ViewActions.click())
        onView(withText("Absence")).perform(ViewActions.click())
        onView(withId(R.id.dateforpickup)).perform(ViewActions.click())
        onView(withId(android.R.id.button1))
            .perform(ViewActions.click())
        onView(withId(android.R.id.button1))
            .perform(ViewActions.click())
        onView(withId(R.id.failure_comments)).perform(ViewActions.typeText("This delivery has failed"))
        onView(withId(R.id.fail_submit_btn)).check(matches(isEnabled()))
    }
}