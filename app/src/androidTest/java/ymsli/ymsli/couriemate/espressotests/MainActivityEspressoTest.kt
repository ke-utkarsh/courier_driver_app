package ymsli.ymsli.couriemate.espressotests

import ymsli.com.couriemate.R
import ymsli.com.couriemate.TestComponentRule
import ymsli.com.couriemate.views.main.MainActivity
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class MainActivityEspressoTest {

    private val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    private val main = IntentsTestRule(MainActivity::class.java, false, false)

    @get:Rule
    val chain = RuleChain.outerRule(component).around(main)

    @Test
    fun validateDateFilter(){
        main.launchActivity(
            Intent(component.getContext(),
                MainActivity::class.java)
        )

        Espresso.onView(ViewMatchers.withId(R.id.filter)).perform(click())
        //TODO calendar testing
    }
}