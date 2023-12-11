package `in`.ymsli.ymlibrary.utils

import android.content.Context
import android.support.test.InstrumentationRegistry
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class LocaleUtilsTest {

    lateinit var instrumentationCtx: Context;

    @Before
    fun setUp() {

        instrumentationCtx = InstrumentationRegistry.getContext();
    }

    @Test
    fun setLocale() {
    LocaleUtils.getInstance(instrumentationCtx).setLocale(instrumentationCtx,"")

    }
}