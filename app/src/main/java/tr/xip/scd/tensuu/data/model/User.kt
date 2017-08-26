package tr.xip.scd.tensuu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class User : RealmObject() {
    var email: String? = null
    var password: String? = null
    var name: String? = null
    var isAdmin: Boolean? = null
    var canModify: Boolean? = null
    var createdAt: Long = System.currentTimeMillis()
}