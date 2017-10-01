package tr.xip.scd.tensuu.ui.reports

import io.realm.Sort
import tr.xip.scd.tensuu.realm.model.Period
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.realm.model.StudentFields
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter
import tr.xip.scd.tensuu.util.ext.*
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
        val period = realm.where(Period::class.java).findFirst()
        if (period != null) {
            start.timeInMillis = period.start
            end.timeInMillis = period.end
        }

        view?.setAdapter(
                ReportsAdapter(
                        realm,
                        start.strippedTimestamp(),
                        end.strippedTimestamp().toEndOfTheDay(),
                        false,
                        realm.where(Student::class.java)
                                .findAll()
                                .sort(
                                        arrayOf(StudentFields.FLOOR,
                                                StudentFields.GRADE,
                                                StudentFields.LAST_NAME,
                                                StudentFields.FIRST_NAME),
                                        arrayOf(Sort.ASCENDING,
                                                Sort.ASCENDING,
                                                Sort.ASCENDING,
                                                Sort.ASCENDING)
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
                start.strippedTimestamp(),
                end.strippedTimestamp().toEndOfTheDay()
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