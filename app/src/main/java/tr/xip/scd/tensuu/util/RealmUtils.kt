package tr.xip.scd.tensuu.util

import android.content.Intent
import io.realm.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.local.RealmConfig
import tr.xip.scd.tensuu.ui.login.LoginActivity

/**
 * Realm stuff
 */
object RealmUtils {
    private val syncConfig by lazy {
        SyncConfiguration.Builder(SyncUser.currentUser(), "realm://${RealmConfig.URL}/~/tensuu-sdc-2017")
                .schemaVersion(3)
                .errorHandler { session, error ->
                    /* Log the user out if credentials invalid so that they can re-login */
                    if (error.errorCode.intValue() == 611) {
                        Credentials.logout()
                        App.context.startActivity(Intent(App.context, LoginActivity::class.java))
                    }
                }
                .build()
    }

    fun syncedRealm(): Realm {
        return Realm.getInstance(syncConfig)
    }
}
