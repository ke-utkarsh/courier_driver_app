package ymsli.ymsli.couriemate.espressotests

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import ymsli.com.couriemate.R
import ymsli.com.couriemate.TestComponentRule
import ymsli.com.couriemate.views.main.MainActivity


class TaskSummaryTest {
    private val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    private val main = IntentsTestRule(MainActivity::class.java, false, false)

    @get:Rule
    val chain = RuleChain.outerRule(component).around(main)

    @Before
    fun setupTest() {
        main.launchActivity(
                Intent(component.getContext(),
                        MainActivity::class.java)
        )
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
    }

    /*
     to check 3 days button is enabled in dropdown menu
     */
    @Test
    fun check3DaysInTaskSummary() {
        onView(withText("Task Summary")).perform(click())
        onView(withId(R.id.spinner_task_summary)).perform(click())
        onView(withText("3 Days")).perform(click())
        assert(checkViewIdis())
    }

    /*
    to check 1 week button is enabled in dropdown menu
    */
    @Test
    fun check1WeekInTaskSummary() {
        onView(withText("Task Summary")).perform(click())
        onView(withId(R.id.spinner_task_summary)).perform(click())
        onView(withText("1 Week")).perform(click())
        assert(checkViewIdis())
    }

    /*
    to check 1 month button is enabled in dropdown menu
    */
    @Test
    fun check1MonthInTaskSummary() {
        onView(withText("Task Summary")).perform(click())
        onView(withId(R.id.spinner_task_summary)).perform(click())
        onView(withText("1 Month")).perform(click())
        assert(checkViewIdis())
    }
    /*
       to check all view id present on
       task summary screen
     */
    private fun checkViewIdis(): Boolean {
        val viewIdis = intArrayOf(
                R.id.spinner_task_summary,
                R.id.label_task_summary_assigned,
                R.id.label_task_summary_delivering,
                R.id.label_task_summary_delivered,
                R.id.label_task_summary_failed,
                R.id.label_task_summary_refused,
                R.id.label_task_summary_returned,
                R.id.label_task_summary_total,
                R.id.label_task_summary_total_delivery_fee,
                R.id.label_task_summary_total_cash,
                R.id.label_task_summary_total_other_payment,
                R.id.tv_task_summary_assigned,
                R.id.tv_task_summary_delivering,
                R.id.tv_task_summary_delivered,
                R.id.tv_task_summary_failed,
                R.id.tv_task_summary_refused,
                R.id.tv_task_summary_returned,
                R.id.tv_task_summary_total,
                R.id.tv_task_summary_total_delivery_fee,
                R.id.tv_task_summary_total_cash,
                R.id.tv_task_summary_total_other_payment,
                R.id.tv_task_summary_total_other_payment,
                R.id.label_task_summary_total_header
        )

        for (id in viewIdis) {
            onView(withId(id)).check(matches(isDisplayed()))
        }

        return true
    }
}