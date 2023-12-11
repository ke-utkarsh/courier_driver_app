package ymsli.ymsli.couriemate.espressotests.taskdetailtests

import ymsli.com.couriemate.R
import ymsli.com.couriemate.TestComponentRule
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ymsli.com.couriemate.database.entity.TaskRetrievalResponse
import ymsli.com.couriemate.views.taskdetail.TaskDetailActivity
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class TaskDetailAssignedTest {

    private val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    private val main = IntentsTestRule(TaskDetailActivity::class.java,false,false)

    @get:Rule
    val chain = RuleChain.outerRule(component).around(main)

    @Before
    fun setupTest(){
        val temp = Intent(component.getContext(),TaskDetailActivity::class.java)
        val taskRet = TaskRetrievalResponse(
            taskId = 454,
            completed = 2,
            dropAddress = "New Fbd",
            failureCount = 0,
            taskSequenceNo = 1,
            maxTaskSequence = 3,
            expectedCodAmount = 0.0,
            orderId = 7,
            taskStatusId = 2,
            expectedDelivery = "2019-11-23T05:05:57.261+0000",
            quantity = 1
        )
        temp.putExtra("SelectedTask",taskRet)
        main.launchActivity(temp)
    }

    @Test
    fun checkAssignedTaskUI(){
        onView(withId(R.id.deliver))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.refuse))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.fail))
            .check(matches(not(isDisplayed())))
    }
}