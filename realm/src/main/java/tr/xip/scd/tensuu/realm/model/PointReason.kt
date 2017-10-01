package tr.xip.scd.tensuu.realm.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class PointReason : RealmObject() {
    var text: String? = null
}