package tr.xip.scd.tensuu.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Period : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var start: Long = 0
    var end: Long = 0
}