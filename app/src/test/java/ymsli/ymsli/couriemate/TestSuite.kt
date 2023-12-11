package ymsli.ymsli.couriemate

import ymsli.ymsli.couriemate.viewmodeltests.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginViewModelTest::class,
    SplashViewModelTest::class,
    TaskDetailViewModelTest::class,
    TaskListViewModelTest::class,
    ValidationUnitTest::class,
    ReturnTaskListViewModelTest::class,
ChangePasswordUnitTest::class,
LoginUnitTest::class,
ValidationUnitTest::class)
class TestSuite