package ymsli.ymsli.couriemate.espressotests

import android.content.Intent

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import ymsli.com.couriemate.R
import ymsli.com.couriemate.TestComponentRule
import ymsli.com.couriemate.views.main.MainActivity

class TransactionRegistrationEspressoTest {

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
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open())

    }
    /* check transaction registration in nav drawer */
    @Test
    fun checkTransactionRegistrationisDisplayed(){
       Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).check((matches(isDisplayed())))
    }
    /* check transaction registration in nav drawer is clickable */
    @Test
    fun checkTransactionRegistrationisClickable(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_description)).check((matches(isDisplayed())))
    }
    /* check header text */
    @Test
    fun checkHeaderShowsTransactionRegistration(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
    }
    /* check Balance Before Transaction is visible or not */
    @Test
    fun checkBalanceBeforeTransactionText(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())

        Espresso.onView(ViewMatchers.withId(R.id.tv_balance_before_transaction)).check((matches(isDisplayed())))
    }
    /* check Balance Before Transaction Amount is visible or not */
    @Test
    fun checkBalanceBeforeTransactionAmount(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())

        Espresso.onView(ViewMatchers.withId(R.id.et_balance_before_transaction)).check((matches(isDisplayed())))
    }
    @Test
    fun checkFields(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_item)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.tv_description)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.tv_price)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.currency_label)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.tv_receipt)).check((matches(isDisplayed())))
    }
    @Test
    fun checkItemSpinnerExpensetext(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.item_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("Expense")).perform(click())
        Espresso.onView(ViewMatchers.withText("Expense")).check((matches(isDisplayed())))
    }
    @Test
    fun checkItemSpinnerBankDeposittext(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.item_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("Bank Deposit")).perform(click())
        Espresso.onView(ViewMatchers.withText("Bank Deposit")).check((matches(isDisplayed())))
    }
    @Test
    fun checkOnClickExpenseSpinner(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.item_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("Expense")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_description)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.et_description)).perform(typeText("Enter Description"))
        Espresso.onView(ViewMatchers.withId(R.id.tv_price)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.et_price)).perform(typeText("1000"))
        Espresso.onView(ViewMatchers.withId(R.id.currency_label)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.tv_receipt)).check((matches(isDisplayed())))
    }
    @Test
    fun checkOnClickBankDepositSpinner(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.item_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("Bank Deposit")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_description)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withText("To Stanbic Bank")).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.tv_price)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.et_price)).perform(typeText("1000"))
        Espresso.onView(ViewMatchers.withId(R.id.currency_label)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.tv_receipt)).check((matches(not(isDisplayed()))))
    }
    @Test
    fun checkOnReceiptSpinerYesText(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.receipt_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("YES")).perform(click())
        Espresso.onView(ViewMatchers.withText("YES")).check((matches(isDisplayed())))

    }
    @Test
    fun checkOnReceiptSpinerNOText(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.receipt_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("NO")).perform(click())
        Espresso.onView(ViewMatchers.withText("NO")).check((matches(isDisplayed())))

    }
    @Test
    fun checkSignatureFragment(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.receipt_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("NO")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_add_sign)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.view_signature)).check((matches(isDisplayed())))

    }
    @Test
    fun checkSignatureBox(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.receipt_spinner)).perform(click())
        Espresso.onView(ViewMatchers.withText("NO")).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_add_sign)).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.btn_add_sign)).perform(click())
        Espresso.onView(ViewMatchers.withText("OK")).check((matches(isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.alertTitle)).check((matches(isDisplayed())))

    }

    @Test
    fun checkSaveButtonIsDisplayed(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withText("SAVE")).check((matches(isDisplayed())))
    }
    @Test
    fun checkSaveButtonIsClickable(){
        Espresso.onView(ViewMatchers.withText(R.string.transaction_reg)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.et_description)).perform(typeText("Enter Description"))
        Espresso.onView(ViewMatchers.withId(R.id.et_price)).perform(typeText("1000"),
            closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withText("SAVE")).perform(click())
        Espresso.onView(ViewMatchers.withText("ONGOING")).check((matches(isDisplayed())))

    }

}