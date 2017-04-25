package tr.xip.scd.tensuu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Point : RealmObject() {
    @PrimaryKey
    var id: Int = -1

    var to: Student? = null
    var from: User? = null
    var amount: Int? = null
    var timestamp: Long? = null
    var reason: String? = null
}