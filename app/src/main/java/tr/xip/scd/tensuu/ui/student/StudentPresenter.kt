package tr.xip.scd.tensuu.ui.student

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.realm.model.PointFields
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.realm.model.StudentFields
import tr.xip.scd.tensuu.common.ui.mvp.RealmPresenter
import tr.xip.scd.tensuu.ui.common.adapter.PointsAdapter
import java.text.SimpleDateFormat

class StudentPresenter : RealmPresenter<StudentView>() {
    private var student: Student? = null

    fun loadWith(extras: Bundle?) {
        if (extras == null) {
            throw IllegalArgumentException("No extra passed to ${StudentActivity::class.java.simpleName}")
        }

        val ssid = extras.getString(StudentActivity.ARG_STUDENT_SSID)
        val rangeStart = extras.getLong(StudentActivity.ARG_RANGE_START)
        val rangeEnd = extras.getLong(StudentActivity.ARG_RANGE_END)

        student = realm.where(Student::class.java)
                .equalTo(StudentFields.SSID, ssid)
                .findFirst()

        if (student != null) {
            view?.setTitle(student?.fullName ?: "?")

            view?.setSsid(student?.ssid)

            view?.setGrade(student?.grade)

            view?.setFloor(student?.floor)

            if (rangeStart != 0L && rangeEnd != 0L) {
                view?.showRestrictedRange()
                view?.setRestrictedRangeText(rangeStart, rangeEnd)
            }

            setPoints(student?.ssid, rangeStart, rangeEnd)
            setAdapter(student?.ssid, rangeStart, rangeEnd)

            /* Notify for the initial state */
            view?.notifyDateChanged()
        } else {
            view?.showToast(context.getString(R.string.error_couldnt_find_student))
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

    fun onRestrictedRangeClosed() {
        view?.showRestrictedRange(false)
        setPoints(student?.ssid, 0, 0)
        setAdapter(student?.ssid, 0, 0)
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

    @SuppressLint("SimpleDateFormat")
    fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            R.id.action_share -> {
                if (student == null) return

                var pointsBody = ""
                val format = SimpleDateFormat("dd/MM/yyyy")
                val data = view?.getAdapter()?.data
                if (data != null) {
                    val dates = data.map { it.timestamp }

                    for (date in dates) {
                        pointsBody += "${format.format(date)}: "
                        for (item in data) {
                            if (item.timestamp == date) {
                                pointsBody += "${item.amount} "
                            }
                        }
                        pointsBody += "\n"
                    }
                }

                val sum = data?.sum(PointFields.AMOUNT)
                val final = context.getString(
                        R.string.share_student_points,
                        student!!.fullName,
                        pointsBody,
                        "${sum ?: "0"} (${100 - (sum?.toInt() ?: 0)})"
                )

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, final)
                intent.type = "text/plain"
                context.startActivity(intent)
            }
        }
    }
}