package tr.xip.scd.tensuu.ui.reports

import android.view.View
import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.data.model.Student

interface CustomRangeView : MvpView {
    fun setAdapter(value: ReportsAdapter)
    fun getAdapter(): ReportsAdapter?
    fun setStartDateText(value: String)
    fun setEndDateText(value: String)

    fun onStudentClicked(view: View)

    fun showDatePickedDialog(y: Int, m: Int, d: Int, listener: (y: Int, m: Int, d: Int) -> Unit)
    fun startStudentActivity(student: Student, start: Long, end: Long)
}