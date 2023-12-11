package `in`.ymsli.ymlibrary.utils.dateutil

import org.junit.Test

import org.junit.Assert.*
import java.util.*

class DateComparatorTest {

    @Test
    fun isBefore() {

    }

    @Test
    fun expireDays() {
        val expiryDays:Long =DateComparator().expireDays(1533120376000,0)
        assertNotEquals(0,expiryDays)
    }

    @Test
    fun calculateDifference() {

     val hashmap:HashMap<String,Long>  =DateComparator().calculateDifference(1533120376000,1533120375000)
      assertEquals("-1",hashmap.get("seconds").toString())
    }

    @Test
    fun calculateDifference1() {

    }

    @Test
    fun getCalenderInstance(){

        assertNotNull( DateUtils.getCalendarInstance(0).get(Calendar.DATE))
    }
}