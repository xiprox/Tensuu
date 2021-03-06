package tr.xip.scd.tensuu.ui.students

import android.view.View
import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.realm.model.Student

interface StudentsView : MvpView {
    fun setSearchExitVisible(value: Boolean = true)

    fun setAdapter(value: StudentsAdapter)
    fun getAdapter(): StudentsAdapter?

    fun onStudentClicked(view: View)

    fun startStudentActivity(student: Student, rangeStart: Long?, rangeEnd: Long?)
    fun runOnUiThread(body: () -> Unit)
}