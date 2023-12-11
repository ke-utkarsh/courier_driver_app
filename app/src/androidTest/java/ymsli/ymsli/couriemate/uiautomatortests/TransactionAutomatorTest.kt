package ymsli.ymsli.couriemate.uiautomatortests

import androidx.test.uiautomator.UiSelector
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import ymsli.com.couriemate.R

class TransactionAutomatorTest : ymsli.ymsli.couriemate.uiautomatortests.BaseAutomatorTest() {

    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun validateItemSpinnerExpense(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Expense")).click()
        device.findObject(UiSelector().text("Expense")).click()
        assert( device.findObject(UiSelector().text("Expense")).exists())

    }
    @Test
    fun validateItemSpinnerDeposit(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Expense")).click()
        device.findObject(UiSelector().text("Bank Deposit")).click()
        assert( device.findObject(UiSelector().text("Bank Deposit")).exists())

    }
    @Test
    fun validateItemSpinnerDepositDescriptionNotEditable(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Expense")).click()
        device.findObject(UiSelector().text("Bank Deposit")).click()
       device.findObject(UiSelector().text("To Stabic Bank")).setText("hello")
        assertFalse(device.findObject(UiSelector().text("hello")).exists())

    }


    @Test
    fun validateItemSpinnerDepositPrefilledDescription(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Expense")).click()
        device.findObject(UiSelector().text("Bank Deposit")).click()
        assert( device.findObject(UiSelector().text("To Stabic Bank")).exists())

    }
    @Test
    fun validateItemSpinnerReceiptIsVisibleOnExpense(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        assert( device.findObject(UiSelector().text("Receipt")).exists())
        assert( device.findObject(UiSelector().text("Yes")).exists())

    }
    @Test
    fun validateItemSpinnerReceiptIsVisibleOnDeposit(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Expense")).click()
        device.findObject(UiSelector().text("Bank Deposit")).click()
        assertFalse( device.findObject(UiSelector().text("Receipt")).exists())
        assertFalse( device.findObject(UiSelector().text("Yes")).exists())
    }
    @Test
    fun validateReceiptSpinnerIsVisibleSignatureFragment(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Yes")).click()
        device.findObject(UiSelector().text("No")).click()
        assert( device.findObject(UiSelector().text("Add Signature")).exists())
        assert(device.findObject(UiSelector().resourceId(resources.getString(R.string.view_signature))).exists())
    }
    @Test
    fun validateReceiptSpinnerYes(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Yes")).click()
        device.findObject(UiSelector().text("Yes")).click()
        assert( device.findObject(UiSelector().text("Yes")).exists())

    }
    @Test
    fun validateReceiptSpinnerNo(){
        launchMainActivity()
        openNavDrawer()
        device.findObject(UiSelector().text("Transaction Registration")).click()
        device.findObject(UiSelector().text("Yes")).click()
        device.findObject(UiSelector().text("No")).click()
        assert(device.findObject(UiSelector().text("No")).exists())

    }


}