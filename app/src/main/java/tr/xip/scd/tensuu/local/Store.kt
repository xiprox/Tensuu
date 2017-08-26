package tr.xip.scd.tensuu.local

import android.content.Context
import android.content.SharedPreferences
import tr.xip.scd.tensuu.App

/**
 * A SharedPreferences manager object that takes care of storing various simple data
 */
object Store {
    private val NAME = "store"

    private val KEY_LAST_POINT_ADD_TIMESTAMP = "last_point_add_timestamp"
    private val KEY_LAST_POINT_ADD_TIMESTAMP_UPDATED = "last_point_add_timestamp_updated"

    private var prefs: SharedPreferences

    init {
        prefs = App.context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    var lastPointAddTimestamp: Long
        set(value) = prefs.edit().putLong(KEY_LAST_POINT_ADD_TIMESTAMP, value).apply()
        get() = prefs.getLong(KEY_LAST_POINT_ADD_TIMESTAMP, System.currentTimeMillis())

    var lastPointTimestampUpdated: Long
        set(value) = prefs.edit().putLong(KEY_LAST_POINT_ADD_TIMESTAMP_UPDATED, value).apply()
        get() = prefs.getLong(KEY_LAST_POINT_ADD_TIMESTAMP_UPDATED, System.currentTimeMillis() - 1000)
}