package tr.xip.scd.tensuu.util.ext

import java.util.*
import java.util.Calendar.*

/**
 * Calendar related extensions
 */

var Calendar.year: Int
    get() = get(YEAR)
    set(value) = set(YEAR, value)

var Calendar.month: Int
    get() = get(MONTH)
    set(value) = set(MONTH, value)

var Calendar.dayOfMonth
    get() = get(DAY_OF_MONTH)
    set(value) = set(DAY_OF_MONTH, value)

var Calendar.dayOfWeek
    get() = get(DAY_OF_WEEK)
    set(value) = set(DAY_OF_WEEK, value)

fun Calendar.addChainable(field: Int, amount: Int): Calendar {
    add(field, amount)
    return this
}

/**
 * Strips this calendar of its hour, minute, second, and millisecond values, then returns the
 * resulting Calendar object.
 */
fun Calendar.stripToDate(): Calendar {
    set(HOUR_OF_DAY, 0)
    set(HOUR, 0)
    set(MINUTE, 0)
    set(SECOND, 0)
    set(MILLISECOND, 0)
    return this
}

/**
 * Applies [Calendar.stripToDate] on this calendar and returns a timestamp from the resulting calendar.
 */
fun Calendar.strippedTimestamp(): Long {
    return stripToDate().timeInMillis
}

fun Calendar.setTimeInMillisAndReturn(time: Long): Calendar {
    timeInMillis = time
    return this
}