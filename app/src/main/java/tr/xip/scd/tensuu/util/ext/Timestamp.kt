package tr.xip.scd.tensuu.util.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * UNIX timestamp related extensions
 */

/**
 * Assumes this long is a UNIX timestamp and adds given days worth of milliseconds to it.
 */
fun Long.plusDays(days: Int): Long {
    return this + (Math.abs(days) * 24 * 60 * 60 * 1000)
}

/**
 * Assumes this long is a UNIX timestamp and subtracts given days worth of milliseconds to it.
 */
fun Long.minusDays(days: Int): Long {
    return this - (Math.abs(days) * 24 * 60 * 60 * 1000)
}

/**
 * Assumes this long is a UNIX timestamp and checks if it's equal to today's timestamp.
 */
fun Long.isToday(): Boolean {
    return System.currentTimeMillis().stripToDate() == this.stripToDate()
}

/**
 * Assumes this long is a UNIX timestamp and checks if it's equal to yesterdays's timestamp.
 */
fun Long.isYesterday(): Boolean {
    return System.currentTimeMillis().minusDays(1).stripToDate() == this.stripToDate()
}

/**
 * Assumes this long is a UNIX timestamp and strips it of its hour, minute, second, and millisecond
 * values, then returns the resulting timestamp.
 */
fun Long.stripToDate(): Long {
    val cal = GregorianCalendar()
    cal.timeInMillis = this
    return cal.stripToDate().timeInMillis
}

/**
 * Assumes this is a UNIX timestamp and returns a formatted String from it.
 *
 * Example: Jun 2, 2017
 */
@SuppressLint("SimpleDateFormat")
fun Long.toMonthDayYearString(): String {
    return SimpleDateFormat("MMM d, yyyy").format(this)
}