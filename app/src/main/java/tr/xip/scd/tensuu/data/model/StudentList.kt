package tr.xip.scd.tensuu.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class StudentList : RealmObject() {
    var name: String? = null
    var owner: User? = null
    var public = false
    var students = RealmList<Student>()
    var createdAt: Long = System.currentTimeMillis()
}