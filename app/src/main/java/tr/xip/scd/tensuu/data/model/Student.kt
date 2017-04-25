package tr.xip.scd.tensuu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Student : RealmObject() {
    @PrimaryKey
    var id: Int = 0

    var ssid: String? = null

    var firstName: String? = null
    var lastName: String? = null
    var grade: String? = null
    var floor: Int? = null
    var fullName: String? = null
    var fullNameSimplified: String? = null
}