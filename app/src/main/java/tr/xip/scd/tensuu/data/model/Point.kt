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

    /**
     * ID generation function trying to create something random. Call this after setting the other
     * fields....
     */
    fun createId(): Int {
        val timeString = "$timestamp"
        val timestampSection = timeString.substring(timeString.length - 8, timeString.length -4)
        val name = to?.firstName ?: "unknown"
        val amountSection = Math.abs(amount ?: 0)
        val nameSection = name.length
        return (timestampSection + amountSection + nameSection).toInt()
    }
}