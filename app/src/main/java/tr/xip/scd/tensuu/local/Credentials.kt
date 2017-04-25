package tr.xip.scd.tensuu.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import io.realm.SyncUser
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.data.model.User
import tr.xip.scd.tensuu.util.ext.unitify

/**
 * A SharedPreferences manager object that takes care of storing user credentials.
 */
@SuppressLint("CommitPrefEdits")
object Credentials {
    private val NAME = "credentials_storage"

    private val KEY_EMAIL = "email"
    private val KEY_NAME = "name"
    private val KEY_ADMIN = "admin"
    private val KEY_MODIFY = "modify"

    private var prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    var email: String?
        set(value) = prefs.edit().putString(KEY_EMAIL, value).commit().unitify()
        get() = prefs.getString(KEY_EMAIL, null)

    var name: String?
        set(value) = prefs.edit().putString(KEY_NAME, value).commit().unitify()
        get() = prefs.getString(KEY_NAME, null)

    var isAdmin: Boolean
        set(value) = prefs.edit().putBoolean(KEY_ADMIN, value).commit().unitify()
        get() = prefs.getBoolean(KEY_ADMIN, false)

    var canModify: Boolean
        set(value) = prefs.edit().putBoolean(KEY_MODIFY, value).commit().unitify()
        get() = prefs.getBoolean(KEY_MODIFY, false)

    fun loadFrom(user: User) {
        email = user.email
        isAdmin = user.isAdmin ?: false
        canModify = user.canModify ?: false
        name = user.name
    }

    fun logout() {
        SyncUser.currentUser().logout()
        prefs.edit().clear().commit()
    }
}