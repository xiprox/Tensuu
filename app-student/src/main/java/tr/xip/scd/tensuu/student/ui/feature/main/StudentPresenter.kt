package tr.xip.scd.tensuu.student.ui.feature.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MenuItem
import io.realm.SyncUser
import tr.xip.scd.tensuu.common.ui.mvp.RealmPresenter
import tr.xip.scd.tensuu.realm.model.*
import tr.xip.scd.tensuu.student.R
import tr.xip.scd.tensuu.student.ui.feature.local.Credentials
import java.text.SimpleDateFormat

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

        if (student != null) {
            view?.setName(student?.fullName ?: "?")
            view?.setSsid(student?.ssid)
            view?.setGrade(student?.grade)
            view?.setFloor(student?.floor)
            setPoints()
            setAdapter()

            /* Notify for the initial state */
            view?.notifyDateChanged()
        } else {
            view?.showToast("Couldn't find student")
            view?.die()
        }
    }

    private fun setPoints() {
        val period = realm.where(Period::class.java).findFirst()!!
        var query = realm.where(Point::class.java)
                .equalTo(PointFields.TO.SSID, student?.ssid)
        if (period.start != 0L && period.end != 0L) {
            query = query
                    .greaterThanOrEqualTo(PointFields.TIMESTAMP, period.start)
                    .lessThanOrEqualTo(PointFields.TIMESTAMP, period.end)
        }
        view?.setPoints(
                100 + query.sum(PointFields.AMOUNT).toInt()
        ).toString()
    }

    private fun setAdapter() {
        val period = realm.where(Period::class.java).findFirst()!!
        var query = realm.where(Point::class.java).equalTo(PointFields.TO.SSID, student?.ssid)
        if (period.start != 0L && period.end != 0L) {
            query = query
                    .greaterThan(PointFields.TIMESTAMP, period.start)
                    .lessThan(PointFields.TIMESTAMP, period.end)
        }
        view?.setAdapter(PointsAdapter(query.findAll()))
    }

    fun onDataChanged() {
        setPoints()
        view?.setFlipperChild(
                if (view?.getAdapter()?.itemCount == 0) {
                    StudentActivity.FLIPPER_NO_POINTS
                } else {
                    StudentActivity.FLIPPER_CONTENT
                }
        )
    }

    fun onSignedOut() {
        Credentials.logout()
        view?.startLoginActivity()
        view?.die()
    }

    @SuppressLint("SimpleDateFormat")
    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.action_sign_out -> {
                view?.showSignOutDialog()
            }
        }
    }

    fun goToLogin() {
        view?.startLoginActivity()
        view?.die()
    }
}