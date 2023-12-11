package ymsli.ymsli.couriemate

import org.junit.runner.RunWith
import org.junit.runners.Suite
import ymsli.ymsli.couriemate.espressotests.MainActivityEspressoTest
import ymsli.ymsli.couriemate.espressotests.TaskSummaryTest
import ymsli.ymsli.couriemate.uiautomatortests.*
import ymsli.ymsli.couriemate.uiautomatortests.mainactivitytests.*

@RunWith(Suite::class)
@Suite.SuiteClasses(
     MainActivityEspressoTest::class,
        TaskSummaryTest::class, CallOperatorTest::class, ChangePasswordAutomatorTest::class,
        AssignedFragmentAutomatorTest::class, DoneFragmentAuromatorTest::class, NotificationAutomatorTest::class,
        OngoingFragmentAutomatorTest::class, ReturnTaskAutomatorTest::class,
        CallOperatorTest::class,TaskHistoryTest::class, TransactionAutomatorTest::class, LoginAutomatorTest::class,
        MainActivityAutomatorTest::class
)
class AndroidTestSuite