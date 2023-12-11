package ymsli.ymsli.couriemate.uiautomatortests

import ymsli.com.couriemate.views.login.LoginActivity
import ymsli.com.couriemate.views.main.MainActivity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers
import ymsli.com.couriemate.R
import ymsli.com.couriemate.views.tasklist.fragment.OngoingTasksFragment

abstract class BaseAutomatorTest {

    lateinit var TAG : String
    lateinit var context : Context
    lateinit var device: UiDevice
    lateinit var resources : Resources

    fun injectDependencies(){
        TAG = generateTag()
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        context = InstrumentationRegistry.getInstrumentation().targetContext
        resources = context.resources
    }

    /**
     * launches login activity
     */
    protected open fun launchLoginActivity(){
        val loginActivityIntent = Intent(context, LoginActivity::class.java)
        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(loginActivityIntent)
        Thread.sleep(3000)
    }

    /**
     * launches main activity
     */
    protected open fun launchMainActivity(){
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(mainActivityIntent)
        Thread.sleep(3000)
    }

    /**
     * to move from assigned task fragment to ongoing task fragment
     */

    protected  open  fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"
            override fun getConstraints() = CoreMatchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TabLayout::class.java))
            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }

     protected open fun openNavDrawer() {
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())
    }

    protected abstract fun generateTag(): String
}