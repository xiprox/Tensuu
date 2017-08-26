package tr.xip.scd.tensuu.util

import io.realm.Realm
import io.realm.SyncConfiguration
import io.realm.SyncUser
import tr.xip.scd.tensuu.data.model.Point
import tr.xip.scd.tensuu.local.RealmConfig
import java.util.concurrent.atomic.AtomicInteger

/**
 * Realm stuff
 */
object RealmUtils {
    private val syncConfig by lazy {
        SyncConfiguration.Builder(
                SyncUser.currentUser(),
                "realm://${RealmConfig.URL}/~/tensuu-sdc-2017"
        ).schemaVersion(2).build()
    }

    fun syncedRealm(): Realm {
        return Realm.getInstance(syncConfig)
    }
}
