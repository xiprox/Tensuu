package tr.xip.scd.tensuu.student.ui.feature.local

import android.content.Context
import android.content.SharedPreferences
import io.realm.SyncUser
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.student.App

/**
 * A SharedPreferences manager object that takes care of storing student credentials.
 */
object Credentials {
    private val NAME = "credentials_storage"

    private val KEY_ID = "id"
    private val KEY_PASSWORD = "password"
    private val KEY_NOTIFICATIONS_TOKEN = "notifications_token"

    private var prefs: SharedPreferences

    init {
        prefs = App.context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    var id: String?
        set(value) = prefs.edit().putString(KEY_ID, value).apply()
        get() = prefs.getString(KEY_ID, null)

    var password: String?
        set(value) = prefs.edit().putString(KEY_PASSWORD, value).apply()
        get() = prefs.getString(KEY_PASSWORD, null)

    var notificationsToken: String?
        set(value) = prefs.edit().putString(KEY_NOTIFICATIONS_TOKEN, value).apply()
        get() = prefs.getString(KEY_NOTIFICATIONS_TOKEN, null)

    fun loadFrom(student: Student) {
        id = student.ssid
        password = student.password
    }

    fun logout() {
        SyncUser.currentUser().logout()
        prefs.edit().clear().apply()
    }
}