package tr.xip.scd.tensuu.realm.util

import io.realm.*
import tr.xip.scd.tensuu.realm.RealmConfig

/**
 * Realm stuff
 */
object RealmUtils {
    private val syncConfig by lazy {
        SyncConfiguration.Builder(SyncUser.currentUser(), "realm://${RealmConfig.URL}/~/tensuu-sdc-2017")
                .schemaVersion(3)
                .build()
    }

    fun syncedRealm(): Realm {
        return Realm.getInstance(syncConfig)
    }
}
