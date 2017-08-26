package tr.xip.scd.tensuu.data.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class PointReason : RealmObject() {
    var text: String? = null
}