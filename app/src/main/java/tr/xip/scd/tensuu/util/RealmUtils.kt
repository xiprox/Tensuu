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
    val lastPointId by lazy {
        if (SyncUser.currentUser() != null) {
            syncedRealm().use {
                AtomicInteger((it.where(Point::class.java).max("id") ?: 0).toInt())
            }
        } else {
            AtomicInteger(0)
        }
    }

    private val syncConfig by lazy {
        SyncConfiguration.Builder(
                SyncUser.currentUser(),
                "realm://${RealmConfig.URL}/~/tensuu"
        ).build()
    }

    fun syncedRealm(): Realm {
        return Realm.getInstance(syncConfig)
    }
}
