package tr.xip.scd.tensuu.ui.reports

import io.realm.Sort
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.data.model.StudentFields
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter
import tr.xip.scd.tensuu.util.ext.dayOfMonth
import tr.xip.scd.tensuu.util.ext.month
import tr.xip.scd.tensuu.util.ext.stripToDate
import tr.xip.scd.tensuu.util.ext.year
import java.text.SimpleDateFormat
import java.util.*

class CustomRangePresenter : RealmPresenter<CustomRangeView>() {
    private val start: Calendar by lazy {
        val cal = GregorianCalendar()
        cal.add(Calendar.DAY_OF_YEAR, -60)
        cal.stripToDate()
    }
    private var end = GregorianCalendar().stripToDate()

    fun init() {
        view?.setAdapter(
                ReportsAdapter(
                        realm,
                        start.timeInMillis,
                        end.timeInMillis,
                        false,
                        realm.where(Student::class.java)
                                .findAll()
                                .sort(
                                        arrayOf(StudentFields.FLOOR, StudentFields.LAST_NAME, StudentFields.FIRST_NAME),
                                        arrayOf(Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING)
                                ),
                        { view?.onStudentClicked(it) }
                )
        )
        onDataChanged()
    }

    private fun setStartDateText() {
        view?.setStartDateText(SimpleDateFormat("MMM d ''yy").format(start.timeInMillis))
    }

    private fun setEndDateText() {
        view?.setEndDateText(SimpleDateFormat("MMM d ''yy").format(end.timeInMillis))
    }

    private fun reloadData() {
        view?.getAdapter()?.updateDates(
                start.timeInMillis,
                end.timeInMillis
        )
    }

    fun onStartDateClicked() {
        view?.showDatePickedDialog(start.year, start.month, start.dayOfMonth, { y, m, d ->
            start.set(y, m, d)
            onDataChanged()
        })
    }

    fun onEndDateClicked() {
        view?.showDatePickedDialog(end.year, end.month, end.dayOfMonth, { y, m, d ->
            end.set(y, m, d)
            onDataChanged()
        })
    }

    fun onDataChanged() {
        setStartDateText()
        setEndDateText()
        reloadData()
    }

    fun onStudentClicked(student: Student?) {
        if (student == null) return
        view?.startStudentActivity(student, start.timeInMillis, end.timeInMillis)
    }
}