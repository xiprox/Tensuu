package tr.xip.scd.tensuu.ui.reports

import io.realm.Sort
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.data.model.StudentFields
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter
import tr.xip.scd.tensuu.util.ext.addChainable
import tr.xip.scd.tensuu.util.ext.dayOfWeek
import tr.xip.scd.tensuu.util.ext.stripToDate
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class WeeklyPresenter : RealmPresenter<WeeklyView>() {
    private var cal = Calendar.getInstance().stripToDate()

    fun init() {
        view?.setAdapter(
                ReportsAdapter(
                        realm,
                        cal.timeInMillis,
                        cal.addChainable(DAY_OF_YEAR, 7).timeInMillis,
                        true,
                        realm.where(Student::class.java)
                                .findAll()
                                .sort(
                                        arrayOf(StudentFields.FLOOR,
                                                StudentFields.LAST_NAME,
                                                StudentFields.FIRST_NAME),
                                        arrayOf(Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING)
                                ),
                        { view?.onStudentClicked(it) }
                )
        )
        goToThisWeek()
    }

    fun shiftStartDayBy(days: Int) {
        cal.add(DAY_OF_YEAR, days)
        onDataChanged()
    }

    fun goToThisWeek() {
        cal = Calendar.getInstance().stripToDate()
        val daysSince = when (cal.dayOfWeek) {
            MONDAY -> 0
            TUESDAY -> 1
            WEDNESDAY -> 2
            THURSDAY -> 3
            FRIDAY -> 4
            SATURDAY -> 5
            SUNDAY -> 6
            else -> 0
        }
        shiftStartDayBy(-daysSince)
    }

    private fun setCurrentWeekText() {
        view?.setCurrentWeekText(
                context.getString(
                        R.string.week_of_s,
                        SimpleDateFormat("MMM d").format(cal.timeInMillis)
                )
        )
    }

    private fun reloadData() {
        val endCal = GregorianCalendar()
        endCal.timeInMillis = cal.timeInMillis
        endCal.add(DAY_OF_YEAR, 7)
        view?.getAdapter()?.updateDates(
                cal.timeInMillis,
                endCal.timeInMillis
        )
    }

    fun onNextWeekClicked() {
        shiftStartDayBy(7)
    }

    fun onCurrentWeekClicked() {
        goToThisWeek()
    }

    fun onPreviousWeekClicked() {
        shiftStartDayBy(-7)
    }

    fun onDataChanged() {
        setCurrentWeekText()
        reloadData()
    }

    fun onStudentClicked(student: Student?) {
        if (student == null) return

        val endCal = GregorianCalendar()
        endCal.timeInMillis = cal.timeInMillis
        endCal.add(DAY_OF_YEAR, 7)
        view?.startStudentActivity(student, cal.timeInMillis, endCal.timeInMillis)
    }
}