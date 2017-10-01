package tr.xip.scd.tensuu.student.ui.feature.main

import io.realm.SyncUser
import tr.xip.scd.tensuu.common.ui.mvp.RealmPresenter
import tr.xip.scd.tensuu.realm.model.*
import tr.xip.scd.tensuu.student.ui.feature.local.Credentials

class StudentPresenter : RealmPresenter<StudentView>() {
    private var student: Student? = null

    fun init() {
        if (SyncUser.currentUser() != null) {
            val student = realm.where(Student::class.java)
                    .equalTo(StudentFields.SSID, Credentials.id)
                    .equalTo(StudentFields.PASSWORD, Credentials.password)
                    .findFirst()

            if (student != null) {
                load()
            } else {
                goToLogin()
            }
        } else {
            goToLogin()
        }
    }

    private fun load() {
        student = realm.where(Student::class.java)
                .equalTo(StudentFields.SSID, Credentials.id)
                .equalTo(StudentFields.PASSWORD, Credentials.password)
                .findFirst()

        val period = realm.where(Period::class.java).findFirst()

        if (student != null && period != null) {
            view?.setTitle(student?.fullName ?: "?")
            view?.setSsid(student?.ssid)
            view?.setGrade(student?.grade)
            view?.setFloor(student?.floor)

            setPoints(student?.ssid, period.start, period.end)
            setAdapter(student?.ssid, period.start, period.end)

            /* Notify for the initial state */
            view?.notifyDateChanged()
        } else {
            view?.showToast("Couldn't find student")
            view?.die()
        }
    }

    private fun setPoints(ssid: String?, rangeStart: Long, rangeEnd: Long) {
        var query = realm.where(Point::class.java)
                .equalTo(PointFields.TO.SSID, ssid)
        if (rangeStart != 0L && rangeEnd != 0L) {
            query = query
                    .greaterThanOrEqualTo(PointFields.TIMESTAMP, rangeStart)
                    .lessThanOrEqualTo(PointFields.TIMESTAMP, rangeEnd)
        }
        view?.setPoints(
                100 + query.sum(PointFields.AMOUNT).toInt()
        ).toString()
    }

    private fun setAdapter(ssid: String?, rangeStart: Long, rangeEnd: Long) {
        var query = realm.where(Point::class.java).equalTo(PointFields.TO.SSID, ssid)
        if (rangeStart != 0L && rangeEnd != 0L) {
            query = query
                    .greaterThan(PointFields.TIMESTAMP, rangeStart)
                    .lessThan(PointFields.TIMESTAMP, rangeEnd)
        }
        view?.setAdapter(PointsAdapter(query.findAll()))
    }

    fun onDataChanged() {
        view?.setFlipperChild(
                if (view?.getAdapter()?.itemCount == 0) {
                    StudentActivity.FLIPPER_NO_POINTS
                } else {
                    StudentActivity.FLIPPER_CONTENT
                }
        )
    }

    fun goToLogin() {
        view?.startLoginActivity()
        view?.die()
    }
}