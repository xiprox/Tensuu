package tr.xip.scd.tensuu.realm.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class Student : RealmObject() {
    var ssid: String? = null
    var firstName: String? = null
    var notificationToken: String? = null
    var lastName: String? = null
    var grade: String? = null
    var floor: Int? = null
    var fullName: String? = null
    var fullNameSimplified: String? = null
    var password: String? = null
}