package tr.xip.scd.tensuu.ui.reports

import android.view.View
import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.data.model.Student

interface WeeklyView : MvpView {
    fun setAdapter(value: ReportsAdapter)
    fun getAdapter(): ReportsAdapter?
    fun setCurrentWeekText(value: String)

    fun onStudentClicked(view: View)

    fun startStudentActivity(student: Student, start: Long, end: Long)
}